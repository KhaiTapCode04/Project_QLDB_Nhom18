package com.example.nhom18_lttbdd_qldb_ngaybc.activities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.MainViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val contacts = viewModel.contacts.value

    // Nhóm theo chữ cái đầu
    val grouped = contacts.groupBy { it.name.first().uppercaseChar() }

    // Load danh bạ khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.loadContacts(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh bạ") },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(onClick = { navController.navigate("add_contact") }) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm")
                }
                FloatingActionButton(onClick = { /* TODO: Xóa */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa")
                }
                FloatingActionButton(onClick = { /* TODO: Lọc */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Lọc")
                }
                FloatingActionButton(onClick = { /* TODO: Nhóm */ }) {
                    Icon(Icons.Default.Group, contentDescription = "Nhóm")
                }
                FloatingActionButton(onClick = { /* TODO: Cài đặt */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Cài đặt")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.searchQuery.value,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text("Tìm kiếm liên lạc") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                grouped.forEach { (letter, list) ->
                    item {
                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(list) { contact ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 8.dp)
                        ) {
                            Text(text = contact.name, style = MaterialTheme.typography.body1)
                            Text(text = "SĐT: ${contact.phone.ifBlank { "trống" }}")
                            Text(text = "email: ${contact.email.ifBlank { "trống" }}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}


