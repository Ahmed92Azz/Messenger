package com.ahmedazz.messenger.recyclerview

import android.content.Context
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.User
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_search.*

class SearchItem(val user: User, val otherUserId: String, val context: Context): Item(){

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView_user_name_search.text = user.name

        GlideApp.with(context)
            .load(storageInstance.getReference(user.profileImage))
            .placeholder(R.drawable.ic_account_circle)
            .into(viewHolder.imageView_user_image_search)

    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {

        if (other !is SearchItem)
            return false
        if (this.user != other.user)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? SearchItem)
    }

    override fun getLayout() = R.layout.item_search
}