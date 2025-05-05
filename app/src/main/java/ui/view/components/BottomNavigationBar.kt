package ui.view.components
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {  },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
            label = { Text("Danh bạ") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
            label = { Text("Nhóm") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
            label = { Text("Cài đặt") }
        )
    }
}