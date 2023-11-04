package com.gosocial.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gosocial.app.daos.PostDao
import com.gosocial.app.databinding.ActivityCreatePostActivityBinding

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostActivityBinding
    private val postDao = PostDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postButton.setOnClickListener {
            if (binding.postInput.text.toString().isNotEmpty()) {
                postDao.createPost(binding.postInput.text.toString())
                finish()
            }
        }
    }

}