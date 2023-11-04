package com.gosocial.app.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.gosocial.app.models.Post
import com.gosocial.app.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    val collection = db.collection("posts")
    private val userDao = UserDao()

    fun createPost(text: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userInfo =
                userDao.getUserById(currentUser!!.uid).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(postText = text, createdBy = userInfo, createdAt = currentTime)
            collection.document().set(post)
        }
    }

    private fun getPostById(postId: String): Task<DocumentSnapshot> {
        return collection.document(postId).get()
    }

    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if (isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            collection.document(postId).set(post)
        }

    }
}