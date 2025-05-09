package ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import data.model.Contact
import kotlinx.coroutines.launch
import ui.viewmodel.contact.ContactViewModel
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Delete_contact_state
import ui.viewmodel.contact.state.Trash_contact_state

@Composable
fun TrashContactScreen(
    navController: NavHostController,
    context: Context,
    Delete_contact_state: Delete_contact_state,
    Contact_state : Contact_state
) {
    val trashedContacts by Delete_contact_state.deleteContacts.collectAsState()
    val scope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        Delete_contact_state.select_delete_contacts(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thùng rác",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xB587FF95)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    Delete_contact_state.select_delete_contacts(context)
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (trashedContacts.isEmpty()) {
                    item {
                        Text(
                            "Thùng rác trống",
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }
                }

                items(trashedContacts) { trashedContact ->
                    TrashContactCard(
                        context = context,
                        contact = trashedContact,
                        Delete_contact_state = Delete_contact_state,
                        contactState = Contact_state
                    )
                }
            }
        }
    }
}

@Composable
fun TrashContactCard(
    context: Context,
    contact: Contact,
    Delete_contact_state: Delete_contact_state,
    contactState: Contact_state
) {
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val viewModel = ContactViewModel()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    contact.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Trong thùng rác",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Khôi phục")
                        },
                        onClick = {
                            /*scope.launch {
                                viewModel.restoreContact(
                                    contact.contact_id,
                                    trashContactState,
                                    contactState,
                                    context
                                )
                            }*/
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = "Khôi phục",
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Xóa vĩnh viễn")
                        },
                        onClick = {
                            /*scope.launch {
                                viewModel.permanentlyDeleteContact(
                                    contact.contact_id,
                                    trashContactState,
                                    context
                                )
                            }*/
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = "Xóa vĩnh viễn",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}