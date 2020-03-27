package com.ahmedazz.messenger.recyclerview


import android.content.Context
import android.text.format.DateFormat
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recipient_item_text_message.*

class RecipientTextMessageItem(private val textMessage: TextMessage,
                      private val messageId: String,
                      val context: Context) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.text_view_message.text = textMessage.text
        viewHolder.text_view_time.text = DateFormat.format("hh:mm a", textMessage.date).toString()
    }

    override fun getLayout() = R.layout.recipient_item_text_message
}