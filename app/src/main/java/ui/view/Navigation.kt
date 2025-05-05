package ui.view


import AddGroupViewModel
import ContactDetailScreen
import ContactListScreen
import android.content.Context
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
import ui.viewmodel.contact.AddContactViewModel
import ui.viewmodel.contact.ConactPreferencesManage
import ui.viewmodel.contact.Constact_state
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.EmailPreferencesManage
import ui.viewmodel.contact.Email_state
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
    val UserViewModel: User_state = viewModel()
    val ContactViewModel: Constact_state = viewModel()
    val EmailViewModel: Email_state = viewModel()

    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {

            UserScreen(
                navController = navController,
                userViewModel = UserViewModel,
                contactViewModel = ContactViewModel,
                emailViewModel = EmailViewModel
            )

        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = UserViewModel
            )
        }
        composable("EditProfile") {
            EditProfile(
                navController = navController,
                viewModel = UserViewModel
            )
        }
        composable("homedb"){
            ContactListScreen(navController = navController, viewModel = ContactViewModel, context)
        }
        composable("email"){
            itemEmail(navController = navController, viewModel = EmailViewModel, context)
        }

        composable("ContactDetail/{contactJson}",arguments = listOf(navArgument("contactJson") { type = NavType.StringType })){backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)
            ContactDetailScreen(navController = navController,contact)
        }

        composable("addcontact") {
            AddContactScreen(
                navController = navController,
                viewModel = AddContactViewModel()
            )
        }

        composable("addgroupscreen") {
            AddGroupScreen(
                navController = navController,
                viewModel = AddGroupViewModel()
            )
        }

        composable("groupscreen") {
            GroupScreen(
                navController = navController
            )
        }

    }
}