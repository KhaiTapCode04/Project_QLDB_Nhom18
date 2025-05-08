package ui.view.components
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {
                navController.navigate("homedb")
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
            label = { Text("Danh bạ") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("groupscreen")
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
            label = { Text("Nhóm") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("setting")
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
            label = { Text("Cài đặt") }
        )
    }
}
