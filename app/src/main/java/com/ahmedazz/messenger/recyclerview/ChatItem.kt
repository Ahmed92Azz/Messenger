package com.ahmedazz.messenger.recyclerview

import android.content.Context
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.TextMessage
import com.ahmedazz.messenger.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class ChatItem(
    val uid: String,
    val user: User,
    val textMessage: TextMessage,
    val context: Context
) : Item() {


    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val currentUserDocRef : DocumentReference
    get() = firestoreInstance.document("users/$uid")

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val format = SimpleDateFormat("hh: mm: a")
        val time = format.format(Date(textMessage.date.time))


        viewHolder.item_tiem_textView.text = time
        viewHolder.item_last_message_textView.text = textMessage.text

        getCurrentUser{user ->
            viewHolder.item_name_textView.text = user.name

            if (user.profileImage.isNotEmpty()) {
                GlideApp.with(context)
                    .load(storageInstance.getReference(user.profileImage))
                    .into(viewHolder.item_circleImageView)
            } else {
                viewHolder.item_circleImageView.setImageResource(R.drawable.ic_account_circle)
            }
        }

    }

    private fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }

    override fun getLayout(): Int {
        return R.layout.recycler_view_item
    }
}