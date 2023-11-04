package com.gosocial.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.gosocial.app.CreatePostActivity
import com.gosocial.app.adapters.PostsAdapter
import com.gosocial.app.daos.PostDao
import com.gosocial.app.databinding.ActivityMainBinding
import com.gosocial.app.models.Post

class MainActivity : AppCompatActivity(), PostsAdapter.OnLikeClickAction {

    private lateinit var binding: ActivityMainBinding
    private val postDao = PostDao()
    private lateinit var mAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postsCollections = postDao.collection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        mAdapter = PostsAdapter(recyclerViewOptions, this)

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = mAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun onLikeClick(postId: String) {
        postDao.updateLikes(postId)
    }
}