import android.content.Context
import android.net.Uri
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
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.ConactPreferencesManage
import ui.viewmodel.contact.Contact_state
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.users.UserPreferencesManager


suspend fun reload_contact(viewModel: Contact_state, context: Context) {
        val user_id = UserPreferencesManager(context).getUserId()
        val contact = Contact_service().get_contact(user_id)
        contact.forEach {
            ConactPreferencesManage(context).saveOrUpdateContact(it)
            viewModel.addOrUpdateContact(it)
    }
}

@Composable
fun ContactListScreen(navController: NavHostController, viewModel: Contact_state, context: Context) {
    LaunchedEffect(Unit) {
        Navigation().get_contact(viewModel, context)
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




        bottomBar = {BottomNavigationBar()},

        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add contact action */ },
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

