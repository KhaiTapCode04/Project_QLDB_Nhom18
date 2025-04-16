//// SplashActivity.kt
//package com.example.project_qldb_nhom18.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import android.widget.Button
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import com.example.nhom18_lttbdd_qldb_ngaybc.R
//import com.example.nhom18_lttbdd_qldb_ngaybc.activities.LoginActivity
//import com.example.nhom18_lttbdd_qldb_ngaybc.network.NetworkUtils
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class SplashActivity : AppCompatActivity() {
//
//    private lateinit var progressBar: ProgressBar
//    private lateinit var statusTextView: TextView
//    private lateinit var retryButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        // Khởi tạo các view
//        progressBar = findViewById(R.id.progressBarSplash)
//        statusTextView = findViewById(R.id.tvConnectionStatus)
//        retryButton = findViewById(R.id.btnRetryConnection)
//
//        // Thiết lập sự kiện click cho nút thử lại
//        retryButton.setOnClickListener {
//            retryButton.visibility = View.GONE
//            checkDatabaseConnection()
//        }
//
//        // Kiểm tra kết nối database khi màn hình splash được tạo
//        checkDatabaseConnection()
//    }
//
//    /**
//     * Kiểm tra kết nối đến cơ sở dữ liệu
//     */
//    private fun checkDatabaseConnection() {
//        // Hiển thị trạng thái đang kiểm tra
//        statusTextView.visibility = View.VISIBLE
//        statusTextView.text = getString(R.string.checking_connection)
//        statusTextView.setTextColor(ContextCompat.getColor(this, R.color.colorNeutral))
//        progressBar.visibility = View.VISIBLE
//
//        // Thực hiện kiểm tra kết nối trong background thread
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val connectionResult = NetworkUtils.testDatabaseConnectionWithDetails()
//
//                if (connectionResult.contains("Kết nối thành công")) {
//                    // Kết nối thành công
//                    statusTextView.text = connectionResult
//                    statusTextView.setTextColor(ContextCompat.getColor(this@SplashActivity, R.color.colorSuccess))
//
//                    // Đợi một chút để người dùng thấy thông báo thành công
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        navigateToMainScreen()
//                    }, 1500)
//                } else {
//                    // Kết nối thất bại
//                    showConnectionError(connectionResult)
//                }
//            } catch (e: Exception) {
//                showConnectionError("Lỗi kiểm tra kết nối: ${e.message}")
//            }
//        }
//    }
//
//    /**
//     * Hiển thị lỗi kết nối và nút thử lại
//     */
//    private fun showConnectionError(errorMessage: String) {
//        statusTextView.text = errorMessage
//        statusTextView.setTextColor(ContextCompat.getColor(this, R.color.colorError))
//        progressBar.visibility = View.GONE
//        retryButton.visibility = View.VISIBLE
//    }
//
//    /**
//     * Chuyển đến màn hình chính sau khi kiểm tra kết nối thành công
//     */
//    private fun navigateToMainScreen() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish() // Đóng activity splash
//    }
//}