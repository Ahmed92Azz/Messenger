package com.ahmedazz.messenger

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.ahmedazz.messenger.fragments.ChatFragment
import com.ahmedazz.messenger.fragments.DiscoverFragment
import com.ahmedazz.messenger.fragments.PeopleFragment
import com.ahmedazz.messenger.glide.GlideApp
import com.ahmedazz.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val mChatFragment = ChatFragment()
    private val mPeopleFragment = PeopleFragment()
    private val mMoreFragment = DiscoverFragment()

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firestoreInstance.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)

                if (user!!.profileImage.isNotEmpty()){
                    GlideApp.with(this@MainActivity)
                        .load(storageInstance.getReference(user.profileImage))
                        .placeholder(R.drawable.ic_account_circle)
                        .into(circleImageView_profile_image)
                } else {
                    circleImageView_profile_image.setImageResource(R.drawable.ic_account_circle)
                }
            }


        setSupportActionBar(toolbar_main)
        supportActionBar?.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }
        bottomNavigationView_main.setOnNavigationItemSelectedListener(this@MainActivity)

        setFragment(mChatFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_chat_item -> {
                setFragment(mChatFragment)
                return true
            }

            R.id.navigation_people_item -> {
                setFragment(mPeopleFragment)
                return true
            }

            R.id.navigation_more_item -> {
                setFragment(mMoreFragment)
                return true
            }

            else -> return false
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.coordinatorLayout_main_content, fragment)
        fr.commit()
    }
}
