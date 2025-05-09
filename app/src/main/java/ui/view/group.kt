package ui.view

import ContactListItem
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Contact_state

@Composable
fun GroupScreen(navController: NavHostController, Contact_state: Contact_state,Block_contact_state: Block_contact_state, context: Context) {

    // Load dữ liệu khi vào màn
    LaunchedEffect(Unit) {
        Contact_state.get_contact(context)
        Contact_state.getGroup()
    }

    // Nhận dữ liệu từ ViewModel
    val groups by  Contact_state.groups.collectAsState()
    val contacts by  Contact_state.contacts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nhóm liên hệ",
                        style = TextStyle(
                            color = Color(0xFF000000),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xB587FF95)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Hồ sơ",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },


    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            // Với mỗi group → hiển thị nhóm + các liên hệ
            groups.forEach { groupItem ->

                val groupContacts = contacts.filter { it.group_id == groupItem.group_id }

                if (groupContacts.isNotEmpty()) {

                    // Nhóm (Title)
                    item {
                        Text(
                            text = "Nhóm: ${groupItem.group_name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE0E0E0))
                                .padding(12.dp)
                        )
                    }

                    // Các contact trong nhóm
                    items(groupContacts) { contact ->
                        Contact_state.getGroupnameById(contact.group_id)?.let { groupName ->
                            ContactListItem(
                                contact = contact,
                                navController = navController,
                                Contact_state =  Contact_state,
                                Block_contact_state= Block_contact_state,
                                Group_name = groupName
                            )
                        }
                    }
                }
            }
        }
    }
}
