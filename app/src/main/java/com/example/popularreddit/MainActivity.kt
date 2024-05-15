package com.example.popularreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popularreddit.ui.screens.MainScreen
import com.example.popularreddit.ui.screens.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                composable(route = Screen.Splash.route) {
                    SplashScreen(navController = navController)
                }

                composable(route = Screen.Main.route) {
                    MainScreen()
                }
            }
        }
    }
}

