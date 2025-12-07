package com.example.examen_api.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examen_api.data.model.User
import com.example.examen_api.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel // <--- 1. IMPORTA ESTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // <--- 2. AGREGA ESTA ETIQUETA AQUÍ
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _users.value = repository.getAllUsers()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar usuarios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createUser(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createUser(user)
                loadUsers()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(id: Int, user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateUser(id, user)
                loadUsers()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.deleteUser(id)
                if (success) {
                    loadUsers()
                    onSuccess()
                } else {
                    _errorMessage.value = "Error al eliminar usuario"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}