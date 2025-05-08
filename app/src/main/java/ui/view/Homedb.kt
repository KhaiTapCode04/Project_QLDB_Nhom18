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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.navigation.NavHostController
import com.google.gson.Gson
import data.model.Contact
import kotlinx.coroutines.launch
import ui.view.Navigation
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.ContactViewModel
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.users.UserPreferencesManager
import com.google.accompanist.swiperefresh.*
import kotlinx.coroutines.delay
import ui.view.reload_email
import ui.viewmodel.contact.sharedPreferences.PhonePreferencesManage
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state

suspend fun reload_contact(viewModel: Contact_state, context: Context) {
    val user_id = UserPreferencesManager(context).getUserId()
    val contact = Contact_service().get_contact(user_id)
    viewModel.clearAll()
    ConactPreferencesManage(context).clearContacts()
    contact.forEach {
        ConactPreferencesManage(context).saveOrUpdateContact(it)
        viewModel.addOrUpdateContact(it)
    }
}
suspend fun reload_phone(Phone_state: Phone_state, context: Context) {
    val user_id = UserPreferencesManager(context).getUserId()
    val phone = Contact_service().get_phone(user_id)
    Phone_state.clearAllPhones()
    PhonePreferencesManage(context).clearPhones()
    phone.forEach {
        PhonePreferencesManage(context).savePhoneList(it)
        Phone_state.addOrUpdatePhone(it)
    }
}

@Composable
fun ContactListScreen(navController: NavHostController, Contact_state: Contact_state,Email_state: Email_state,Phone_state: Phone_state, context: Context) {
    LaunchedEffect(Unit) {
        Contact_state.get_contact(context)
        Contact_state.getGroup()
    }
    val groups by Contact_state.groups.collectAsState()
    val scope = rememberCoroutineScope()
    val contactList by Contact_state.contacts.collectAsState()
    var search by remember { mutableStateOf("") }
    var isSortedAsc by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    val filteredContactList by remember(contactList, search, groups) {
        derivedStateOf {
            contactList.filter { it.name.contains(search, ignoreCase = true) }
        }
    }
    var sortedContactList by remember { mutableStateOf(filteredContactList) }
    LaunchedEffect(filteredContactList) {
        sortedContactList = filteredContactList
    }
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
                    IconButton(onClick = { /* Profile action */
                        navController.navigate("profile")
                    }) {
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


        bottomBar = { BottomNavigationBar(navController) },

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

            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = search,
                    onValueChange = { search = it },
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
                IconButton(onClick = {
                    isSortedAsc = !isSortedAsc
                    sortedContactList = if (isSortedAsc) {
                        sortedContactList.sortedBy { it.name }
                    } else {
                        sortedContactList.sortedByDescending { it.name }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    scope.launch {
                        reload_contact(Contact_state, context)
                        reload_email(Email_state,context)
                        reload_phone(Phone_state,context)

                        isRefreshing = false
                    }
                }
            ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(max(200.dp, 1000.dp))
            ) {
                items(sortedContactList) { contact ->
                    Contact_state.getGroupnameById(contact.group_id)
                        ?.let { ContactListItem(contact, navController, Contact_state, it) }
                }
            }
        }
        }
    }
}

@Composable
fun ContactListItem(
    contact: Contact,
    navController: NavHostController,
    Contact_state: Contact_state,
    Group_name: String
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
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
                text = contact.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
//            Text(
//                text = contact.name,
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Gray
//            )
            Text(
                text = "Nhóm: ${Group_name}",
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
                        val gson = Gson()
                        val contactJson = Uri.encode(gson.toJson(contact))
                        navController.navigate("contactDetail/$contactJson")
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
                        navController.navigate("edit/${contact.contact_id}")
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
                        scope.launch {

                            ContactViewModel().deleteContact(
                                contact_id = contact.contact_id,
                                Contact_state,
                                context
                            )
                        }
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

