package ui.view.components
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route


    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "homedb",
            onClick = { navController.navigate("homedb") },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Danh bạ") },
            label = { Text("Danh bạ") }
        )
        NavigationBarItem(
            selected = currentRoute == "groupscreen",
            onClick = {
                navController.navigate("groupscreen")
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Nhóm") },
            label = { Text("Nhóm") }
        )
        NavigationBarItem(
            selected = currentRoute == "setting",
            onClick = { navController.navigate("setting") },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Cài đặt") },
            label = { Text("Cài đặt") }
        )
    }
}
