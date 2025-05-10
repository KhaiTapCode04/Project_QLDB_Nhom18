package ui.view


import ContactDetailScreen
import ContactListScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import com.google.gson.Gson
import data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage

import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.EditContactViewModel
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.sharedPreferences.PhonePreferencesManage
import ui.viewmodel.contact.state.AddContact_state
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Phone_state
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state

import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Delete_contact_state
import ui.viewmodel.groups.Group_state

class Navigation: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val testViewModel: User_state = viewModel()
            TodoNavigation(viewModel = testViewModel)
        }
    }




//favicon
    @Composable
    fun ContactIcon() {
        Image(
            painter = painterResource(id = R.drawable.ic_contacts),
            contentDescription = "Contact Icon",
            modifier = Modifier.size(48.dp)
        )
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
    val User_state: User_state = viewModel()
    val Group_state: Group_state = viewModel()
    val Contact_state: Contact_state = viewModel()
    val Email_state: Email_state = viewModel()
    val Phone_state: Phone_state = viewModel()
    val Delete_contact_state: Delete_contact_state = viewModel()
    val AddContact_state: AddContact_state = viewModel()
    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    val emailState: Email_state = viewModel()
    val phoneState: Phone_state = viewModel()
    val viewmodeleditcontact: EditContactViewModel = viewModel()
    val Block_contact_state : Block_contact_state = viewModel()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            UserScreen(
                navController = navController,
                User_state = User_state,
                Group_state =Group_state,
                Contact_state = Contact_state,
                Block_contact_state =Block_contact_state,
                Delete_contact_state =Delete_contact_state,
                Email_state = Email_state,
                Phone_state = Phone_state
            )
        }
        composable ("RegisterScreen"){
            RegisterScreen(navController=navController)
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                User_state = User_state,
                Contact_state = Contact_state,
                Block_contact_state = Block_contact_state,
                Email_state = Email_state,
                Phone_state = Phone_state
            )
        }
        composable("EditProfile") {
            EditProfile(
                navController = navController,
                User_state = User_state,
            )
        }
        composable("homedb"){
            ContactListScreen(navController = navController,Group_state=Group_state, Contact_state = Contact_state,Delete_contact_state= Delete_contact_state,Block_contact_state= Block_contact_state, Email_state = Email_state,Phone_state = Phone_state, context)

        }
//        composable("email"){
//            a(navController = navController, viewModel = EmailViewModel, context)
//        }

        composable("ContactDetail/{contactJson}",arguments = listOf(navArgument("contactJson") { type = NavType.StringType })){backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)
            ContactDetailScreen(navController = navController,contact,Contact_state= Contact_state, Block_contact_state=Block_contact_state, Email_state = Email_state, Phone_state = Phone_state, context)
        }

        composable("groupscreen") {
            GroupScreen(
                navController = navController,
                Contact_state= Contact_state,
                Delete_contact_state =Delete_contact_state,
                Block_contact_state=Block_contact_state,
                context = context
            )
        }

        composable("addcontact") {
            AddContact(
                navController = navController,
                AddContact_state = AddContact_state,
                Contact_state = Contact_state,
                Email_state = Email_state,Phone_state = Phone_state
            )
        }

        composable(
            route = "contactDetail/{contactJson}",
            arguments = listOf(navArgument("contactJson") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)

            ContactDetailScreen(navController, contact,Contact_state=Contact_state,
                Block_contact_state=Block_contact_state, emailState, phoneState, context)
        }

        composable("setting") {
            Setting(
                navController = navController,
            )
        }
        composable("edit/{contactJson}",
            arguments = listOf(navArgument("contactJson") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)

            EditContactScreen(
                navController = navController,
                context = context,
                contact = contact,
                AddContact_state = AddContact_state,
                viewModel = viewmodeleditcontact
            )
        }
        composable("blocklist") {
            BlockedContactScreen(
                navController= navController,
                context= context,
                Block_contact_state = Block_contact_state,
                Contact_state = Contact_state
            )
        }
        /*composable("trash") {
            TrashContactScreen(navController = navController)
        }*/
        composable("about") {
            AboutScreen(navController = navController)
        }
        composable("TrashContactScreen"){
            TrashContactScreen(
                navController = navController,
                context = context,
                Delete_contact_state = Delete_contact_state,
                Contact_state = Contact_state,

            )
        }


    }
}