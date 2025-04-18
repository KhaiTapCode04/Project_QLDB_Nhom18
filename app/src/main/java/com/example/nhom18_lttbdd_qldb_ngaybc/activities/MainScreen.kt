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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val contacts = viewModel.groupedContacts
    val searchQuery = viewModel.searchQuery

    // Fetch data ngay lần đầu vào màn hình
    LaunchedEffect(Unit) {
        viewModel.fetchContacts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh bạ") },
                actions = {
                    IconButton(onClick = { /* TODO: Chuyển tới Profile */ }) {
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
                FloatingActionButton(onClick = { /* TODO: Thêm liên lạc */ }) {
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
                value = searchQuery.value,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Tìm kiếm liên lạc") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                contacts.forEach { (letter, contactsUnderLetter) ->
                    item {
                        Text(
                            text = letter,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(contactsUnderLetter) { contact ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 4.dp
                        ) {
                            ListItem(
                                text = { Text(contact.name) },
                                secondaryText = { Text(contact.phone) } // ✅ Dùng contact.phone (đúng với ViewModel)
                            )
                        }
                    }
                }
            }
        }
    }
}
