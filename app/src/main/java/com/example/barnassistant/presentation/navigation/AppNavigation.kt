package com.example.barnassistant.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.barnassistant.presentation.screens.channel_list_screen.ChannelListScreen
//import com.example.barnassistant.presentation.screens.channel_list_screen.ChannelListScreen
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.detail.DetailBarnListScreen
import com.example.barnassistant.presentation.screens.home.HomeScreen
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.example.barnassistant.presentation.screens.login.LoginScreen
import com.example.barnassistant.presentation.screens.message_list_screen.MessageListScreen
import com.example.barnassistant.presentation.screens.splash.SplashScreen
import com.example.barnassistant.presentation.screens.update_screen.UpdateScreen
import com.example.barnassistant.presentation.screens.update_screen.UpdateScreenViewModel

@OptIn(ExperimentalFoundationApi::class)
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
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            val detailViewModel = hiltViewModel<BarnItemViewModel>()
            HomeScreen(navController = navController,viewModel, detailViewModel)
        }
        composable(AppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

//        composable(AppScreens.DetailScreen.name) {
//            val viewModel = hiltViewModel<BarnItemViewModel>()
//            DetailBarnListScreen(navController = navController,viewModel)
//        }

//        composable(AppScreens.UpdateScreen.name) {
//            val viewModel = hiltViewModel<HomeScreenViewModel>()
//            val detailViewModel = hiltViewModel<BarnItemViewModel>()
//            UpdateScreen(navController = navController,viewModel,detailViewModel)
//        }

        val detailName2 = AppScreens.DetailScreen.name
        composable("$detailName2/{listName}", arguments = listOf(navArgument("listName"){
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("listName").let {
                val viewModel = hiltViewModel<HomeScreenViewModel>()
                val detailViewModel = hiltViewModel<BarnItemViewModel>()
                DetailBarnListScreen(navController = navController,detailViewModel,viewModel, curName = it.toString())
            }
        }


        val detailName = AppScreens.UpdateScreen.name
        composable("$detailName/{listName}", arguments = listOf(navArgument("listName"){
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("listName").let {
                val viewModel = hiltViewModel<HomeScreenViewModel>()
            val detailViewModel = hiltViewModel<BarnItemViewModel>()
            val updateViewModel = hiltViewModel<UpdateScreenViewModel>()
                UpdateScreen(navController = navController,viewModel,detailViewModel, curName = it.toString(),updateViewModel)
            }
        }
    val channelListName = AppScreens.ChannelListScreen.name
        composable(channelListName) {
          ChannelListScreen(navController = navController)
        }
        val messageListName = AppScreens.MessageListScreen.name
        composable("$messageListName/{cid}") { backStackEntry ->
            MessageListScreen(
                navController = navController,
                cid = backStackEntry.arguments?.getString("cid")!!
            )
        }

    }
}


