package com.example.examen_api.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int? = null,
    val name: String,
    val phone: String? = null,
    val email: String,
    val password: String? = null, // Solo para crear/actualizar, no se muestra
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)