package com.example.barnassistant

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory


@HiltAndroidApp
class BarnAssistantApplication:Application() {

    @SuppressLint("CheckResult")
    override fun onCreate() {
    super.onCreate()

        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = applicationContext,
        )




        val client = ChatClient.Builder("zp2zrhmrf4mz", applicationContext)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()

        val email = FirebaseAuth.getInstance().currentUser?.email
        val currentUserName = if (!email.isNullOrEmpty())
            FirebaseAuth.getInstance().currentUser?.email?.split("@")
                ?.get(0)else
            "N.A"
        Log.d("test", "onCreate: currentUserName $currentUserName ")
    val user = User().apply {

            id = currentUserName?:""
            name = currentUserName?:""
            image = "https://bit.ly/2TIt8NR"

    }
        val token = client.          devToken(user.id)
        val token2 = "${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw"
        Log.d("test", "onCreate: token ${token.split("devtoken").get(0)}5km0x67w0RcTI4WswLYOQSlRbipNyQ-QSVjdHKXnYgw")



    client.connectUser(
    user,
       token2
        //"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoibW9yYWxleDI2MCJ9.zLbGPVDvY3WfiBcc331cCeBrWS6Py10wMZ27ai5Kupo"
    ).enqueue()
client.channel("messaging:messaging","new10").create(listOf("sss"), emptyMap()).enqueue()
//
        client.createChannel(channelType = "messaging", channelId = "new10", memberIds = listOf("sss"),
            mutableMapOf()).enqueue()


    }



}