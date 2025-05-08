package ui.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.EditContactViewModel

@Composable
fun EditContactScreen(
    navController: NavHostController,
    context: Context,
    contactId: Int,
    viewModel: EditContactViewModel = viewModel()
) {
    // State variables
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var groupId by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chỉnh sửa liên hệ", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Hồ sơ */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color(0xFF666666))
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Chỉnh sửa thông tin liên hệ để cập nhật danh bạ", fontSize = 14.sp, color = Color(0xFF666666))

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            EditableTextField("Họ và tên", name) { name = it }
            Spacer(modifier = Modifier.height(16.dp))

            // Phone
            EditableTextField("Số điện thoại", phoneNumber, keyboardType = KeyboardType.Phone) { phoneNumber = it }
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            EditableTextField("Email", email, keyboardType = KeyboardType.Email) { email = it }
            Spacer(modifier = Modifier.height(16.dp))

            // GroupId
            EditableTextField("Group ID", groupId, keyboardType = KeyboardType.Number) { groupId = it }

            Spacer(modifier = Modifier.height(40.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.updateContact(context, contactId ,name, groupId, email, phoneNumber)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF2196F3), Color(0xFF1976D2))))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Loading + Message
            if (isLoading) {
                CircularProgressIndicator()
            }

            if (message.isNotEmpty()) {
                Text(message, color = Color.Red)
            }
        }
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
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
                .height(56.dp)
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
