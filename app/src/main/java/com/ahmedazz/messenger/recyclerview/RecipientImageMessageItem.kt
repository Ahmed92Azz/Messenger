package com.ahmedazz.messenger.recyclerview

import android.content.Context
import android.text.format.DateFormat
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.ImageMessage
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.sender_item_image_message.*

class RecipientImageMessageItem(private val imageMessage: ImageMessage,
                             private val messageId: String,
                             val context: Context) : Item() {

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView_message_time.text = DateFormat.format("hh:mm a", imageMessage.date).toString()

        if (imageMessage.imagePath.isNotEmpty()){
            GlideApp.with(context)
                .load(storageInstance.getReference(imageMessage.imagePath))
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(viewHolder.imageView_message_image)
        }
    }

    override fun getLayout() = R.layout.recipient_item_image_message
}