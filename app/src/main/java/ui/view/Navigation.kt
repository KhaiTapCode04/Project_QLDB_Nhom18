package ui.view


import ContactDetailScreen
import ContactListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state

class Navigation: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val testViewModel: User_state = viewModel()
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
fun TodoNavigation(viewModel: User_state) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    val testViewModel: User_state = viewModel()
    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            if(user.getUserId() == 0){
            UserScreen(
                navController = navController,
                viewModel = testViewModel
            )}
            else{
                viewModel.updateUser(user.getUserId(),user.getUserName(),user.getUserEmail(),user.getUserPhone(),user.getProfilePicture())
                navController.navigate("a")
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
                ContactListScreen(navController = navController, viewModel = testViewModel, context)
        }
        composable("email"){
            a(navController = navController, viewModel = testViewModel, context)
        }

        composable("ContactDetail/{contactJson}",arguments = listOf(navArgument("contactJson") { type = NavType.StringType })){backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)
            ContactDetailScreen(navController = navController,contact)
        }
    }
}