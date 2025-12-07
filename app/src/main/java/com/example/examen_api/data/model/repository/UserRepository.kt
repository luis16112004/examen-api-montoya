package com.example.examen_api.data.repository

import com.example.examen_api.data.model.User
import com.example.examen_api.data.network.RetrofitClient
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val apiService = RetrofitClient.instance

    suspend fun getAllUsers(): List<User> = apiService.getUsers()

    suspend fun getUserById(id: Int): User = apiService.getUserById(id)

    suspend fun createUser(user: User): User = apiService.createUser(user)

    suspend fun updateUser(id: Int, user: User): User = apiService.updateUser(id, user)

    suspend fun deleteUser(id: Int): Boolean {
        return try {
            apiService.deleteUser(id).isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}