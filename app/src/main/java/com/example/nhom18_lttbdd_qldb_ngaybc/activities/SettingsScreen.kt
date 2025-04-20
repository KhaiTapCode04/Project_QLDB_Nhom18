package com.example.nhom18_lttbdd_qldb_ngaybc.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cài đặt",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SettingOption(title = "Tùy chọn giao diện") {
                // TODO: xử lý khi click, chưa cần làm
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            SettingOption(title = "Số đã chặn") {
                // TODO: xử lý khi click, chưa cần làm
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            SettingOption(title = "Nhập từ tệp") {
                // TODO: xử lý khi click, chưa cần làm
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            SettingOption(title = "Xuất sang tệp") {
                // TODO: xử lý khi click, chưa cần làm
            }
        }
    }
}

@Composable
fun SettingOption(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
}
