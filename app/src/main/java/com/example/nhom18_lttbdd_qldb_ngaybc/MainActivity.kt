package com.example.nhom18_lttbdd_qldb_ngaybc

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nhom18_lttbdd_qldb_ngaybc.adapter.ContactAdapter
import com.example.nhom18_lttbdd_qldb_ngaybc.adapter.UserAdapter
import com.example.nhom18_lttbdd_qldb_ngaybc.api.ApiService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var checkButton: Button
    private lateinit var contactsRecyclerView: RecyclerView
    private val apiService = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        checkButton = findViewById(R.id.checkConnectionButton)
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView)

        checkButton.setOnClickListener {
            checkDatabaseConnection()
        }


        // Gọi hàm để lấy danh sách người dùng
        /*getAllContacts()*/
        getAllUsers()
    }
    private fun getAllUsers() {
        lifecycleScope.launch {
            try {
                val response = apiService.getAllUsers()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.status == "success") {
                        // Hiển thị danh sách người dùng
                        val users = result.data ?: emptyList() // Đây là List<User>
                        val adapter = UserAdapter(users) // Sử dụng UserAdapter
                        contactsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        contactsRecyclerView.adapter = adapter
                    } else {
                        statusTextView.text = "Lỗi: ${result?.message ?: "Không xác định"}"
                    }
                } else {
                    statusTextView.text = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                statusTextView.text = "Lỗi kết nối: ${e.message}"
                Toast.makeText(this@MainActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkDatabaseConnection() {
        lifecycleScope.launch {
            try {
                val response = apiService.checkConnection(true)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.status == "success") {
                        statusTextView.text = "Kết nối thành công: ${result.message}"
                    } else {
                        statusTextView.text = "Lỗi: ${result?.message ?: "Không xác định"}"
                    }
                } else {
                    statusTextView.text = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                statusTextView.text = "Lỗi kết nối: ${e.message}"
                Toast.makeText(this@MainActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /*private fun getAllContacts() {
        lifecycleScope.launch {
            try {
                val response = apiService.getAllContacts()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.status == "success") {
                        // Hiển thị danh sách người dùng
                        val contacts = result.data ?: emptyList()
                        val adapter = ContactAdapter(contacts)
                        contactsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        contactsRecyclerView.adapter = adapter
                    } else {
                        statusTextView.text = "Lỗi: ${result?.message ?: "Không xác định"}"
                    }
                } else {
                    statusTextView.text = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                statusTextView.text = "Lỗi kết nối: ${e.message}"
                Toast.makeText(this@MainActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }*/
}