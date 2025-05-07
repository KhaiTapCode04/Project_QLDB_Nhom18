import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import data.model.Contact
import ui.view.Navigation
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state


@Composable
fun ContactDetailScreen(navController: NavHostController, contact: Contact, Email_state: Email_state,Phone_state: Phone_state, context: Context) {
    LaunchedEffect(Unit) {
        Phone_state.get_phone(context)
        Email_state.get_email(context)
        Email_state.getGroup()
    }
    val groups by Email_state.groups.collectAsState()
    val phones by Phone_state.phones.collectAsState()
    val emails by Email_state.emails.collectAsState()

    val emailFilter = emails.filter { it.contact_id == contact.contact_id }
    val phoneFilter = phones.filter { it.contact_id == contact.contact_id }

    val groupName by remember(groups) {
        derivedStateOf {
            Email_state.getGroupnameById(contact.group_id) ?: ""
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi Tiết Liên Hệ") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("email") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Hồ sơ"
                        )
                    }
                }
            )
        },
        bottomBar = {BottomNavigationBar()}

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Name
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Action Buttons
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionButton(
                    text = "Gọi",
                    icon = Icons.Default.Person,
                    onClick = { /* Call action */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                QuickActionButton(
                    text = "Tin nhắn",
                    icon = Icons.Default.Person,
                    onClick = { /* Message action */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                QuickActionButton(
                    text = "Video",
                    icon = Icons.Default.Person,
                    onClick = { /* Video call action */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .heightIn(min = 100.dp, max = 300.dp),
                ) {
                    item {
                        ContactDetailRow("Điện thoại") {
                            Column {
                                phoneFilter.forEach { phone ->
                                    Text(text = phone.phone_number)
                                }
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ContactDetailRow("Email") {
                            Column {
                                emailFilter.forEach { email ->
                                    Text(text = email.email_address)
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ContactDetailRow("Địa chỉ", contact.contact_id.toString() ?: "Không có")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ContactDetailRow("Nhóm: ", groupName )
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Block action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Chặn")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* Edit action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff4eaf4e)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Chỉnh sửa")
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    icon: Any, // Use Any to allow flexibility with icon type
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xff4eaf4e), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon as androidx.compose.ui.graphics.vector.ImageVector,
                contentDescription = text,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ContactDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun ContactDetailRow(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "$label:", style = MaterialTheme.typography.labelMedium)
        content()
    }
}
