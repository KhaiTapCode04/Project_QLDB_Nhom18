// MainActivity.kt
package com.example.baithuchanh_tuan5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "FirebaseGoogleAuth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Firebase Auth
        auth = Firebase.auth

        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Sử dụng Web Client ID trực tiếp thay vì qua resource
            .requestIdToken("675503726140-nqvt3m3nehffargc1c1ok4pp3qca2kt7.apps.googleusercontent.com") // Thay thế bằng Web Client ID của bạn từ Firebase Console
            .requestEmail()
            .build()

        // Tạo một GoogleSignInClient với cấu hình đã chỉ định
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            // Thiết lập theme cho Material 3
            MaterialTheme {
                // Kiểm tra trạng thái đăng nhập hiện tại
                val currentUser = remember { mutableStateOf(auth.currentUser) }

                // Giao diện UI dựa trên trạng thái đăng nhập
                if (currentUser.value != null) {
                    // Người dùng đã đăng nhập, hiển thị màn hình chính
                    MainScreen(
                        user = currentUser.value,
                        onSignOut = {
                            signOut()
                            currentUser.value = null
                        }
                    )
                } else {
                    // Người dùng chưa đăng nhập, hiển thị màn hình đăng nhập
                    LoginScreen(
                        onSignInSuccess = { user ->
                            currentUser.value = user
                        }
                    )
                }
            }
        }
    }


hieuknguyen