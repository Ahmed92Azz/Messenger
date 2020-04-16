package com.ahmedazz.messenger.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        Log.d("token", token)
    }
}