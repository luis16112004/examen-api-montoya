package com.example.examen_api.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.examen_api.ui.viewmodel.UserViewModel // Asegúrate de importar el ViewModel real si obtienes datos de ahí

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavHostController,
    // Si tienes un ViewModel para listar usuarios, inyéctalo aquí:
    // viewModel: UserViewModel = hiltViewModel()
) {
    // Si estás usando datos reales del ViewModel, reemplaza esto.
    // Si usas datos mockeados para probar la UI, asegúrate que los IDs sean números en String
    val users = listOf(
        UserItem("1", "Usuario 1"),
        UserItem("2", "Usuario 2"),
        UserItem("3", "Usuario 3")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Usuarios") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear usuario")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("detail/${user.id}")
                    }
                ) {
                    ListItem(
                        headlineContent = { Text(user.name) },
                        supportingContent = { Text("ID: ${user.id}") },
                        trailingContent = {
                            IconButton(
                                onClick = {
                                    navController.navigate("edit/${user.id}")
                                }
                            ) {
                                Text("Editar")
                            }
                        }
                    )
                }
            }
        }
    }
}

data class UserItem(
    val id: String,
    val name: String
)