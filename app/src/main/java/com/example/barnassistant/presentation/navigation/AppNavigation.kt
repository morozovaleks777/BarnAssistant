package com.example.barnassistant.presentation.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.detail.DetailBarnListScreen
import com.example.barnassistant.presentation.screens.detail.DetailBarnListViewModel
import com.example.barnassistant.presentation.screens.home.HomeScreen
import com.example.barnassistant.presentation.screens.login.LoginScreen
import com.example.barnassistant.presentation.screens.splash.SplashScreen

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = AppScreens.SplashScreen.name ){
        composable(AppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(AppScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(AppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(AppScreens.DetailScreen.name) {
            val viewModel = hiltViewModel<BarnItemViewModel>()
            DetailBarnListScreen(navController = navController,viewModel)
        }
    }
}


