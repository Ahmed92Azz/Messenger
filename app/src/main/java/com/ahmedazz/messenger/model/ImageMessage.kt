package com.ahmedazz.messenger.model

import java.util.*

class ImageMessage (val imagePath: String,
                    override val senderId: String,
                    override val recipientId: String,
                    override val senderName: String,
                    override val recipientName: String,
                    override val date: Date,
                    override val type: String = MessageType.IMAGE): Message {

    constructor(): this("", "","","","",Date(0))
}