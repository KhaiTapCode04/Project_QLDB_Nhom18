//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Close
////import androidx.compose.material.icons.filled.Image
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp


package com.example.nhom18_lttbdd_qldb_ngaybc
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Greeting()
        }
    }
}

@Composable
fun Greeting() {
    // Sử dụng Box với background màu trắng thay vì theme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hi",
                fontSize = 24.sp,
                color = Color.Black
            )
        }
    }
}
//@Composable
//fun ContactEditScreen() {
//    val darkBackground = Color(0xFF121212)
//    val goldColor = Color(0xFFE6C28C)
//    val brownCircle = Color(0xFF6D604A)
//    val darkFieldColor = Color(0xFF1E1E1E)
//
//    val scrollState = rememberScrollState()
//
//    var nameValue by remember { mutableStateOf("Anh Của Trường") }
//    var lastNameValue by remember { mutableStateOf("Họ") }
//    var companyValue by remember { mutableStateOf("Công ty") }
//    var phoneValue by remember { mutableStateOf("0396 294 644") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(darkBackground)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // Top Bar - Giữ ngoài khu vực cuộn
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { /* Handle back */ }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
//
//                Text(
//                    text = "Chỉnh sửa người liên hệ",
//                    color = Color.White,
//                    fontSize = 18.sp
//                )
//
//                Row {
//                    Button(
//                        onClick = { /* Handle save */ },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = goldColor
//                        ),
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Text(
//                            text = "Lưu",
//                            color = Color.Black,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    IconButton(onClick = { /* Handle more options */ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More options",
//                            tint = Color.White
//                        )
//                    }
//                }
//            }
//
//            // Phần nội dung có thể cuộn
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .verticalScroll(scrollState)
//            ) {
//                // Profile Picture Section
//                Box(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .align(Alignment.CenterHorizontally)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(CircleShape)
//                            .background(brownCircle),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Image,
//                            contentDescription = "Add photo",
//                            tint = Color.White,
//                            modifier = Modifier.size(40.dp)
//                        )
//                    }
//                }
//
//                Text(
//                    text = "Thêm ảnh",
//                    color = goldColor,
//                    fontSize = 16.sp,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                // Name Field
//                Text(
//                    text = "Tên",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
//                )
//                TextField(
//                    value = nameValue,
//                    onValueChange = { nameValue = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = darkFieldColor,
//                        focusedContainerColor = darkFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = Color.White,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                // Last Name Field
//                TextField(
//                    value = lastNameValue,
//                    onValueChange = { lastNameValue = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = darkFieldColor,
//                        focusedContainerColor = darkFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = Color.White,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                // Company Field
//                TextField(
//                    value = companyValue,
//                    onValueChange = { companyValue = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = darkFieldColor,
//                        focusedContainerColor = darkFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = Color.White,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                // Phone Section
//                Text(
//                    text = "Điện thoại (Di động)",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(8.dp))
//                                .background(darkFieldColor)
//                                .padding(horizontal = 16.dp, vertical = 12.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            // Vietnam flag
//                            Box(
//                                modifier = Modifier
//                                    .size(24.dp, 16.dp)
//                                    .background(Color.Red)
//                            ) {
//                                Text(
//                                    text = "🇻🇳",
//                                    color = Color.White,
//                                    fontSize = 10.sp,
//                                    modifier = Modifier.align(Alignment.Center)
//                                )
//                            }
//
//                            Text(
//                                text = "▼",
//                                color = Color.White,
//                                fontSize = 10.sp,
//                                modifier = Modifier.padding(horizontal = 4.dp)
//                            )
//
//                            Text(
//                                text = phoneValue,
//                                color = Color.White,
//                                fontSize = 16.sp,
//                                modifier = Modifier.weight(1f)
//                            )
//                        }
//                    }
//
//                    IconButton(onClick = { /* Clear phone */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "Clear phone",
//                            tint = Color.White
//                        )
//                    }
//                }
//
//                Text(
//                    text = "Thêm số điện thoại",
//                    color = goldColor,
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Additional Info Buttons
//                Button(
//                    onClick = { /* Add email */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = brownCircle
//                    ),
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.ic_dialog_email),
//                            contentDescription = "Email",
//                            tint = Color.White,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            text = "Thêm email",
//                            color = Color.White
//                        )
//                    }
//                }
//
//                Button(
//                    onClick = { /* Add birthday */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = brownCircle
//                    ),
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
//                            contentDescription = "Birthday",
//                            tint = Color.White,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            text = "Thêm ngày sinh",
//                            color = Color.White
//                        )
//                    }
//                }
//
//                Button(
//                    onClick = { /* Add address */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = brownCircle
//                    ),
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.ic_dialog_map),
//                            contentDescription = "Address",
//                            tint = Color.White,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            text = "Thêm địa chỉ",
//                            color = Color.White
//                        )
//                    }
//                }
//
//                // For the second image content
//                Button(
//                    onClick = { /* Add to group */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = brownCircle
//                    ),
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = android.R.drawable.ic_menu_add),
//                            contentDescription = "Add to group",
//                            tint = Color.White,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            text = "Thêm vào nhóm",
//                            color = Color.White
//                        )
//                    }
//                }
//
//                TextField(
//                    value = "",
//                    onValueChange = { },
//                    placeholder = { Text("Ghi chú", color = Color.Gray) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp, top = 8.dp),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = darkFieldColor,
//                        focusedContainerColor = darkFieldColor,
//                        unfocusedTextColor = Color.White,
//                        focusedTextColor = Color.White,
//                        cursorColor = Color.White,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                Button(
//                    onClick = { /* Add field */ },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .padding(top = 8.dp, bottom = 16.dp)
//                        .align(Alignment.CenterHorizontally),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Transparent,
//                        contentColor = goldColor
//                    ),
//                    border = BorderStroke(1.dp, goldColor),
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Text(
//                        text = "Thêm trường",
//                        color = goldColor
//                    )
//                }
//
//                // Thêm padding cuối cùng để tránh bị che bởi các hệ thống UI như thanh điều hướng
//                Spacer(modifier = Modifier.height(60.dp))
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ContactEditScreenPreview() {
//    MaterialTheme {
//        ContactEditScreen()
//    }
//}