package com.example.examen_api.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.examen_api.data.model.User
import com.example.examen_api.ui.viewmodel.UserDetailViewModel
import com.example.examen_api.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditScreen(
    userId: String, // Viene como String desde NavHost
    navController: NavHostController,
    detailViewModel: UserDetailViewModel = hiltViewModel(),
    viewModel: UserViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    val user by detailViewModel.user.collectAsState()
    val isLoading by detailViewModel.isLoading.collectAsState() // detailViewModel loading state
    val isSaving by viewModel.isLoading.collectAsState() // viewModel (saving) loading state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(userId) {
        // CORRECCIÓN: Convertir a Int para cargar
        val idAsInt = userId.toIntOrNull() ?: 0
        detailViewModel.loadUser(idAsInt)
    }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            phone = it.phone
            email = it.email
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (user == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Usuario no encontrado")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigateUp() }) {
                        Text("Volver")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                        },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError,
                        singleLine = true
                    )
                    if (nameError) {
                        Text(
                            text = "El nombre es requerido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            phoneError = false
                        },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        isError = phoneError,
                        singleLine = true
                    )
                    if (phoneError) {
                        Text(
                            text = "El teléfono es requerido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        isError = emailError,
                        singleLine = true
                    )
                    if (emailError) {
                        Text(
                            text = "El email es requerido y debe ser válido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                // Validaciones
                                var hasError = false

                                if (name.isBlank()) {
                                    nameError = true
                                    hasError = true
                                }

                                // Phone es opcional, no validamos si está vacío

                                if (email.isBlank() || !email.contains("@")) {
                                    emailError = true
                                    hasError = true
                                }

                                if (!hasError) {
                                    val updatedUser = User(
                                        id = user!!.id,
                                        name = name,
                                        phone = phone.ifBlank { null },
                                        email = email
                                    )
                                    // CORRECCIÓN: Convertir ID String a Int para el update
                                    val idAsInt = userId.toIntOrNull() ?: 0
                                    viewModel.updateUser(idAsInt, updatedUser) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Usuario actualizado exitosamente")
                                            navController.navigateUp()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Guardando...")
                            } else {
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}