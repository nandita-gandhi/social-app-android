package com.gosocial.app.models

data class Post(
    val postText: String = "",
    val likedBy: ArrayList<String> = ArrayList(),
    val createdBy: User = User(),
    val createdAt: Long = 0L,
)
