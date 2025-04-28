package ui.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.users.test

class Navigation: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoNavigation()
        }
}
}

class SharedViewModel : ViewModel() {
    private val _sharedData = MutableStateFlow<String?>(null)
    val sharedData: StateFlow<String?> = _sharedData.asStateFlow()
    fun updateData(data: String) {
        _sharedData.value = data
    }
}
@Composable
fun TodoNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    val testViewModel: test = viewModel()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            UserScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = testViewModel
            )
        }
        composable("EditProfile") {
            EditProfile(
                navController = navController,
                viewModel = testViewModel
            )
        }
    }
}