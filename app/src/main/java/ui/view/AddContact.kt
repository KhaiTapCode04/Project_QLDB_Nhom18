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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.model.Group
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.viewmodel.contact.ContactViewModel
import ui.viewmodel.contact.state.AddContact_state
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state

@Composable
fun AddContact(
    navController: NavHostController,
    AddContact_state: AddContact_state,
    Contact_state: Contact_state,
    Email_state:Email_state,
    Phone_state:Phone_state
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

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var groupError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        AddContact_state.getGroup() // Ensure groups are loaded
    }

    LaunchedEffect(addContactResult) {
        addContactResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm liên hệ mới", style = TextStyle(color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xB587FF95)),
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại") } },
                actions = { IconButton(onClick = { navController.navigate("profile") }) { Icon(Icons.Default.Person, "Hồ sơ", Modifier.size(48.dp)) } }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Thêm thông tin liên hệ mới", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Họ và tên
                FieldLabel("Họ và tên")
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            if (it.length <= 50) {
                                AddContact_state._name.value = it
                                nameError = null
                            } else {
                                nameError = "Tên không được vượt quá 50 ký tự"
                            }
                        },
                        isError = nameError != null,
                        placeholder = { Text("Nhập họ và tên", color = Color(0xFF757575)) },
                        leadingIcon = { Icon(Icons.Default.Person, "Họ và tên", tint = Color(0xFF616161)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                    nameError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp),
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Số điện thoại
                FieldLabel("Số điện thoại")
                Column {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            val cleaned = it.filter { char -> char.isDigit() }
                            if (cleaned.length <= 15) {
                                AddContact_state._phone.value = cleaned
                                phoneError = if (cleaned.isEmpty()) "Số điện thoại không được để trống"
                                else if (cleaned.length < 10) "Số điện thoại tối thiểu 10 chữ số"
                                else null
                            } else {
                                phoneError = "Số điện thoại tối đa 15 chữ số"
                            }
                        },
                        isError = phoneError != null,
                        placeholder = { Text("Nhập số điện thoại", color = Color(0xFF757575)) },
                        leadingIcon = { Icon(Icons.Default.Phone, "Số điện thoại", tint = Color(0xFF616161)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                    phoneError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp),
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                FieldLabel("Email")
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            AddContact_state._email.value = it
                            emailError = if (it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches())
                                "Email không hợp lệ" else null
                        },
                        isError = emailError != null,
                        placeholder = { Text("Nhập email", color = Color(0xFF757575)) },
                        leadingIcon = { Icon(Icons.Default.Email, "Email", tint = Color(0xFF616161)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                    emailError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp),
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nhóm
                FieldLabel("Nhóm")
                Column {
                    GroupDropdownMenu(
                        selectedGroup = selectedGroup?.group_name ?: "Chọn nhóm",
                        groups = groups,
                        onGroupSelected = {
                            AddContact_state._selectedGroup.value = it
                            groupError = null
                        }
                    )
                    if (groups.isEmpty()) {
                        Text(
                            "Không có nhóm nào để chọn",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                            fontSize = 12.sp
                        )
                    } else if (selectedGroup == null) {
                        Text(
                            "Vui lòng chọn một nhóm",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Button lưu
                Button(
                    onClick = {
                        nameError = if (name.isBlank()) "Tên không được để trống" else if (name.length > 50) "Tên không được vượt quá 50 ký tự" else null
                        phoneError = if (phone.isBlank()) "Số điện thoại không được để trống"
                        else if (!phone.all { it.isDigit() }) "Số điện thoại chỉ chứa số"
                        else if (phone.length < 10) "Số điện thoại tối thiểu 10 chữ số"
                        else if (phone.length > 15) "Số điện thoại tối đa 15 chữ số"
                        else null
                        emailError = if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email không hợp lệ" else null
                        groupError = if (selectedGroup == null && groups.isNotEmpty()) "Vui lòng chọn một nhóm" else null

                        if (nameError == null && phoneError == null && emailError == null && groupError == null) {
                            scope.launch {
                                ContactViewModel().addContact(AddContact_state, Contact_state, Email_state, Phone_state,context)
                                Toast.makeText(context, "Đã lưu liên hệ", Toast.LENGTH_SHORT).show()
                                delay(1000)
                                navController.navigate("homedb") {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        } else {
                            Toast.makeText(context, "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text("Lưu", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
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