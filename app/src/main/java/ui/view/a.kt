package ui.view
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ui.viewmodel.users.test

@Composable
fun UserProfileScreen(navController: NavHostController, viewModel: test) {

    val userId by viewModel.user_id.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val email by viewModel.email.collectAsState()
    val profilePicture by viewModel.profile_picturel.collectAsState()

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Phần avatar
            Card(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(120.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (profilePicture.isNotEmpty()) {
                        // Sử dụng Coil để tải ảnh từ URL hoặc đường dẫn cục bộ
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(profilePicture)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Ảnh đại diện",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Hiển thị avatar mặc định nếu không có ảnh
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Avatar mặc định",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Tên người dùng
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // ID người dùng
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "ID",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ID: ${userId ?: "Chưa đăng nhập"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Email
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Nút chỉnh sửa hồ sơ
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Chỉnh sửa hồ sơ")
            }
        }
    }
}

@Composable
fun UserProfileCard(viewModel: test = viewModel()) {
    // Thu thập trạng thái từ ViewModel
    val userName by viewModel.userName.collectAsState()
    val email by viewModel.email.collectAsState()
    val profilePicture by viewModel.profile_picturel.collectAsState()

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            if (profilePicture.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profilePicture)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Ảnh đại diện",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Avatar mặc định",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin người dùng
            Column {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}