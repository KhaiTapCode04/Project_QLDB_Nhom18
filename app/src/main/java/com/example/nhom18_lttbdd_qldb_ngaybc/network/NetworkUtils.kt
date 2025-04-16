//package com.example.nhom18_lttbdd_qldb_ngaybc.network
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
///**
// * Lớp tiện ích để quản lý các tác vụ mạng
// */
//class NetworkUtils {
//    companion object {
//        /**
//         * Kiểm tra xem thiết bị có kết nối internet không
//         * @param context Context của ứng dụng
//         * @return true nếu có kết nối internet, false nếu không
//         */
//        fun isNetworkAvailable(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val network = connectivityManager.activeNetwork ?: return false
//            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
//
//            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//        }
//
//        /**
//         * Kiểm tra kết nối database trong coroutine
//         * @return true nếu kết nối thành công, false nếu thất bại
//         */
//        suspend fun testDatabaseConnection(): Boolean {
//            return withContext(Dispatchers.IO) {
//                ApiClient.testConnection()
//            }
//        }
//
//        /**
//         * Kiểm tra kết nối database và trả về thông báo chi tiết trong coroutine
//         * @return Chuỗi thông báo kết quả kết nối
//         */
//        suspend fun testDatabaseConnectionWithDetails(): String {
//            return withContext(Dispatchers.IO) {
//                ApiClient.testConnectionWithDetails()
//            }
//        }
//    }
//}