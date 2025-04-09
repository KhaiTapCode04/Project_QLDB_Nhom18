package com.example.nhom18_lttbdd_qldb_ngaybc

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nhom18_lttbdd_qldb_ngaybc.databinding.ActivityMainBinding
import com.example.nhom18_lttbdd_qldb_ngaybc.network.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập StrictMode để cho phép kết nối mạng trong main thread (chỉ dùng cho demo)
        setupThreadPolicy()

        // Thiết lập sự kiện click cho nút kiểm tra kết nối
        binding.btnTestConnection.setOnClickListener {
            testDatabaseConnection()
        }
    }

    /**
     * Thiết lập ThreadPolicy để cho phép kết nối mạng trên main thread
     * CHỈ SỬ DỤNG CHO MỤC ĐÍCH DEMO, không nên dùng trong sản phẩm thực tế
     */
    private fun setupThreadPolicy() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    /**
     * Kiểm tra kết nối database
     */
    private fun testDatabaseConnection() {
        // Hiển thị trạng thái đang tải
        binding.progressBarConnection.visibility = View.VISIBLE
        binding.tvConnectionStatus.text = getString(R.string.checking_connection)
        binding.tvConnectionStatus.setTextColor(ContextCompat.getColor(this, R.color.color_neutral))
        binding.btnTestConnection.isEnabled = false

        // Thực hiện kiểm tra kết nối trong background
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d(TAG, "Bắt đầu kiểm tra kết nối database...")

                // Kiểm tra kết nối mạng trước
                if (!NetworkUtils.isNetworkAvailable(this@MainActivity)) {
                    binding.tvConnectionStatus.text = "Không có kết nối mạng. Vui lòng kiểm tra Wifi/Dữ liệu di động."
                    binding.tvConnectionStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color_error))
                    Toast.makeText(this@MainActivity, "Không có kết nối mạng", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Kiểm tra kết nối database
                val connectionResult = NetworkUtils.testDatabaseConnectionWithDetails()
                Log.d(TAG, "Kết quả kiểm tra: $connectionResult")

                if (connectionResult.contains("Kết nối thành công")) {
                    // Kết nối thành công
                    binding.tvConnectionStatus.text = connectionResult
                    binding.tvConnectionStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color_success))
                    Toast.makeText(this@MainActivity, "Kết nối CSDL thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    // Kết nối thất bại
                    // Phân tích lỗi cụ thể
                    val errorMessage = when {
                        connectionResult.contains("Communications link failure") ->
                            "Không thể kết nối đến máy chủ MySQL. Vui lòng kiểm tra xem máy chủ có đang chạy không."
                        connectionResult.contains("Access denied") ->
                            "Tài khoản hoặc mật khẩu không chính xác."
                        connectionResult.contains("Unknown database") ->
                            "Cơ sở dữ liệu không tồn tại. Vui lòng tạo cơ sở dữ liệu trước."
                        else -> connectionResult
                    }

                    binding.tvConnectionStatus.text = errorMessage
                    binding.tvConnectionStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color_error))
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ
                Log.e(TAG, "Lỗi kiểm tra kết nối: ${e.message}", e)
                binding.tvConnectionStatus.text = "Lỗi: ${e.message}"
                binding.tvConnectionStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color_error))
            } finally {
                // Ẩn loading và kích hoạt lại nút
                binding.progressBarConnection.visibility = View.GONE
                binding.btnTestConnection.isEnabled = true
            }
        }
    }
}