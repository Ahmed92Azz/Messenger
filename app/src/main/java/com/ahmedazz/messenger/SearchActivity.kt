package com.ahmedazz.messenger

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.support.v7.widget.SearchView
import com.ahmedazz.messenger.model.User
import com.ahmedazz.messenger.recyclerview.SearchItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    //
    val firestoreInstance by lazy {
        FirebaseFirestore.getInstance()
    }

    private var shouldInitRecyclerView = true
    private lateinit var searchSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)




        setSupportActionBar(toolbar_search)

        supportActionBar?.title = ""
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
                    val query = firestoreInstance
                        .collection("users")
                        .orderBy("name")
                        .startAt(p0.trim())
                        .endAt(p0.trim()+ "\uf8ff")
                    showResultSearch(::updateRecyclerView, query)

                    return true
                }

            })
        }
        return true
    }

    private fun showResultSearch(onListen: (List<Item>) -> Unit, query: Query) {
        val items = mutableListOf<Item>()
        query.get().addOnSuccessListener {
            it.documents.forEach { document ->
                items.add(SearchItem(document.toObject(User::class.java)!!, this@SearchActivity))
            }
            onListen(items)
        }

    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recyclerView_search.apply {
                layoutManager =
                    LinearLayoutManager(this@SearchActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    searchSection = Section(items)
                    add(searchSection)
                }

            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = searchSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()
    }

}
