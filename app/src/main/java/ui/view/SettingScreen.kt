package ui.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ui.view.components.BottomNavigationBar

@Composable
fun SettingScreen(navController: NavHostController, context: Context) {

    // Danh sách option (title, icon, route)
    val settingOptions = remember {
        listOf(
            SettingOption("Thông tin tài khoản", Icons.Default.Person, "profile"),
            SettingOption("Xuất/Nhập danh bạ", Icons.Default.Upload, "export_import"),
            SettingOption("Danh sách chặn", Icons.Default.Block, "block_list"),
            SettingOption("Thùng rác", Icons.Default.Delete, "trash"),
            SettingOption("Giao diện", Icons.Default.BrightnessMedium, "theme"),
            SettingOption("Giới thiệu", Icons.Default.Info, "about")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài Đặt") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Hồ sơ",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Card chứa các option
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                Column {

                    settingOptions.forEachIndexed { index, option ->
                        SettingItem(option = option, onClick = {
                            navController.navigate(option.route)
                        })

                        if (index != settingOptions.lastIndex) {
                            Divider()
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SettingItem(option: SettingOption, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = option.title,
            tint = Color(0xFF4CAF50), // Màu xanh giống hình bạn gửi
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Đi tới",
            tint = Color.Gray
        )
    }
}

data class SettingOption(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
