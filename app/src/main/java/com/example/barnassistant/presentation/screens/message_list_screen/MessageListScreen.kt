package com.example.barnassistant.presentation.screens.message_list_screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.barnassistant.presentation.navigation.AppScreens
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes


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
