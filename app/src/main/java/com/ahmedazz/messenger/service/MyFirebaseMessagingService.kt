package com.ahmedazz.messenger.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.ahmedazz.messenger.MainActivity
import com.ahmedazz.messenger.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    companion object{
        const val CHANNEL_ID = "CHANNEL_ID"
    }

    override fun onNewToken(token: String?) {
        Log.d("MyToken", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

//        if (remoteMessage.notification != null){
//            Log.d("notify", remoteMessage.data.toString())
//        }

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        createNotification(title, body)

    }

    private fun createNotification(title: String?, body: String?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.chat_channel_name)
            val descriptionText = getString(R.string.chat_channel_descriptionText)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.icon_messenger)
        builder.setContentTitle(title)
        builder.setContentText(body)
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        notificationManager.notify(0, builder.build())
    }
}