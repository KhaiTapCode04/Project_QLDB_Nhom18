package ui.view
//ten file test -> login
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.viewmodel.contact.Constact_state
import ui.viewmodel.contact.Email_state
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state
import ui.viewmodel.users.UserService


@SuppressLint("SuspiciousIndentation")
@Composable
fun UserScreen(navController: NavHostController, userViewModel: User_state,
               contactViewModel: Constact_state,
               emailViewModel: Email_state) {
    val context = LocalContext.current
    val user = UserPreferencesManager(context)

    LaunchedEffect(Unit) {
//        goi ham load
//        dữ liệu gắn cho userviewmodel
        userViewModel.loadUserData(context)
    }
//kiem tra bien quyet dinh
    if (userViewModel.navigateToHome) {
        userViewModel.setNavigatetoAfalse()
        // Điều hướng khi có tín hiệu
//      goi tiep Laucheffect dam bao goi 1 lan view khac thoi
        LaunchedEffect(Unit) {
            navController.navigate("homedb") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val name by userViewModel.userName.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (name.isNotEmpty()) {
            Text(
                text = "Xin chào, $name",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = null
            },
            label = { Text("Tên đăng nhập") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
                    return@Button
                }

                isLoading = true
                errorMessage = null

                scope.launch {
//                    context
                    try {
                        val loginHandler = withContext(Dispatchers.IO) {
                            UserService.login(context,username, password)
                        }

                        if (loginHandler != null) {
                            userViewModel.updateUser(
                                loginHandler.id,
                                loginHandler.username,
                                loginHandler.email,
                                loginHandler.phone.toString(),
                                loginHandler.profile_picture.toString()
                            )

//                            Navigation().get_contact(contactViewModel, context)
                            contactViewModel.get_contact(context)
//                            Navigation().get_email(emailViewModel,context)
                            emailViewModel.get_email(context)
                            navController.navigate("a"){
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
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Đăng nhập")
            }
        }
    }
}
