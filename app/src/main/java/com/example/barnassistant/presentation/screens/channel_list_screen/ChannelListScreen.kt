package com.example.barnassistant.presentation.screens.channel_list_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.barnassistant.R
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.CreateButton
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.extensions.internal.addMember
import io.getstream.chat.android.client.extensions.internal.updateLastMessage
import io.getstream.chat.android.client.extensions.internal.updateMember
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import java.io.File
import java.util.*
import kotlin.math.absoluteValue
import kotlin.random.Random


@OptIn(InternalStreamChatApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChannelListScreen(
    navController: NavController,


) {
val userName= rememberSaveable {
    mutableStateOf("")
}
//    val userNameList= rememberSaveable {
//        mutableListOf<String>("")
//    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val offlinePluginFactory = StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
        ),
        appContext = context,
    )

val client = ChatClient.Builder("zp2zrhmrf4mz", appContext = context)
        .withPlugin(offlinePluginFactory)
        .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
        .build()
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0)else
        "N.A"
    Log.d("test", "onCreate: chanelList currentUserName $currentUserName ")
    val user = User().apply {

        id = currentUserName?:""
        name = currentUserName?:""
        image = "https://bit.ly/2TIt8NR"

    }
    val token = client.          devToken(user.id)
    val token2 = "${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
    Log.d("test", "onCreate: token ${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw")

    val userNameList= rememberSaveable {
        mutableListOf(user.name)
    }

    client.connectUser(
        user,
        token2
        //"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoibW9yYWxleDI2MCJ9.zLbGPVDvY3WfiBcc331cCeBrWS6Py10wMZ27ai5Kupo"
    ).enqueue()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        ChatTheme {
            Column() {

var list= mutableListOf<String>()
                com.example.barnassistant.presentation.components.InputField(valueState = userName,
                    labelId = "enter  name",
                    keyboardType = KeyboardType.Text,
                    enabled = true,
                    onAction = KeyboardActions {
                        // if (!valid) return@KeyboardActions
userNameList.add(userName.value)
                        userName.value = ""
                        keyboardController?.hide()
                    })
                CreateButton(textId = "get",
                    loading = false,
                    validInputs =true ) {
list.addAll(0,userNameList)

                    Log.d("testik", "ChannelListScreen:list ${list}")





                }

            val i= rememberSaveable {
                mutableStateOf((Random.nextInt(2,9999999)*Random.nextInt(1,99)).absoluteValue)
            }
                ChannelsScreen(
                    onHeaderAvatarClick = {

                    },
                    onHeaderActionClick = {


                        Log.d("testik", "ChannelListScreen:${i.value} ")
                      client.channel("messaging", "${i.value}")
                           .create(list, emptyMap()).enqueue()

                    navController.navigate("${AppScreens.MessageListScreen.name}/messaging:${i.value}")
                      // navController.navigate("${AppScreens.MessageListScreen.name}/${cs.cid}")
                    },

                    isShowingHeader = true,
                    title = stringResource(id = R.string.app_name),
                    isShowingSearch = true,
                    onItemClick = { channel ->
                        navController.navigate("${AppScreens.MessageListScreen.name}/${channel.cid}")


                    },
                    onBackPressed = { navController.navigate(AppScreens.HomeScreen.name) }
                )
            }
        }
        }
    }




