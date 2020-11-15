package com.example.postsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postsapp.model.Post
import kotlinx.android.synthetic.main.list_item.view.*

class PostAdapter(
    private val posts: List<Post>,
    private val onClick: (Post) -> Unit
) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(post: Post) {
            with(root) {
                post_title.text = post.title
                post_text.text = post.body
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val holder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )

        holder.root.delete_button.setOnClickListener {
            onClick(posts[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
        holder.bind(posts[position])

    override fun getItemCount(): Int {
        return posts.size
    }
}