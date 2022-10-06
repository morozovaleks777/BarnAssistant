package com.example.barnassistant.presentation.screens.channel_list_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.barnassistant.R
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.CreateButton
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.ProgressCallback
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import java.io.File
import kotlin.math.absoluteValue
import kotlin.random.Random


@SuppressLint("CheckResult")
@OptIn(ExperimentalComposeUiApi::class, InternalStreamChatApi::class)
@Composable
fun ChannelListScreen(
    navController: NavController,
client: ChatClient,
    channelViewModel: ChannelViewModel

) {

    val listobj = rememberSaveable {
        mutableStateOf(listOf<BarnItemDB>())
    }

    listobj.value = ChannelViewModel.filteredListBarnItemDB.collectAsState().value

    val userName = rememberSaveable {
        mutableStateOf("")
    }

    val messageId = rememberSaveable {
        mutableStateOf(0)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current


    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0) else
        "N.A"

    val user = User().apply {

        id = currentUserName ?: ""
        name = currentUserName ?: ""
        image = "https://bit.ly/2TIt8NR"

    }
    val token = client.devToken(user.id)
    val token2 = "${token.split("devtoken")[0]}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
    val userNameList = rememberSaveable {
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
            Column {


              val list= rememberSaveable{
                 mutableStateOf(mutableListOf<String>())
              }
                val i = rememberSaveable {
                    mutableStateOf(
                        (Random.nextInt(2, 99999) * Random.nextInt(
                            1,
                            99
                        )).absoluteValue
                    )
                }
                BarnAppBar(title ="chatik huyatik" , navController =navController,
                    showProfile = false ,icon = Icons.Default.ArrowBack,){
                    navController.navigate(AppScreens.HomeScreen.name)
                }
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
                CreateButton(
 colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8D5ACC).copy(alpha = 0.7f)),
                    textId = "create channel",
                    loading = false,
                    validInputs = true
                ) {
                    list.value.addAll(0, userNameList)

                    Log.d("testik", "ChannelListScreen:list $list")
                    client.channel("messaging", "${i.value}")
                        .create(list.value, emptyMap()).enqueue()

                }


                ChannelsScreen(
                    onHeaderAvatarClick = {



                    },
                    onHeaderActionClick = {


                        Log.d("testik", "ChannelListScreen:${i.value} ")
                        client.channel("messaging", "${i.value}")
                            .create(list.value, emptyMap()).enqueue()
                        navController.navigate("${AppScreens.MessageListScreen.name}/messaging:${i.value}")

                    },

                    isShowingHeader = false,
                    title = stringResource(id = R.string.app_name),
                    isShowingSearch = true,

                    onItemClick = { channel ->

                        val channelClient = client.channel(channelType = channel.type, channel.id)

                        Log.d(
                            "TAG",
                            "ChannelListScreen: viewModel.filteredListBarnItemDB.value ${ChannelViewModel.filteredListBarnItemDB.value}"
                        )
                        Log.d(
                            "TAG",
                            "ChannelListScreen: viewModel.filteredListBarnItemDB.value ${ Log.d(
                                "TAG",
                                "ChannelListScreen: commands ${channel.config.commands}"
                            )}"
                        )

                        if (listobj.value.isNotEmpty()) {

                            val mapper = jacksonObjectMapper()
                            val writer = mapper.writer(DefaultPrettyPrinter())
                            val str = writer.writeValueAsString(listobj.value)
                            val file = File(context.filesDir, "list.json")

                            file.createNewFile()

                            file.writeText(str)

                            channelClient.sendFile(
                                file,
                                object : ProgressCallback {
                                    override fun onSuccess(url: String?) {
                                        file.delete()
                                        Log.d("tust", "onSuccess yea good $url")
                                    }

                                    override fun onError(error: ChatError) {
                                        error.message
                                    }

                                    override fun onProgress(bytesUploaded: Long, totalBytes: Long) {

                                        Log.d("tust", "onProgress: loading.. ")

                                    }
                                }
                            )
                                .enqueue()

                            val attachment = Attachment(
                                type = "file",
                                upload = file,

                                )
                          val listName=listobj.value[0].listName
                          val   messageText="send you list of items \"$listName\""
                            val message2 = Message(
                                cid = channel.cid,
                                attachments = mutableListOf(attachment),
                                id = messageId.toString(),
                                text = messageText,
                                extraData = mutableMapOf("listJson" to str)
                            )
                            if (listobj.value.isNotEmpty() && ChannelViewModel.isNeedSendFile.value) {
                                channelClient.sendMessage(message2).enqueue {
                                    if (it.isSuccess) {
                                        ++messageId.value
                                        it.data()
                                        file.delete()
                                        listobj.value = emptyList()
                                       ChannelViewModel.isNeedSendFile.value=false
                                    }
                                    Log.d(
                                        "tust",
                                        "ChannelListScreen: attachment.uploadState ${attachment.uploadState} "
                                    )
                                    Log.d(
                                        "tust",
                                        "ChannelListScreen: it.isSuccess ${it.isSuccess} "
                                    )

                                    it.isSuccess

                                }

                            }

                            Log.d("tust", "ChannelListScreen: ${context.fileList().toList()}")
                        }
                        navController.navigate("${AppScreens.MessageListScreen.name}/${channel.cid}")


                    },
                    onBackPressed = { navController.navigate(AppScreens.HomeScreen.name) }
                )
            }
        }
    }
}






