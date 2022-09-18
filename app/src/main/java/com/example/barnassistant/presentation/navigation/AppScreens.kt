package com.example.barnassistant.presentation.navigation

import java.lang.IllegalArgumentException

enum class AppScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    ReaderStatsScreen,
    MessageListScreen,
    ChannelListScreen,
    HelpScreen,
    Counter;

    companion object {
        fun fromRoute(route: String?): AppScreens
                = when(route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            ReaderStatsScreen.name -> ReaderStatsScreen
            ChannelListScreen.name ->ChannelListScreen
            MessageListScreen.name ->MessageListScreen
            HelpScreen.name ->HelpScreen
            Counter.name ->Counter
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}