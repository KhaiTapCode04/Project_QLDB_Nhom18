package ui.view


import ContactListScreen
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
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.test

class Navigation: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val testViewModel: test = viewModel()
            TodoNavigation(viewModel = testViewModel)
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
fun TodoNavigation(viewModel: test) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    val testViewModel: test = viewModel()
    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    NavHost(
        navController = navController,
        startDestination = "a"
    ) {
        composable("login") {
            if(user.getUserId() == 0){
            UserScreen(
                navController = navController,
                viewModel = testViewModel
            )}
            else{
                viewModel.updateUser(user.getUserId(),user.getUserName(),user.getUserEmail(),user.getUserPhone(),user.getProfilePicture())
                navController.navigate("profile")
            }
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
        composable("a"){
            ContactListScreen(navController = navController)
        }
    }
}