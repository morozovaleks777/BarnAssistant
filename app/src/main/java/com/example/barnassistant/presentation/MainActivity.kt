package com.example.barnassistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.barnassistant.presentation.navigation.AppNavigation
import com.example.barnassistant.ui.theme.BarnAssistantTheme
import dagger.hilt.android.AndroidEntryPoint
@ExperimentalMaterialApi
@AndroidEntryPoint
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarnAssistantTheme {
               BarnAssistantApp()
            }
        }
    }
}


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BarnAssistantApp() {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize() ) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            AppNavigation()
        }

    }
}



@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BarnAssistantTheme {
        BarnAssistantApp()
    }
}