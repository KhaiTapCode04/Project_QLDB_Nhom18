package ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import ui.view.components.BottomNavigationBar

// ✅ Data class
data class SettingItem(val title: String, val iconResId: Int)

@Composable
fun Setting(navController: NavHostController) {
    val darkGreen = Color(0xFF2E7D32)
    var showDialog by remember { mutableStateOf(false) }

    val settingsItems = listOf(
        SettingItem("Thông tin tài khoản", R.drawable.ic_profile),
        SettingItem("Xuất/Nhập danh bạ", R.drawable.ic_export),
        SettingItem("Danh sách chặn", R.drawable.ic_block),
        SettingItem("Thùng rác", R.drawable.ic_delete),
        SettingItem("Giao diện", R.drawable.ic_theme),
        SettingItem("Giới thiệu", R.drawable.ic_info)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cài đặt",
                        style = TextStyle(
                            color = Color(0xFF000000),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xB587FF95)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
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
        bottomBar = {
            BottomNavigationBar(navController,"setting")
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2))
                .padding(16.dp)
        ) {
            items(settingsItems) { item ->
                SettingsItemView(item = item, iconColor = darkGreen, onClick = {
                    if (item.title == "Xuất/Nhập danh bạ") showDialog = true
                })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (showDialog) {
        ExportImportDialog(
            onDismiss = { showDialog = false },
            onExportClick = {
                // Thực hiện xuất danh bạ
                showDialog = false
            },
            onImportClick = {
                // Thực hiện nhập danh bạ
                showDialog = false
            }
        )
    }
}

@Composable
fun SettingsItemView(item: SettingItem, iconColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0F2F1)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = item.title,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(iconColor)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.Gray
            )
        }
    }
}



@Composable
fun ExportImportDialog(
    onDismiss: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = "Xuất/Nhập Danh Bạ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(top = 4.dp) // Adjust top padding as needed
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng"
                    )
                }
            }
        },
        text = {
            Column {
                Button(
                    onClick = onExportClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Xuất danh bạ",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Xuất danh bạ", color = Color.White)
                }

                Button(
                    onClick = onImportClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "Nhập danh bạ",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nhập danh bạ", color = Color.White)
                }
            }
        },
        confirmButton = {},
        dismissButton = {} // Dismiss button left empty
    )
}