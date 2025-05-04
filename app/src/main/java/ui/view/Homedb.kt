import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import data.model.Contact
import ui.view.Navigation
import ui.viewmodel.contact.ConactPreferencesManage
import ui.viewmodel.contact.Constact_state
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.users.UserPreferencesManager










@Composable
fun ContactListScreen(navController: NavHostController, viewModel: Constact_state, context: Context) {
    LaunchedEffect(Unit) {
        viewModel.get_contact(context)
    }
    val contactList by viewModel.contacts.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Hồ Sơ") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Profile action */ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    navController.navigate("profile")
                                },
                        )
                    }
                })
        },

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Danh bạ tab action */ },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
                    label = { Text("Danh bạ") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Nhóm tab action */ },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
                    label = { Text("Nhóm") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Cài đặt tab action */ },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
                    label = { Text("Cài đặt") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add contact action */
                navController.navigate("addcontact")
                },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Contact")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Bar with Filter
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tìm kiếm liên hệ") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Search",
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("profile")
                                },
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* Filter action */ }) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }

            LazyColumn {
                items(contactList) { contact ->
                    ContactListItem(contact, navController)
                }
            }
        }
    }
}

@Composable
fun ContactListItem(contact: Contact, navController: NavHostController) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                val gson = Gson()
                val contactJson = Uri.encode(gson.toJson(contact))
                navController.navigate("ContactDetail/$contactJson")
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder Profile Image
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Contact Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.contact_id.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = contact.group_id.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        // More Options with Dropdown Menu
        Box {
            IconButton(onClick = { showDropdownMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }

            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Xem chi tiết") },
                    onClick = {
                        // Handle view details action
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Xem chi tiết"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Chỉnh sửa") },
                    onClick = {
                        // Handle edit action
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Chỉnh sửa"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Chặn") },
                    onClick = {
// Handle block action
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Chặn"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Xóa") },
                    onClick = {
                        // Handle delete action
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Xóa"
                        )
                    }
                )
            }
        }
    }
}

//fun ContactListScreen(navController: NavHostController, viewModel: Constact_state, context: Context) {
//    LaunchedEffect(Unit) {
//        get_contact(viewModel,context)
//    }
//    val contactList by viewModel.contacts.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//
//                title = { Text("Hồ Sơ") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Quay lại"
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* Profile action */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = "profile",
//                            modifier = Modifier
//                                .size(48.dp)
//                                .clickable{
//                                    navController.navigate("profile")
//                                },
//                        )
//                    }
//                })
//        },
//
//        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    selected = true,
//                    onClick = { /* Danh bạ tab action */ },
//                    icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
//                    label = { Text("Danh bạ") }
//                )
//                NavigationBarItem(
//                    selected = false,
//                    onClick = { /* Nhóm tab action */ },
//                    icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
//                    label = { Text("Nhóm") }
//                )
//                NavigationBarItem(
//                    selected = false,
//                    onClick = { /* Cài đặt tab action */ },
//                    icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
//                    label = { Text("Cài đặt") }
//                )
//            }
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /* Add contact action */ },
//                containerColor = Color(0xFF2196F3)
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Add Contact")
//            }
//        }
//    ) { paddingValues ->
//
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//            // Search Bar with Filter
//            Divider()
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                TextField(
//                    value = "",
//                    onValueChange = {},
//                    placeholder = { Text("Tìm kiếm liên hệ") },
//                    modifier = Modifier
//                        .weight(1f)
//                        .clip(RoundedCornerShape(8.dp)),
//                    singleLine = true,
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = "Search",
//                            modifier = Modifier
//                                .clickable{
//                                    navController.navigate("profile")
//                                },
//                        )
//                    }
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                IconButton(onClick = { /* Filter action */ }) {
//                    Icon(
//                        imageVector = Icons.Outlined.FilterList,
//                        contentDescription = "Filter"
//                    )
//                }
//            }
//
//            // Contact List
//            LazyColumn {
//                items(contactList) { contact ->
//                    ContactListItem(contact, navController)
//                }
//            }
//        }
//    }
//}