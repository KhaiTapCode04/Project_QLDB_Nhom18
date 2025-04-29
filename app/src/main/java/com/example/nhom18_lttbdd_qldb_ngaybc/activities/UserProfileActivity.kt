// UserProfileActivity.kt
//ProfileScreen.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.activities

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button

import androidx.compose.material.Surface
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.ProfileViewModel
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current

    // Khi vào màn hình sẽ tự load dữ liệu
    LaunchedEffect(Unit) {
        viewModel.loadUserData(context)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tên: ${viewModel.userName.value}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email: ${viewModel.userEmail.value}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /* TODO: chức năng Edit */
                    navController.navigate("edit_profile")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: chức năng Đăng xuất */
                    viewModel.logout(context) // Xóa dữ liệu
                    navController.navigate("login") { // Điều hướng về màn login
                        popUpTo("profile") { inclusive = true } // Xóa luôn profile khỏi backstack
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng xuất")
            }
        }
    }
}
