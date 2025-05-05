package ui.view

import AddGroupEvent
import AddGroupState
import AddGroupViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddGroupScreen(
    navController: NavHostController,
    viewModel: AddGroupViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Local state để điều khiển reset text
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AddGroupEvent.Success -> {
                    snackbarHostState.showSnackbar(event.message)

                    // Clear TextField
                    nameText = ""
                    descriptionText = ""

                    // Quay lại màn trước
                    navController.popBackStack()
                }
                is AddGroupEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo Nhóm Mới") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nameText,
                onValueChange = {
                    nameText = it
                    viewModel.onNameChanged(it)
                },
                label = { Text("Tên nhóm") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descriptionText,
                onValueChange = {
                    descriptionText = it
                    viewModel.onDescriptionChanged(it)
                },
                label = { Text("Mô tả") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.addGroup(context)
                },
                enabled = nameText.isNotBlank() && descriptionText.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tạo Group")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text("Quay lại")
            }

            if (uiState is AddGroupState.Loading) {
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}
