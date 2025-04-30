// AddContactActivity.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.activities


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.AddContactViewModel


@Composable
fun AddContactScreen(
    navController: NavController,
    viewModel: AddContactViewModel = viewModel()
) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm liên lạc") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = { viewModel.name.value = it },
                label = { Text("Tên") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.value = it },
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            /*OutlinedTextField(
                value = viewModel.birthday.value,
                onValueChange = { viewModel.birthday.value = it },
                label = { Text("Ngày sinh") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.address.value,
                onValueChange = { viewModel.address.value = it },
                label = { Text("Địa chỉ") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.note.value,
                onValueChange = { viewModel.note.value = it },
                label = { Text("Ghi chú") },
                modifier = Modifier.fillMaxWidth()
            )*/

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    viewModel.addContact(context)
                    Log.d("ket qua them contact:","Ket qua: ${viewModel.addContactResult.value}")


                }) {
                    Text("Lưu")
                }

                OutlinedButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Hủy")
                }
            }
        }
    }
}
