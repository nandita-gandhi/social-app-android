package com.gosocial.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.gosocial.app.Helpers.Utils
import com.gosocial.app.R
import com.gosocial.app.databinding.RowPostItemRvBinding
import com.gosocial.app.models.Post

class PostsAdapter(options: FirestoreRecyclerOptions<Post>, val listener: OnLikeClickAction) :
    FirestoreRecyclerAdapter<Post, PostsAdapter.PostsViewHolder>(
        options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(RowPostItemRvBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int, model: Post) {
        holder.bind(model)
    }

    inner class PostsViewHolder(private val binding: RowPostItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                tvPostTitle.text = post.postText
                tvUserName.text = post.createdBy.displayName
                tvLikeCount.text = "${post.likedBy.size}"
                Glide.with(ivUserImage.context).load(post.createdBy.imageUrl).circleCrop()
                    .into(ivUserImage)
                tvCreatedAt.text = Utils.getTimeAgo(post.createdAt)

                ivLikeButton.setOnClickListener {
                    listener.onLikeClick(snapshots.getSnapshot(adapterPosition).id)
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val isLiked = post.likedBy.contains(currentUserId)
                if (isLiked) {
                    ivLikeButton.setImageDrawable(ContextCompat.getDrawable(ivLikeButton.context,
                        R.drawable.ic_liked))
                } else {
                    ivLikeButton.setImageDrawable(ContextCompat.getDrawable(ivLikeButton.context,
                        R.drawable.ic_unliked))
                }
            }
        }
    }

    interface OnLikeClickAction {
        fun onLikeClick(postId: String)
    }
}