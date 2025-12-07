package com.example.examen_api.data.repository

import com.example.examen_api.data.model.User
import com.example.examen_api.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllUsers(): List<User> {
        val response = apiService.getUsers()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string() ?: "Sin detalles"
            throw Exception("Error al obtener usuarios: ${response.code()} - ${response.message()}\n$errorBody")
        }
    }

    suspend fun getUserById(id: Int): User {
        val response = apiService.getUserById(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al obtener usuario: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun createUser(user: User): User {
        val response = apiService.createUser(user)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string() ?: "Error desconocido"
            throw Exception("Error al crear usuario: ${response.code()} - $errorBody")
        }
    }

    suspend fun updateUser(id: Int, user: User): User {
        val response = apiService.updateUser(id, user)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string() ?: "Error desconocido"
            throw Exception("Error al actualizar usuario: ${response.code()} - $errorBody")
        }
    }

    suspend fun deleteUser(id: Int): Boolean {
        return try {
            val response = apiService.deleteUser(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}