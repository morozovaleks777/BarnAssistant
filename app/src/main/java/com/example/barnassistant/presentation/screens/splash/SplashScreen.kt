package com.example.barnassistant.presentation.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.barnassistant.R
import com.example.barnassistant.presentation.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
    var isPlaying = true
    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            // Infinite repeatable rotation when is playing
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = tween(
                    durationMillis = 1250,
                    easing = LinearOutSlowInEasing
                )
//                animationSpec = infiniteRepeatable(
//                    animation = tween(3000, easing = LinearEasing),
//                   repeatMode = RepeatMode.Restart
//                )
            ) {
                currentRotation = value

            }
        } else {
            if (currentRotation > 0f) {
                // Slow down rotation on pause
                rotation.animateTo(
                    targetValue = currentRotation + 50,
                    animationSpec = tween(
                        durationMillis = 1250,
                        easing = LinearOutSlowInEasing
                    )
                ) {
                    currentRotation = value
                }
            }
        }
    }

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(0.9f, animationSpec = tween(800, easing = {
            OvershootInterpolator(8f)
                .getInterpolation(it)
        }))
        delay(3000)
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(AppScreens.LoginScreen.name)
        }
        else {
            navController.navigate(AppScreens.HomeScreen.name)
        }
        //  navController.navigate(ReaderScreens.LoginScreen.name)
    }
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale = scale.value)
            .rotate(rotation.value),

        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    )
    {
        Column(
            Modifier.padding(1.dp), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
          //  ReaderLogo()
            Animat(navController)
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "\"Read.Change.Yourself\"",
                style = MaterialTheme.typography.h5,
                color = Color.Gray.copy(alpha = 0.5f)
            )


        }


    }


}

@Composable
private fun Animat(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
        // .background(MaterialTheme.colors.primary)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottiefiles_sticker_0))
        val logoAnimationState =
            animateLottieCompositionAsState(
                composition = composition,
                restartOnPlay = true,
                iterations = 10
            )
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress },

            )
//        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
//            navController.navigate(AppScreens.LoginScreen.name)
//        }
    }
}
