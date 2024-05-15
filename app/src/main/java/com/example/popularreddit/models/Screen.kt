package com.example.popularreddit.models

sealed class Screen(val route: String) {
    data object Splash : Screen("splash_screen")
    data object Main : Screen("main_screen")
}