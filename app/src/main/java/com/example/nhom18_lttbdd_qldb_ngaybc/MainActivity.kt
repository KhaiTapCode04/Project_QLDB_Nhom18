package com.example.nhom18_lttbdd_qldb_ngaybc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = ""
)

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "ID: ${user.id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun UserList(users: List<User>) {
    Column(modifier = Modifier.fillMaxSize()) {
        users.forEach { user ->
            UserCard(user = user)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var users by remember { mutableStateOf(emptyList<User>()) }

            LaunchedEffect(Unit) {
                firestore.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        val userList = result.documents.map { document ->
                            User(
                                id = document.getString("id") ?: "",
                                name = document.getString("name") ?: "",
                                email = document.getString("email") ?: ""
                            )
                        }
                        users = userList
                        Log.d("MainActivity", "Loaded users: $users")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("MainActivity", "Error loading users", exception)
                    }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Firebase User List") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) { contentPadding ->  // Nhận contentPadding từ Scaffold
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),  // Sử dụng contentPadding
                    contentAlignment = Alignment.Center
                ) {
                    if (users.isEmpty()) {
                        CircularProgressIndicator()
                    } else {
                        UserList(users = users)
                    }
                }
            }

        }
    }
}
