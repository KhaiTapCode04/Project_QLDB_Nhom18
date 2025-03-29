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

    // Composable cho màn hình đăng nhập
    @Composable
    fun LoginScreen(onSignInSuccess: (FirebaseUser) -> Unit) {
        val context = LocalContext.current

        // Launcher để xử lý kết quả từ Intent đăng nhập Google
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Lấy GoogleSignInAccount từ Intent
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                // Lấy ID token từ tài khoản và đăng nhập với Firebase
                account.idToken?.let { token ->
                    firebaseAuthWithGoogle(token) { user ->
                        // Callback khi đăng nhập thành công
                        onSignInSuccess(user)
                    }
                }
            } catch (e: ApiException) {
                // Đăng nhập Google thất bại
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(context, "Đăng nhập thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Giao diện đăng nhập
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Material 3 sử dụng colorScheme thay vì colors
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Đăng nhập vào ứng dụng",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 50.dp)
                )

                Button(
                    onClick = {
                        // Bắt đầu luồng đăng nhập Google
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4) // Material 3 sử dụng containerColor thay vì backgroundColor
                    )
                ) {
                    Text(
                        text = "Đăng nhập bằng Google",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // Composable cho màn hình chính sau khi đăng nhập
    @Composable
    fun MainScreen(user: FirebaseUser?, onSignOut: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Xin chào, ${user?.displayName ?: "Người dùng"}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = user?.email ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 50.dp)
                )

                Button(
                    onClick = onSignOut,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Đăng xuất",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // Phương thức xác thực với Firebase bằng Google credential
    private fun firebaseAuthWithGoogle(idToken: String, onSuccess: (FirebaseUser) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    user?.let { onSuccess(it) }
                } else {
                    // Đăng nhập thất bại
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this, "Xác thực thất bại: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Phương thức đăng xuất
    private fun signOut() {
        // Đăng xuất khỏi Firebase
        auth.signOut()

        // Đăng xuất khỏi Google
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
        }
    }
}
