package com.example.barnassistant.presentation.screens.message_list_screen

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barnassistant.presentation.navigation.AppScreens
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.extensions.internal.updateLastMessage
import io.getstream.chat.android.client.extensions.internal.users
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.ui.util.getLastMessage
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import java.io.File


@OptIn(InternalStreamChatApi::class)
@Composable
fun MessageListScreen(
    navController: NavController,
    cid: String,
) {




    ChatTheme(
        shapes = StreamShapes.defaultShapes().copy(
            avatar = RoundedCornerShape(8.dp),
            attachment = RoundedCornerShape(16.dp),
            myMessageBubble = RoundedCornerShape(16.dp),
            otherMessageBubble = RoundedCornerShape(16.dp),
            inputField = RectangleShape,
        )
    )  {

        MessagesScreen(
            showHeader=true,

            channelId = cid,
            messageLimit = 50,
            onBackPressed = {
                navController.navigate(AppScreens.ChannelListScreen.name) }
        ){


        }
    }

}
