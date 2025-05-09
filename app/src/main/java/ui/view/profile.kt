package ui.view
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import data.model.BlockedContact
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.sharedPreferences.BlockConactPreferencesManage
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.sharedPreferences.PhonePreferencesManage
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Phone_state
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state

@Composable
fun ProfileScreen(navController: NavHostController, User_state: User_state, Contact_state:Contact_state,Block_contact_state: Block_contact_state, Email_state: Email_state, Phone_state: Phone_state) {
    val context = LocalContext.current
    val user_id by User_state.user_id.collectAsState()
    val name by User_state.userName.collectAsState()
    val email by User_state.email.collectAsState()
    val phone by User_state.phone.collectAsState()
    val profileImage by User_state.profile_picturel.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hồ sơ người dùng") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Email
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Divider()

                    // Phone
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Số điện thoại",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = phone ?: "",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Button(
                onClick = {navController.navigate("EditProfile")},
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chỉnh sửa hồ sơ")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    val user = UserPreferencesManager(context)
                    val contact = ConactPreferencesManage(context)
                    val blockContact = BlockConactPreferencesManage(context)
                    val email = EmailPreferencesManage(context)
                    val phone = PhonePreferencesManage(context)
                    user.clearUserInfo()
                    contact.clearContacts()
                    blockContact.clearBlockContacts()
                    email.clearEmails()
                    phone.clearPhones()
                    User_state.clearUser()
                    Contact_state.clearAll()
                    Block_contact_state.clearAll()
                    Email_state.clearAllEmails()
                    Phone_state.clearAllPhones()

                    navController.navigate("login"){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng xuất")
            }
        }
    }
}