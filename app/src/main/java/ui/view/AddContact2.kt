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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import data.model.group
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.AddContactViewModel

@Composable
fun AddContact(navController: NavHostController, viewModel: AddContactViewModel = viewModel()) {

    val context = LocalContext.current
    val groups = viewModel.groups.value
    val addResult = viewModel.addContactResult.value
    val isLoading = viewModel.isLoading.value

    LaunchedEffect(Unit) {
        viewModel.getGroup()
    }

    LaunchedEffect(addResult) {
        addResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm Liên Hệ Mới", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Hồ sơ", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F7FA))
            )
        },
        bottomBar = { BottomNavigationBar() }
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

                Spacer(modifier = Modifier.height(16.dp))

                // Họ và tên
                FieldLabel("Họ và tên")
                OutlinedTextField(
                    value = viewModel.name.value,
                    onValueChange = { viewModel.name.value = it },
                    placeholder = { Text("Nhập họ và tên", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Số điện thoại
                FieldLabel("Số điện thoại")
                OutlinedTextField(
                    value = viewModel.phone.value,
                    onValueChange = { viewModel.phone.value = it },
                    placeholder = { Text("Nhập số điện thoại", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                FieldLabel("Email")
                OutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = { viewModel.email.value = it },
                    placeholder = { Text("Nhập email", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nhóm
                FieldLabel("Nhóm")
                GroupDropdownMenu(
                    selectedGroup = viewModel.selectedGroup.value?.group_name ?: "Chọn nhóm",
                    groups = groups,
                    onGroupSelected = { viewModel.selectedGroup.value = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Button lưu
                Button(
                    onClick = { viewModel.addContact(context) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text(text = "Lưu", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 16.sp,
        color = Color.Black,
        textAlign = TextAlign.Start
    )
}

@Composable
fun GroupDropdownMenu(
    selectedGroup: String,
    groups: List<group>,
    onGroupSelected: (group) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedGroup,
            onValueChange = {},
            placeholder = { Text("Chọn nhóm", color = Color.Gray) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
