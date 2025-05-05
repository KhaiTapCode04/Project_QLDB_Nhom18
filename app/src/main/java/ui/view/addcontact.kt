package ui.view
//
//// AddContactActivity.kt
//
//
//import android.util.Log
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import ui.viewmodel.contact.AddContactViewModel
//
//
//@Composable
//fun AddContactScreen(
//    navController: NavController,
//    viewModel: AddContactViewModel = viewModel()
//) {
//    val resultMessage = viewModel.addContactResult.value
//
//    val context = LocalContext.current
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Thêm liên lạc") }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .padding(24.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedTextField(
//                value = viewModel.name.value,
//                onValueChange = { viewModel.name.value = it },
//                label = { Text("Tên") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = viewModel.phone.value,
//                onValueChange = { viewModel.phone.value = it },
//                label = { Text("Số điện thoại") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = viewModel.email.value,
//                onValueChange = { viewModel.email.value = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Button(onClick = {
//                    viewModel.addContact(context)
//                    Log.d("ket qua them contact:", "Ket qua: ${viewModel.addContactResult.value}")
//                    viewModel.clearAllfields()
//
//
//                }) {
//                    Text("Lưu")
//                }
//
//                OutlinedButton(onClick = {
//                    navController.popBackStack()
//                }) {
//                    Text("Hủy")
//                }
//            }
//            resultMessage?.let { message ->
//                Text(
//                    text = message,
//                    color = Color.Red,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//}
