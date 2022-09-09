package com.example.barnassistant.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import com.example.barnassistant.R
import com.example.barnassistant.dataStore
import com.example.barnassistant.presentation.MainActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



import androidx.datastore.preferences.core.stringPreferencesKey


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class TechFirebaseMessageService : FirebaseMessagingService() {



    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

//When App in background notification is handle by system
        // and it used notification payload and used title and body

        // and app in foreground i am using data payload

        // now send only data payload on that case onMessageRecievd also called in background.


        //Log incoming message
        Log.d("CloudMessage", "From ${message.from}")

        //Log Data Payload
        if (message.data.isNotEmpty()) {
            Log.d("CloudMessage", "Message Data ${message.data}")
        }

        //Check if message contains a notification payload

        message.data.let {
            Log.d("CloudMessage", "Message Data Body ${it["body"]}")
            Log.d("CloudMessage", "Message Data Title  ${it["title"]}")
            //when app in forground that notification is not shown on status bar
            //lets write a code to display notification in status bar when app in forground.
            showNotificationOnStatusBar(it)
        }

        if (message.notification != null) {

            Log.d("CloudMessage", "Notification ${message.notification}")
            Log.d("CloudMessage", "Notification Title ${message.notification!!.title}")
            Log.d("CloudMessage", "Notification Body ${message.notification!!.body}")

        }

    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    private fun showNotificationOnStatusBar(data: Map<String, String>) {

        //Create Intent it will be launched when user tap on notification from status bar.
        val intent = Intent(this, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        intent.putExtra("title",data["title"])
        intent.putExtra("body",data["body"])

        // it should be unqiue when push comes.
        var requestCode = System.currentTimeMillis().toInt()
        var pendingIntent : PendingIntent
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =
                PendingIntent.getActivity(this, requestCode,intent, FLAG_MUTABLE)
        }else{
            pendingIntent =
                PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        val builder = NotificationCompat.Builder(this,"Global").setAutoCancel(true)
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText((data["body"])))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)


        with(NotificationManagerCompat.from(this)){
            notify(requestCode,builder.build())
        }


    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM",token)
        GlobalScope.launch {
            saveGCMToken(token)
        }
    }

    //Save GCM Token DataStore Preference
    // you can used to send it on your Server.
    private suspend fun saveGCMToken(token: String) {
        val gckTokenKey = stringPreferencesKey("gcm_token")
        baseContext.dataStore.edit { pref ->
            pref[gckTokenKey] = token
        }
    }
}