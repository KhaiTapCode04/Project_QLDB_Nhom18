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
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

import data.model.Contact
import kotlinx.coroutines.launch
import ui.viewmodel.contact.ContactViewModel
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Contact_state

@Composable
fun BlockedContactScreen(
    navController: NavHostController,
    context: Context,
    Block_contact_state: Block_contact_state,
    Contact_state: Contact_state
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
                title = { Text("Danh sách chặn", style = TextStyle(color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xB587FF95)),
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại") } },
                actions = { IconButton(onClick = { navController.navigate("profile") }) { Icon(Icons.Default.Person, "Hồ sơ", Modifier.size(48.dp)) } }
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
                        Text("Không có liên hệ nào bị chặn",fontSize = 20.sp, color = Color.Gray)
                    }
                }

                items(blockedContacts) { blockedContact ->
                    BlockedContactCard(context, blockedContact,Block_contact_state, Contact_state)
                }

            }
        }
    }
}

@Composable
fun BlockedContactCard(context: Context,Contact: Contact, Block_contact_state: Block_contact_state, Contact_state: Contact_state) {
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
                Text(Contact.name, fontSize = 19.sp, fontWeight = FontWeight.Medium)
                Text("Đã bị chặn", fontSize = 16.sp, color = Color.Red)
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
                            Text(text = "Bỏ chặn",)
                        },
                        onClick = {
                            scope.launch {
                                ContactViewModel().unBlockContact(
                                    Contact.contact_id,
                                    Block_contact_state,
                                    Contact_state,
                                    context
                                )
                            }
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = "Bỏ chặn",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                }


            }
        }
    }
}

