package com.ahmedazz.messenger

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.support.v7.widget.SearchView
import android.view.MenuItem
import com.ahmedazz.messenger.model.User
import com.ahmedazz.messenger.recyclerview.ChatItem
import com.ahmedazz.messenger.recyclerview.SearchItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var searchSection: Section
    private var shouldInitRecyclerView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar_search)
        supportActionBar?.title = ""

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.actionSearch)?.actionView as SearchView).apply {
            isIconified = false
            setSearchableInfo(searchManager.getSearchableInfo(componentName))

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0!!.isEmpty()){
                        return false
                    }

                    val query = db.collection("users")
                        .orderBy("name")
                        .startAt(p0.trim())
                        .endAt(p0.trim() + "\uf8ff")

                    showResultSearch(::updateRecyclerView , query)

                    return true
                }

            })
        }
        return true
    }

    private fun showResultSearch(onListen: (List<Item>) -> Unit, query: Query) {

        val items = mutableListOf<Item>()
        query.get().addOnSuccessListener {

            it.documents.forEach {document ->
                items.add(SearchItem(document.toObject(User::class.java)!!, document.id,this@SearchActivity))
            }
            onListen(items)
        }
    }

    private fun updateRecyclerView(items: List<Item>){

        fun init(){
            recyclerView_search.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)

                adapter = GroupAdapter<ViewHolder>().apply {

                    searchSection = Section(items)
                    add(searchSection)

                    setOnItemClickListener(onItemClick)
                }
            }

            shouldInitRecyclerView = false
        }

        fun updateItem() = searchSection.update(items)

        if (shouldInitRecyclerView){
            init()
        } else {
            updateItem()
        }
    }

    private val onItemClick = OnItemClickListener { item, view ->

        if (item is SearchItem){

            val intentChatActivity = Intent(this, ChatActivity::class.java)
            intentChatActivity.putExtra("user_name",item.user.name)
            intentChatActivity.putExtra("profile_image",item.user.profileImage)
            intentChatActivity.putExtra("other_uid",item.otherUserId)
            this.startActivity(intentChatActivity)
        }
    }

}
