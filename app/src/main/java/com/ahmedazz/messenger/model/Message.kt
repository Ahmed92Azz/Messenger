package com.ahmedazz.messenger.model

import java.util.*

interface Message {

    val senderId: String
    val recipientId: String
    val senderName: String
    val recipientName: String
    val date: Date
    val type: String
}