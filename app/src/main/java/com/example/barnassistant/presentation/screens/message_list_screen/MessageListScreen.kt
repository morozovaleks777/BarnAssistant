package com.example.barnassistant.presentation.screens.message_list_screen

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barnassistant.presentation.MainActivity
import com.example.barnassistant.presentation.navigation.AppScreens
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.chat.android.client.extensions.internal.updateLastMessage
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.compose.ui.components.composer.InputField
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.ui.util.getLastMessage
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import okhttp3.internal.platform.android.BouncyCastleSocketAdapter.Companion.factory
import java.util.*
import javax.inject.Inject


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
