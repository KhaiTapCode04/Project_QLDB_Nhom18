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
import ui.viewmodel.contact.ConactPreferencesManage

import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.EmailPreferencesManage
import ui.viewmodel.contact.Email_state
import ui.viewmodel.contact.PhonePreferencesManage
import ui.viewmodel.contact.Phone_state
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state

import ui.viewmodel.contact.Contact_state

class Navigation: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val testViewModel: User_state = viewModel()
            TodoNavigation(viewModel = testViewModel)
        }
    }
    suspend fun get_contact(viewModel: Contact_state, context: Context) {
        if(ConactPreferencesManage(context).getContactList().isEmpty()) {
            val user_id = UserPreferencesManager(context).getUserId()
            val contact = Contact_service().get_contact(user_id)
            contact.forEach {
                ConactPreferencesManage(context).saveOrUpdateContact(it)
                viewModel.addOrUpdateContact(it)
            }
        }
    }

    suspend fun get_email(viewModel: Email_state, context: Context) {
        if(EmailPreferencesManage(context).getEmailList().isEmpty()){
            val user_id = UserPreferencesManager(context).getUserId()
            val email = Contact_service().get_email(user_id)
            email.forEach {
                EmailPreferencesManage(context).saveEmailList(it)
                viewModel.addOrUpdateEmail(it)

            }
        }else{
            EmailPreferencesManage(context).getEmailList().forEach {
                viewModel.addOrUpdateEmail(it)
            }
        }
    }

    suspend fun get_phone(viewModel: Phone_state, context: Context) {
        if(PhonePreferencesManage(context).getPhoneList().isEmpty()){
            val user_id = UserPreferencesManager(context).getUserId()
            val phone = Contact_service().get_phone(user_id)
            phone.forEach {
                PhonePreferencesManage(context).savePhoneList(it)
                viewModel.addOrUpdatePhone(it)

            }
        }else{
            PhonePreferencesManage(context).getPhoneList().forEach {
                viewModel.addOrUpdatePhone(it)
            }
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
    val ContactViewModel: Contact_state = viewModel()
    val EmailViewModel: Email_state = viewModel()
    val PhoneViewModel: Phone_state = viewModel()

    val context = LocalContext.current
    val user = UserPreferencesManager(context)
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {

//            UserScreen(
//                navController = navController,
//                userViewModel = UserViewModel,
//                contactViewModel = ContactViewModel,
//                emailViewModel = EmailViewModel,
//            )

            AddContact2(navController)
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                userViewModel = UserViewModel,
                contactViewModel = ContactViewModel,
                emailViewModel = EmailViewModel,
                phoneViewModel = PhoneViewModel
            )
        }
        composable("EditProfile") {
            EditProfile(
                navController = navController,
                viewModel = UserViewModel
            )
        }
        composable("a"){
            ContactListScreen(navController = navController, viewModel = ContactViewModel, context)
        }
//        composable("email"){
//            a(navController = navController, viewModel = EmailViewModel, context)
//        }

        composable("ContactDetail/{contactJson}",arguments = listOf(navArgument("contactJson") { type = NavType.StringType })){backStackEntry ->
            val contactJson = backStackEntry.arguments?.getString("contactJson") ?: ""
            val contact = Gson().fromJson(contactJson, Contact::class.java)
            ContactDetailScreen(navController = navController,contact, emailViewModel = EmailViewModel, phoneViewModel = PhoneViewModel, context)
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