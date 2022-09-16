package com.example.barnassistant.presentation.screens.channel_list_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barnassistant.R
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.detail.CreateButton
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.extensions.internal.users
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.UploadedFile
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.ProgressCallback
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.util.getLastMessage
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.streams.toList




@SuppressLint("CheckResult")
@OptIn( ExperimentalComposeUiApi::class, InternalStreamChatApi::class)
@Composable
fun ChannelListScreen(
    navController: NavController,
client: ChatClient,

viewModel: BarnItemViewModel= hiltViewModel()
) {
//    val listobj= rememberSaveable() {
//        mutableStateOf(listOf<BarnItemDB>())
//    }

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
    Log.d("test", "onCreate: chanelList currentUserName $currentUserName ")
    val user = User().apply {

        id = currentUserName ?: ""
        name = currentUserName ?: ""
        image = "https://bit.ly/2TIt8NR"

    }
    val token = client.devToken(user.id)
    val token2 = "${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
    Log.d(
        "test",
        "onCreate: token ${
            token.split("devtoken").get(0)
        }5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
    )

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
            Column() {

                val list = mutableListOf<String>()
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
                    textId = "get",
                    loading = false,
                    validInputs = true
                ) {
                    list.addAll(0, userNameList)

                    Log.d("testik", "ChannelListScreen:list $list")


                }

                val i = rememberSaveable {
                    mutableStateOf(
                        (Random.nextInt(2, 9999999) * Random.nextInt(
                            1,
                            99
                        )).absoluteValue
                    )
                }

                ChannelsScreen(
                    onHeaderAvatarClick = {


                    },
                    onHeaderActionClick = {


                        Log.d("testik", "ChannelListScreen:${i.value} ")
                        client.channel("messaging", "${i.value}")
                            .create(list, emptyMap()).enqueue()
                        navController.navigate("${AppScreens.MessageListScreen.name}/messaging:${i.value}")

                    },

                    isShowingHeader = true,
                    title = stringResource(id = R.string.app_name),
                    isShowingSearch = true,

                    onItemClick = { channel ->

//                        val x = context.assets.open("list.json").bufferedReader().lines().distinct()
//                            .toList()
//                        Log.d("testos", "ChannelListScreen: list $x ")
                       // Log.d("testos", "ChannelListScreen: text ${channel.users()}")


                        //  val message = Message(
//                            text = "I’m mowing the air Rand, I’m mowing the air.",
//                            cid = channel.cid,
//                            extraData = mutableMapOf("customField" to "123")
//                        )

//                        client.sendMessage(channel.type, channelId = channel.id,message).enqueue { result ->
//                            if (result.isSuccess) {
//                              val message1: Message = result.data()
//                            } else {
//                                // Handle result.error()
//                            }
//
//                        }
                        //------------------------------------------------------------------------------

                        val channelClient = client.channel(channelType = channel.type, channel.id)

// Set a custom FileUploader implementation when building your client

                     val  listobj = listOf(BarnItemDB(1, "name name ", 2f, 4f, true, "list1"))
                      //  listobj.value =viewModel.filteredListBarnItemDB.value
                        val mapper2 = jacksonObjectMapper()
                        val writer = mapper2.writer(DefaultPrettyPrinter());
                       val str = writer.writeValueAsString(listobj)
                        val file2 = File(context.filesDir, "list8.txt")
                        val file = File(context.filesDir, "list.json")
                      // file.createNewFile()

                        file2.writeText(str)
                        file.writeText(str)
                      //  val obj: BarnItemDB = mapper2.readValue(str)
                     //   Log.d("tust", "ChannelListScreen: readfrom ${obj}")
// Upload a file, monitoring for progress with a ProgressCallback
                        channelClient.sendFile(
                            file,
                            object : ProgressCallback {
                                override fun onSuccess(url: String?) {
                                    val fileUrl = url

                                    Log.d("tust", "onSuccess yea good $fileUrl")
                                }

                                override fun onError(error: ChatError) {
                                    error.message
                                }

                                override fun onProgress(bytesUploaded: Long, totalBytes: Long) {

                                    Log.d("tust", "onProgress: loading.. ")

                                }
                            }
                        )
                            .enqueue() // No callback passed to enqueue, as we'll get notified above anyway


                        val attachment = Attachment(
                            type = "file",
                            upload = file,

                        )

                        val message2 = Message(
                            cid = channel.cid,
                            attachments = mutableListOf(attachment),
                            id = messageId.toString(),
                            text = "kuku",
                            extraData = mutableMapOf("listJson" to str)


                            )
                        channelClient.sendMessage(message2).enqueue() {
                            if (it.isSuccess) {
                                ++messageId.value
                                it.data()

                            }
                            Log.d(
                                "tust",
                                "ChannelListScreen: attachment.uploadState ${attachment.uploadState} "
                            )
                            Log.d("tust", "ChannelListScreen: it.isSuccess ${it.isSuccess} ")

                            it.isSuccess

                        }
                        Log.d("tust", "ChannelListScreen: ${context.fileList().       toList()}")

//----------------------------------------------------------------------------------------------
                        navController.navigate("${AppScreens.MessageListScreen.name}/${channel.cid}")


                    },
                    onBackPressed = { navController.navigate(AppScreens.HomeScreen.name) }
                )
            }
        }
    }
}






