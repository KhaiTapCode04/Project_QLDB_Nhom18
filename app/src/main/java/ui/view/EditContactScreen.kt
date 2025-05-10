package ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.EditContactViewModel

@Composable
fun EditContactScreen(
    navController: NavHostController,
    context: Context,
    contactId: Int,
    viewModel: EditContactViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val groups by viewModel.groups.collectAsState()
    val selectedGroup by viewModel.selectedGroup.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    // Load nhóm khi mở màn
    LaunchedEffect(Unit) {
        viewModel.getGroup()
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            delay(2000)
            viewModel.clearToastMessage()
            if (toastMessage == "Cập nhật thành công!") {
                navController.navigateUp()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chỉnh sửa liên hệ",
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
        /*bottomBar = {
            BottomNavigationBar(navController)

        }*/
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)) // Xám trắng nhạt
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Cập nhật thông tin liên hệ", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Họ và tên
            FieldLabel("Họ và tên")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
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

            // Số điện thoại
            FieldLabel("Số điện thoại")
            OutlinedTextField(
                value = phone,
                onValueChange = { name = it },
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

            // Email
            FieldLabel("Email")
            OutlinedTextField(
                value = email,
                onValueChange = { name= it },
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
                selectedGroup = selectedGroup?.group_name ?: "",
                groups = groups,
                onGroupSelected = { viewModel._selectedGroup.value = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.updateContact(context, contactId, name, email, phone) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Cập nhập", color = Color.White, fontSize = 19.sp)
                }
            }
        }
    }
}


@Composable
fun EditableTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorText: String = "",
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Medium, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            isError = errorText.isNotEmpty()
        )

        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}