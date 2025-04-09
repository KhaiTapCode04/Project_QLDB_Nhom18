


package com.example.nhom18_lttbdd_qldb_ngaybc


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                    ContactListScreen()


        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen() {
    val mintGreen = Color(0xFFE0FFE0)
    val darkGreen = Color(0xFF1A4D1A)

    val contacts = listOf(
        Contact(Name("Nguyễn","Trung Hiếu"), "0344466721", 'K'),
        Contact(Name("Đặng", "Huỳnh Khải"), "0372116712", 'K'),
        Contact(Name("Lê","Thị Nhung"), "xxxxxxxxx", 'N')
    )

    val groupedContacts = contacts.groupBy { it.name.first.first() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mintGreen)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Danh bạ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGreen
                )
            }



            // Search and Menu Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(40.dp),
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(40.dp),
                        tint = Color.Black
                    )
                }

                SearchBar(
                    modifier = Modifier.weight(1f),
                    onSearch = { /* TODO */ }
                )

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                            .size(40.dp),
                    ){
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle),
                        contentDescription = "Account",
                        modifier = Modifier
                            .size(40.dp),
                        tint = Color.Black
                    )
                }
            }

            // All Contacts Dropdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id =  R.drawable.supervisor_account),
                        modifier = Modifier
                            .size(24.dp),
                        contentDescription = "Contacts",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tất cả liên hệ",
                        color = Color.Black,
                        fontSize = 22.sp,)
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_ios_new),
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }

                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_list),
                        contentDescription = "Sort",
                        modifier = Modifier.size(35.dp),
                        tint = Color.Black
                    )
                }
            }

            // Contact List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                groupedContacts.forEach { (initial, contactsForInitial) ->
                    item {
                        Text(
                            text = initial.toString(),
                            color = Color.Red,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    items(contactsForInitial) { contact ->
                        ContactItem(contact = contact)
                    }
                }
            }

            // Bottom Navigation
            BottomNavigation()
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFFFD9B3),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Contact",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.LightGray.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.initial.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${contact.name.first} ${contact.name.last}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.phone,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(50.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = { Text("Tìm kiếm...", color = Color.Black) },
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = R.drawable.seach),
                contentDescription = "Search",
                tint = Color.Black
            )
        },
        singleLine = true
    )
}

@Composable
fun BottomNavigation() {
    Surface(
        color = mintGreen,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavigationItem(
                icon = Icons.Default.Person,
                label = "Danh bạ",
                isSelected = true
            )

            BottomNavigationItem(
                icon = Icons.Default.Menu,
                label = "Nhóm",
                isSelected = false
            )

            BottomNavigationItem(
                icon = Icons.Outlined.Settings,
                label = "Cài đặt",
                isSelected = false
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Black else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

data class Contact(
    val name:  Name,
    val phone: String,
    val initial: Char
)
data class Name(
    val first: String,
    val last: String,
)

// Màu mint green được sử dụng trong ứng dụng
val mintGreen = Color(0xFFE0FFE0)

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {
    ContactListScreen()
}
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.unit.sp
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
//import androidx.compose.material.icons.filled.Image
////import androidx.compose.material.icons.filled.Image
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ElevatedButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MenuDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.motionEventSpy
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ContactEditScreen()
//        }
//    }
//}
//val darkBackground = Color(0xFF121212)
//val goldColor = Color(0xFFE6C28C)
//val brownCircle = Color(0xFF6D604A)
//val darkFieldColor = Color(0xFF1E1E1E)
////val scrollState = rememberScrollState()
//@Composable
//fun ContactEditScreen() {
//        Taskbar()
//}
//@Composable
//fun Taskbar(){
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(0.dp)
//            .background(Color(0xFFe5e5e5))
//            .padding(vertical = 5.dp),
//
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ){
//        IconButton(onClick = {}) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                modifier = Modifier
//                    .size(20.dp),
//                contentDescription = "Back",
//                tint = Color.Black
//            )
//        }
//        Text(
//            text = "Chỉnh sửa người liên hệ",
//            color = Color.Black,
//            fontSize = 20.sp
//        )
//        Spacer(modifier = Modifier.width(15.dp))
////        Button(
////            onClick = { /* Handle save */ },
////            modifier = Modifier
////                .width(70.dp)
////                .height(40.dp),
////            colors = ButtonDefaults.buttonColors(
////                containerColor = Color(0xFF1976D2)),
//////            shape = RoundedCornerShape(16.dp),
////            contentPadding = PaddingValues(0.dp)
////        )
////
//        ElevatedButton(
//            onClick = { /* xử lý sự kiện click */ },
//            modifier = Modifier
//                .width(70.dp)
//                .height(40.dp),
//            colors = ButtonDefaults.elevatedButtonColors(
//                containerColor = Color(0xFF2393e8),
//            ),
//            elevation = ButtonDefaults.elevatedButtonElevation(
//                defaultElevation = 3.dp,
//                pressedElevation = 3.dp,
//                focusedElevation = 3.dp,
//                hoveredElevation = 3.dp
//            ),
//            contentPadding = PaddingValues(0.dp)
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "save",
//                    color = Color.Black,
//                    fontSize = 14.sp,
//                    textAlign = TextAlign.Center,
//                    maxLines = 1
//                )
//            }
//        }
//            IconButton(
//                onClick = { /* Handle more options */ },
//            ) {
//                Icon(
//                    modifier = Modifier,
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = "More options",
//                    tint = Color.Black
//                )
//            }
//        }
//    }

            // Phần nội dung có thể cuộn
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
//
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

                // Last Name Field
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

                // Company Field
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

//                 Phone Section
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
//                                text = "hí",
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
//update

