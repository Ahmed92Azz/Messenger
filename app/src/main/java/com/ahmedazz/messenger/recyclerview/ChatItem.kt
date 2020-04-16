package com.ahmedazz.messenger.recyclerview

import android.content.Context
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.Document
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.*


class ChatItem(
    val uid: String,
    val user: User,
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

        viewHolder.item_tiem_textView.text = "Time"
        viewHolder.item_last_message_textView.text = "last message..."

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