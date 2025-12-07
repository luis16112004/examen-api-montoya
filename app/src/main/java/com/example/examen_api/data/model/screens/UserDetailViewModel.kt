package com.example.examen_api.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examen_api.data.model.User
import com.example.examen_api.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel // <--- 1. AGREGA ESTE IMPORT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // <--- 2. AGREGA ESTA ETIQUETA OBLIGATORIA
class UserDetailViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _user.value = repository.getUserById(id)
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar usuario: ${e.message}"
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