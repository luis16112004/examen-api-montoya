package com.example.examen_api.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Para emulador Android: usar 10.0.2.2:8000
    // Para dispositivo físico: usar la IP de tu máquina (ej: 192.168.1.70:8000)
    // Para localhost en desarrollo: usar 127.0.0.1:8000
    private const val BASE_URL = "http://192.168.1.70:8000/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}