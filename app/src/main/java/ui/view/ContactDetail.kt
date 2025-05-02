import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.model.Contact


@Composable
fun ContactDetailScreen(
    navController: NavHostController,
    contact: Contact
) {
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
        }
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
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ContactDetailRow("Điện thoại", contact.name)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ContactDetailRow("Email", contact.contact_id.toString())
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ContactDetailRow("Địa chỉ", contact.group_id.toString())
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ContactDetailRow("Nhóm", contact.group_id.toString())
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
