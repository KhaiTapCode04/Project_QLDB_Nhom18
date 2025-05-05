package ui.view

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ui.viewmodel.group.GroupViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ui.viewmodel.group.GroupViewModelFactory

@Composable
fun GroupScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val viewModel: GroupViewModel = viewModel(factory = GroupViewModelFactory(context))
    val groups by viewModel.groups.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        isLoading.value = true
        viewModel.loadGroupsFromCache()
        isLoading.value = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhóm") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("contactlist") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Danh bạ") },
                    label = { Text("Danh bạ") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Nhóm tab - hiện tại */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Nhóm") },
                    label = { Text("Nhóm") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Cài đặt tab */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Cài đặt") },
                    label = { Text("Cài đặt") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addgroupscreen")
                },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Thêm nhóm")
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing.value),
            onRefresh = {
                isRefreshing.value = true
                viewModel.fetchGroups()
                isRefreshing.value = false
            },
            modifier = Modifier.padding(paddingValues)
        ) {
            // ⚡️ Luôn sử dụng LazyColumn (kể cả khi groups rỗng)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = if (groups.isEmpty()) Arrangement.Center else Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    isLoading.value -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    groups.isEmpty() -> {
                        item {
                            Text("Chưa có nhóm nào được tạo.")
                        }
                    }

                    else -> {
                        items(groups) { group ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = group.name, style = MaterialTheme.typography.titleMedium)
                                    Text(text = group.description, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
