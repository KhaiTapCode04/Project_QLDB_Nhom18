import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.model.Contact
import ui.view.components.BottomNavigationBar
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state

@Composable
fun ContactDetailScreen(
    navController: NavHostController,
    contact: Contact,
    Email_state: Email_state,
    Phone_state: Phone_state,
    context: Context
) {
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
                title = {
                    Text(
                        text = "Chi tiết liên hệ",
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
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB0BEC5)), // Xám xanh nhạt
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Name
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionButton(
                    text = "Gọi",
                    icon = Icons.Default.Call,
                    backgroundColor = Color(0xFF4CAF50), // Xanh lá
                    contentColor = Color.White,
                    onClick = { /* Call action */ }
                )
                QuickActionButton(
                    text = "Tin nhắn",
                    icon = Icons.Default.Message,
                    backgroundColor = Color(0xFF2196F3), // Xanh dương
                    contentColor = Color.White,
                    onClick = { /* Message action */ }
                )
                QuickActionButton(
                    text = "Video",
                    icon = Icons.Default.Videocam,
                    backgroundColor = Color(0xFFE91E63), // Hồng
                    contentColor = Color.White,
                    onClick = { /* Video call action */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F7FA) // Xám trắng nhạt
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // Phone
                        ContactDetailRow(
                            icon = Icons.Default.Phone,
                            label = "Số điện thoại",
                            content = {
                                phoneFilter.forEach { phone ->
                                    Text(
                                        text = phone.phone_number,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                        modifier = Modifier.padding(top = 4.dp),
                                        color = Color.Black
                                    )
                                }
                            }
                        )

                        Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

                        // Email
                        ContactDetailRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            content = {
                                emailFilter.forEach { email ->
                                    Text(
                                        text = email.email_address,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                        modifier = Modifier.padding(top = 4.dp),
                                        color = Color.Black
                                    )
                                }
                            }
                        )

                        Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

                        // Address
                        ContactDetailRow(
                            icon = Icons.Default.Home,
                            label = "Địa chỉ",
                            content = {
                                Text(
                                    text = contact.contact_id.toString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = Color.Black
                                )
                            }
                        )

                        Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

                        // Group
                        ContactDetailRow(
                            icon = Icons.Default.Group,
                            label = "Nhóm",
                            content = {
                                Text(
                                    text = groupName,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = Color.Black
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Block action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350) // Đỏ nhạt
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Chặn",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { /* Edit action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50) // Xanh lá
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(backgroundColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = Color.Black
        )
    }
}

@Composable
fun ContactDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF616161),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            )
            content()
        }
    }
}