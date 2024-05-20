package com.example.popularreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popularreddit.ui.Screen
import com.example.popularreddit.ui.screens.main.MainScreen
import com.example.popularreddit.ui.screens.main.MainViewModel
import com.example.popularreddit.ui.screens.splash.SplashScreen
import com.example.models.models.appsettings.model.AppSettingsPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RedditActivity : ComponentActivity() {

    @Inject
    lateinit var settingsStore: AppSettingsPrefs


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
                    val mainViewModel: MainViewModel = hiltViewModel()
                    MainScreen(mainViewModel = mainViewModel, settingsStore = settingsStore)
                }
            }
        }
    }
}

