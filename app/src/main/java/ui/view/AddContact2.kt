package ui.view
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.model.group
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.Contact_service

@Composable
fun AddContact2(navController: NavHostController) {
    // State variables for form inputs
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("Chọn nhóm") }
    var groups by remember { mutableStateOf<List<group>>(emptyList()) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        groups = Contact_service().get_group()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thêm Liên Hệ Mới",
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Profile action */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Hồ sơ",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F7FA),
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Name Input
            Text(
                text = "Họ và tên",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nhập họ và tên", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Input
            Text(
                text = "Số điện thoại",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Nhập số điện thoại", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            Text(
                text = "Email",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Nhập email", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address Input
            Text(
                text = "Địa chỉ",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = { Text("Nhập địa chỉ", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Group Dropdown
            Text(
                text = "Nhóm",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            GroupDropdownMenu(
                selectedGroup = selectedGroup,
                groups = groups,
                onGroupSelected = { selectedGroup = it.group_name }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    Toast.makeText(context,name+" "+phoneNumber+" "+email+" "+address+" "+selectedGroup,
                        Toast.LENGTH_LONG).show()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Lưu",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun GroupDropdownMenu(
    selectedGroup: String,
    groups: List<group>,
    onGroupSelected: (group) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedGroup,
            onValueChange = {},
            placeholder = { Text("Chọn nhóm", color = Color.Gray) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        // Dropdown menu hiển thị bên dưới
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.group_name) },
                    onClick = {
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}


