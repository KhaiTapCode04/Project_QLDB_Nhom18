package ui.view
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

class dowloadImg : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Example URL for a profile image - replace with your actual URL
        val imageUrl = "https://nettruyen.world/uploads/680d65158c455.jpg"

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen(imageUrl)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(imageUrl: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Using SubcomposeAsyncImage for more control over loading states
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    // Show a circular progress indicator while loading
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    // Show the image when successfully loaded
                    SubcomposeAsyncImageContent()
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Image loaded successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    // Show an error icon when loading fails
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "Error",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    // Default placeholder
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Placeholder",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Load Image Button
        Button(
            onClick = {
                Toast.makeText(context, "Reloading image...", Toast.LENGTH_SHORT).show()
                // Force recomposition to reload the image
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Reload Image")
        }
    }
}