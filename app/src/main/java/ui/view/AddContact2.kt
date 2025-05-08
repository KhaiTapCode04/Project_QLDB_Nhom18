package ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.model.Group
import kotlinx.coroutines.launch
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.ContactViewModel
import ui.viewmodel.contact.state.AddContact_state
import ui.viewmodel.contact.state.Contact_state

@Composable
fun AddContact(
    navController: NavHostController,
    AddContact_state: AddContact_state,
    Contact_state: Contact_state
) {
    val context = LocalContext.current
    val name by AddContact_state.name.collectAsState()
    val email by AddContact_state.email.collectAsState()
    val phone by AddContact_state.phone.collectAsState()

    val groups by AddContact_state.groups.collectAsState()
    val addContactResult by AddContact_state.addContactResult.collectAsState()
    val isLoading by AddContact_state.isLoading.collectAsState()
    val selectedGroup by AddContact_state.selectedGroup.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        AddContact_state.getGroup()
    }

    LaunchedEffect(addContactResult) {
        addContactResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thêm liên hệ mới",
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
            BottomNavigationBar(navController,"homedb")

        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA)) // Xám trắng nhạt
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Họ và tên
                FieldLabel("Họ và tên")
                OutlinedTextField(
                    value = name,
                    onValueChange = { AddContact_state._name.value = it },
                    placeholder = { Text("Nhập họ và tên", color = Color(0xFF757575)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Họ và tên",
                            tint = Color(0xFF616161)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFB0BEC5)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Số điện thoại
                FieldLabel("Số điện thoại")
                OutlinedTextField(
                    value = phone,
                    onValueChange = { AddContact_state._phone.value = it },
                    placeholder = { Text("Nhập số điện thoại", color = Color(0xFF757575)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Số điện thoại",
                            tint = Color(0xFF616161)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFB0BEC5)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                FieldLabel("Email")
                OutlinedTextField(
                    value = email,
                    onValueChange = { AddContact_state._email.value = it },
                    placeholder = { Text("Nhập email", color = Color(0xFF757575)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color(0xFF616161)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFB0BEC5)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nhóm
                FieldLabel("Nhóm")
                GroupDropdownMenu(
                    selectedGroup = selectedGroup?.group_name ?: "Chọn nhóm",
                    groups = groups,
                    onGroupSelected = { AddContact_state._selectedGroup.value = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Button lưu
                Button(
                    onClick = {
                        scope.launch {
                            ContactViewModel().addContact(AddContact_state, Contact_state, context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Xanh lá
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Lưu",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }
            }
        }
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        textAlign = TextAlign.Start
    )
}

@Composable
fun GroupDropdownMenu(
    selectedGroup: String,
    groups: List<Group>,
    onGroupSelected: (Group) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedGroup,
            onValueChange = {},
            placeholder = { Text("Chọn nhóm", color = Color(0xFF757575)) },
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Nhóm",
                    tint = Color(0xFF616161)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color(0xFFB0BEC5)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.group_name, fontSize = 16.sp) },
                    onClick = {
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}