package ui.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import email
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.EmailPreferencesManage
import ui.viewmodel.users.UserPreferencesManager
import ui.viewmodel.contact.Email_state
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

var emailList: List<email> = listOf()

suspend fun reload_email(viewModel: Email_state, context: Context){
        val user_id = UserPreferencesManager(context).getUserId()
        val email = Contact_service().get_email(user_id)
        email.forEach {
            EmailPreferencesManage(context).saveEmailList(it)
            viewModel.addOrUpdateEmail(it)
        }
}

@Composable
fun a(navController: NavHostController, viewModel: Email_state, context: Context){
    LaunchedEffect(Unit) {
        Navigation().get_email(viewModel,context)
    }
    val emails by viewModel.emails.collectAsState()

    LazyColumn {
        items(emails) { email ->
            Text(text = email.email_address)
            Log.d("Email", email.email_address)
        }
    }
}