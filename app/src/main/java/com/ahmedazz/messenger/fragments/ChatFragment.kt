package com.ahmedazz.messenger.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ahmedazz.messenger.ChatActivity
import com.ahmedazz.messenger.ProfileActivity

import com.ahmedazz.messenger.R
import com.ahmedazz.messenger.model.User
import com.ahmedazz.messenger.recyclerview.ChatItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {


    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var chatSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val textViewTitle = activity?.findViewById(R.id.title_toolbar_textView) as TextView
        textViewTitle.text = "Chats"

        val circleImageViewProfileImage = activity?.findViewById(R.id.circleImageView_profile_image) as ImageView

        circleImageViewProfileImage.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
            activity!!.finish()
        }

        // Listening of chats.....
        addChatListener(::initRecyclerView)

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private fun addChatListener(onListen: (List<Item>) -> Unit): ListenerRegistration {

        return firestoreInstance.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("sharedChat")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if (firebaseFirestoreException != null) {
                return@addSnapshotListener
            }

            val items = mutableListOf<Item>()

            querySnapshot!!.documents.forEach {document ->

                if (document.exists()){
                    items.add(ChatItem(document.id,document.toObject(User::class.java)!!, activity!!))
                }

            }
            onListen(items)
        }
    }

    private fun initRecyclerView(item: List<Item>) {

        chat_recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GroupAdapter<ViewHolder>().apply {
                chatSection = Section(item)
                add(chatSection)
                setOnItemClickListener(onItemClick)
            }
        }
    }

    private val onItemClick = OnItemClickListener { item, view ->

        if (item is ChatItem){

            val intentChatActivity = Intent(activity, ChatActivity::class.java)
            intentChatActivity.putExtra("user_name",item.user.name)
            intentChatActivity.putExtra("profile_image",item.user.profileImage)
            intentChatActivity.putExtra("other_uid",item.uid)
            activity!!.startActivity(intentChatActivity)
        }

    }

}
