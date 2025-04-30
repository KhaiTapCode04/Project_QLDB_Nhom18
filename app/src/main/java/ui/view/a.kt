package ui.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import contactList
import data.model.Contact
import email
import get_contact
import ui.viewmodel.contact.ConactPreferencesManage
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.EmailPreferencesManage
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.users.User_state
import androidx.compose.foundation.lazy.items

var emailList: List<email> = listOf()
suspend fun get_email(viewModel: User_state, context: Context): List<email> {
    if(EmailPreferencesManage(context).getEmailList().isEmpty()){
        val user_id = UserPreferencesManager(context).getUserId()
        val email = Contact_service().get_email(user_id)
        email.forEach {
            EmailPreferencesManage(context).saveEmailList(it)
        }
    }
    return EmailPreferencesManage(context).getEmailList()
}

@Composable
fun a(navController: NavHostController, viewModel: User_state, context: Context){
    LaunchedEffect(Unit) {
        emailList = get_email(viewModel,context)
    }
    LazyColumn {
        items(emailList) { email ->
            Text(text = email.email_id.toString())
            Log.d(email.email_id.toString(), email.email_address)
        }
    }
}