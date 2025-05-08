package ui.view.components
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, selected: String) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == "homedb",
            onClick = {
                if (selected != "homedb") {
                    navController.navigate("homedb")
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
            label = { Text("Danh bạ") }
        )
        NavigationBarItem(
            selected = selected == "groupscreen",
            onClick = {
                if (selected != "groupscreen") {
                    navController.navigate("groupscreen")
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
            label = { Text("Nhóm") }
        )
        NavigationBarItem(
            selected = selected == "setting",
            onClick = {
                if (selected != "setting") {
                    navController.navigate("setting")
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
            label = { Text("Cài đặt") }
        )
    }
}

