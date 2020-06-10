package com.ahmedazz.messenger.recyclerview

import android.content.Context
import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_search.*

class SearchItem(val user: User, private val context: Context) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView_user_name_search.text = user.name
    }


    // This method to avoid duplicate messages
    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is SearchItem)
            return false
        if (this.user != other.user)
            return false
        return true
    }

    // This method to avoid duplicate messages
    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? SearchItem)
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    override fun getLayout() = R.layout.item_search
}