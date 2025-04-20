package com.example.nhom18_lttbdd_qldb_ngaybc.activities

import ProfileScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nhom18_lttbdd_qldb_ngaybc.EditProfileScreen
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.AddContactViewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.MainViewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.SplashViewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.LoginViewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels.RegisterViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()
    val addContactViewModel: AddContactViewModel = viewModel()
    val splashViewModel: SplashViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewmodel: RegisterViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel
            )
        }
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = registerViewmodel

            )
        }
        composable("main") {
            MainScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable("add_contact") {
            AddContactScreen(
                viewModel = addContactViewModel,
                mainViewModel = mainViewModel,
                onSaveSuccess = {
                    navController.popBackStack() // Quay về main khi lưu thành công
                },
                onCancelClicked = {
                    navController.popBackStack() // Quay về main nếu hủy
                }
            )
        }

        composable("profile") {
            ProfileScreen(navController)
        }

        composable("edit_profile") {
            EditProfileScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }


    }
}
