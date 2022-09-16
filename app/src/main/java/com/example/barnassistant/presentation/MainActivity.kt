package com.example.barnassistant.presentation

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.barnassistant.MyReseiver
import com.example.barnassistant.presentation.navigation.AppNavigation
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.example.barnassistant.ui.theme.BarnAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

private lateinit var viewModel:BarnItemViewModel
private lateinit var homeViewModel:HomeScreenViewModel
   lateinit var receiver:MyReseiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BarnItemViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeScreenViewModel::class.java]
         receiver=MyReseiver( viewModel,homeViewModel)
val intent =Intent(MyReseiver.ACTION_FILE_DOWNLOAD)
        sendBroadcast(intent)
        val intentFilter=IntentFilter().apply {
            addAction(MyReseiver.ACTION_FILE_DOWNLOAD)
            addAction(Intent.ACTION_MANAGE_PACKAGE_STORAGE)
            addAction(Intent.CATEGORY_APP_FILES)
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)


        }


        setContent {
            BarnAssistantTheme {
                Column {
                    BarnAssistantApp()

                }
            }
        }

        registerReceiver(receiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BarnAssistantApp() {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background,
      //  modifier = Modifier.fillMaxSize()
    )
    {
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