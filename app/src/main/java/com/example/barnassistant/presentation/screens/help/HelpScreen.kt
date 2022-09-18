package com.example.barnassistant.presentation.screens.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.barnassistant.presentation.components.BarnAppBar

@Composable
fun HelpScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        BarnAppBar(
            title = "Help",
           icon = Icons.Filled.ArrowBack,
            onBackArrowClicked = { navController.popBackStack() },
            showProfile = false, onSearchClicked = {}, navController = navController
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Help.", style = MaterialTheme.typography.h4)
        }
    }
}