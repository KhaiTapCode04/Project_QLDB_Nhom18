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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import data.model.BlockedContact
import data.model.Contact
import kotlinx.coroutines.launch
import ui.viewmodel.contact.state.Block_contact_state

@Composable
fun BlockedContactScreen(
    navController: NavHostController,
    context: Context,
    Block_contact_state: Block_contact_state = viewModel()
) {

    val blockedContacts by Block_contact_state.blockedContacts.collectAsState()
    val scope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        Block_contact_state.get_block_contacts(context)
    }

    if(blockedContacts.isEmpty()){
        Toast.makeText(context,"heheee", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách chặn", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Hồ sơ */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.Gray)
                    }
                }
            )
        }
    ) { paddingValues ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {

                scope.launch {isRefreshing = true
                    Block_contact_state.reload_block_contacts(context)
                    isRefreshing = false
                } },

            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (blockedContacts.isEmpty()) {
                    item {
                        Text("Không có liên hệ nào bị chặn", color = Color.Gray)
                    }
                }

                items(blockedContacts) { blockedContact ->
                    BlockedContactCard(context, blockedContact, Block_contact_state)
                }

            }
        }
    }
}

@Composable
fun BlockedContactCard(context: Context, blockedContact: Contact, viewModel: Block_contact_state) {

    var showMenu by remember { mutableStateOf(false) }

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
                Text(blockedContact.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Đã bị chặn", fontSize = 14.sp, color = Color.Gray)
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
                        text = { Text("Bỏ chặn") },
                        onClick = {
//                            viewModel.unblockContact(context, blockedContact.contact_id)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

