import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nhom18_lttbdd_qldb_ngaybc.R
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
    if(!contact.isEmpty()){
        Log.d("asddf","hehe")
    }else{
        Log.d("asddf","hihi"+user_id)
    }
    contact.forEach {
        Log.d("asddf",it.name)
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
                title = {
                    Text(
                        text = "Quản lý danh bạ",
                        style = TextStyle(
                            color = Color(0xFF000000), // Màu xanh đậm #339900 (theo yêu cầu)
                            fontSize = 32.sp, // Chữ to hơn
                            fontWeight = FontWeight.Bold // Chữ đậm hơn
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xB587FF95)
                ),
                /*navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },*/
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    navController.navigate("profile")
                                }
                        )
                    }
                }
            )
        },

        bottomBar = { BottomNavigationBar(navController) },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addcontact")
                },
                containerColor = Color(0xFF1D4BCD)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "",
                    tint = Color.White
                )
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
                    placeholder = {
                        Text(
                            text = "Tìm kiếm liên hệ",
                            color = Color.Gray.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .weight(2f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            tint = Color.Gray.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)

                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
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

                    scope.launch {
                        isRefreshing = true
                        reload_contact(Contact_state, context)
                        reload_email(Email_state,context)
                        reload_phone(Phone_state,context)
                        isRefreshing = false
                        Toast.makeText(context,"done", Toast.LENGTH_SHORT).show()
                        sortedContactList.forEach {
                            Log.d("check", it.name)

                        }
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 1000.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    items(sortedContactList) { contact ->
                        Contact_state.getGroupnameById(contact.group_id)?.let { groupName ->
                            ContactListItem(contact, navController, Contact_state, groupName)
                        }
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
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            )
           /* Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )*/
            Text(
                text = "Nhóm: ${Group_name}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_view),
                            contentDescription = "Xem chi tiết",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Chỉnh sửa") },
                    onClick = {
                        navController.navigate("edit/${contact.contact_id}")
                        showDropdownMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Chỉnh sửa",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified

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
                            painter = painterResource(id = R.drawable.ic_block),
                            contentDescription = "Chặn",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
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
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Xóa",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified

                        )
                    }
                )
            }
        }
    }
}

