package com.example.nhom18_lttbdd_qldb_ngaybc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.nhom18_lttbdd_qldb_ngaybc.activities.AppNavGraph
import com.example.nhom18_lttbdd_qldb_ngaybc.activities.LoginScreen
//import com.example.nhom18_lttbdd_qldb_ngaybc.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}
