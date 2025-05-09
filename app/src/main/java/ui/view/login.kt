package ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.viewmodel.contact.sharedPreferences.BlockConactPreferencesManage
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.UserService
import ui.viewmodel.users.User_state

@SuppressLint("SuspiciousIndentation")
@Composable
fun UserScreen(
    navController: NavHostController,
    User_state: User_state,
    Contact_state: Contact_state,
    Block_contact_state: Block_contact_state,
    Email_state: Email_state,
    Phone_state: Phone_state
) {
    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    if (user.getUserId() != 0) {
        User_state.updateUser(
            user.getUserId(),
            user.getUserName(),
            user.getUserEmail(),
            user.getUserPhone(),
            user.getProfilePicture()
        )
        ConactPreferencesManage(context).getContactList().forEach {
            Contact_state.addOrUpdateContact(it)
        }
        BlockConactPreferencesManage(context).getBlockContactList().forEach {
            Block_contact_state.addOrUpdateBlockContact(it)
        }
        EmailPreferencesManage(context).getEmailList().forEach {
            Email_state.addOrUpdateEmail(it)
        }

        navController.navigate("homedb") {
            popUpTo("login") { inclusive = true }
        }
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val name by User_state.userName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(), // Chiếm toàn bộ màn hình
                        contentAlignment = Alignment.Center // Căn giữa nội dung
                    ) {
                        Text(
                            text = "Đăng nhập",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xB587FF95)
                ),

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Tiêu đề ứng dụng
            Text(
                text = "Quản lý danh bạ",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (name.isNotEmpty()) {
                Text(
                    text = "Xin chào, $name",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF616161)
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Tên đăng nhập
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null
                },
                label = { Text("Tên đăng nhập", fontSize = 16.sp) },
                placeholder = { Text("Nhập tên đăng nhập", color = Color(0xFF757575)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Tên đăng nhập",
                        tint = Color(0xFF616161)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFB0BEC5),
                    focusedLabelColor = Color(0xFF4CAF50),
                    cursorColor = Color(0xFF4CAF50)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Mật khẩu", fontSize = 16.sp) },
                placeholder = { Text("Nhập mật khẩu", color = Color(0xFF757575)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Mật khẩu",
                        tint = Color(0xFF616161)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFB0BEC5),
                    focusedLabelColor = Color(0xFF4CAF50),
                    cursorColor = Color(0xFF4CAF50)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Thông báo lỗi
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nút đăng nhập
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Vui lòng nhập đầy đủ thông tin"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val loginHandler = withContext(Dispatchers.IO) {
                                UserService.login(context, username, password)
                            }

                            if (loginHandler != null) {
                                User_state.updateUser(
                                    loginHandler.id,
                                    loginHandler.username,
                                    loginHandler.email,
                                    loginHandler.phone.toString(),
                                    loginHandler.profile_picture.toString()
                                )

                                Contact_state.get_contact(context)
                                Block_contact_state.get_block_contacts(context)
                                Email_state.get_email(context)
                                Phone_state.get_phone(context)
                                navController.navigate("homedb") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                errorMessage = "Đăng nhập thất bại. Kiểm tra lại thông tin đăng nhập"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Lỗi: ${e.localizedMessage ?: "Không xác định"}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Xanh lá
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = "Đăng nhập",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Đăng nhập",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Đăng ký/Quên mật khẩu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { navController.navigate("register") },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2196F3))
                ) {
                    Text(
                        text = "Đăng ký",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                TextButton(
                    onClick = { /* TODO: Navigate to forgot password screen */ },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2196F3))
                ) {
                    Text(
                        text = "Quên mật khẩu?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}