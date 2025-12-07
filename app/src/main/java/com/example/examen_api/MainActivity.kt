package com.example.examen_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen_api.ui.screens.UserCreateScreen
import com.example.examen_api.ui.screens.UserDetailScreen
import com.example.examen_api.ui.screens.UserEditScreen
import com.example.examen_api.ui.screens.UserListScreen
import com.example.examen_api.ui.theme.ExamenapiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamenapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            UserListScreen(navController)
                        }

                        composable("create") {
                            UserCreateScreen(navController)
                        }

                        // PASA String (sin convertir a Int)
                        composable("detail/{userId}") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            UserDetailScreen(
                                navController = navController,
                                userId = userId  // String
                            )
                        }

                        // PASA String (sin convertir a Int)
                        composable("edit/{userId}") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            UserEditScreen(
                                navController = navController,
                                userId = userId  // String
                            )
                        }
                    }
                }
            }
        }
    }
}