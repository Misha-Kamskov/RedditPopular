package com.example.popularreddit.ui.screens.splash

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.popularreddit.R
import com.example.popularreddit.models.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val orientation = LocalConfiguration.current.orientation
    var logoVisibility by rememberSaveable { mutableStateOf(false) }

    val navigateToMain: () -> Unit = {
        navController.navigate(Screen.Main.route) { popUpTo(0) }
    }

    LaunchedEffect(Unit) {
        if (!logoVisibility) {
            delay(1500L)
            logoVisibility = true
            delay(1500L)
            navigateToMain()
        } else {
            delay(100L)
            navigateToMain()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5D8BFF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = logoVisibility,
            enter = fadeIn(tween(1000, easing = EaseInBounce)) + scaleIn(
                tween(
                    1000,
                    easing = EaseInBounce
                )
            )
        ) {
            val imageModifier = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Modifier
                    .fillMaxWidth(0.5f)
                    .wrapContentHeight()
            } else {
                Modifier
                    .fillMaxHeight(0.5f)
                    .wrapContentWidth()
            }
            Image(
                modifier = imageModifier,
                painter = painterResource(id = R.drawable.reddit_bar_logo),
                contentDescription = null
            )
        }
    }

}