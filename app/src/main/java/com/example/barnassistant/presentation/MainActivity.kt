package com.example.barnassistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
//        val offlinePluginFactory = StreamOfflinePluginFactory(
//            config = Config(
//                backgroundSyncEnabled = true,
//                userPresence = true,
//                persistenceEnabled = true,
//                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
//            ),
//            appContext = applicationContext,
//        )
//
//
//
//
//        val client = ChatClient.Builder("zp2zrhmrf4mz", applicationContext)
//            .withPlugin(offlinePluginFactory)
//            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
//            .build()
//
//        val email = FirebaseAuth.getInstance().currentUser?.email
//        val currentUserName = if (!email.isNullOrEmpty())
//            FirebaseAuth.getInstance().currentUser?.email?.split("@")
//                ?.get(0)else
//            "N/A"
//        Log.d("test", "onCreate: currentUserName $currentUserName ")
//        val user = User().apply {
//
//          //  id = currentUserName?:""
//            id = "tutorial-droid"
//            name = "Tutorial Droid"
//            image = "https://bit.ly/2TIt8NR"
//
//        }
//        val token = client.          devToken(user.id)
//        val token2 = "${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
//        Log.d("test", "onCreate: token ${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw")
//
//
//
//        client.connectUser(
//            user,
//          //  token2
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
//        ).enqueue()








        setContent {
            BarnAssistantTheme {
Column {

BarnAssistantApp()
//    ChatTheme {
//      //  BarnAssistantApp()
//        ChannelsScreen(
//            title = stringResource(id = R.string.app_name),
//            isShowingSearch = true,
//            onItemClick = { channel ->
//                startActivity(MessagesActivity.getIntent(context = this@MainActivity, channel.cid))
//            },
//            onBackPressed = { finish() }
//        )
//    }
}

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