package com.example.postsapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postsapp.PostAdapter
import com.example.postsapp.R
import com.example.postsapp.application.MyApp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsActivity : AppCompatActivity() {
    companion object {
        const val TAG = "PostsActivity"
    }

    private var deleteCall: Call<ResponseBody>? = null

    private lateinit var addPostIntent: Intent
    private lateinit var postsAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_main)

        val viewManager = LinearLayoutManager(this)
        val context = this

        addPostIntent = Intent(context, CreatePostActivity::class.java)
        if (!this::postsAdapter.isInitialized) {
            postsAdapter = PostAdapter(MyApp.instance.posts) {
                delete_button.isEnabled = false
                deleteCall = it.id?.let { it1 -> MyApp.instance.postsService.deletePost(it1) }
                deleteCall?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        MyApp.instance.infoMessage(
                            context,
                            "Successfully deleted post ${it.id}"
                        )
                        val posts = MyApp.instance.posts
                        for (ind in 0 until posts.size) {
                            if (posts[ind].id == it.id) {
                                posts.removeAt(ind)
                                break
                            }
                        }
                        postsAdapter.notifyDataSetChanged()
                        delete_button.isEnabled = true
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        MyApp.instance.infoMessage(
                            context,
                            "Error occurred while deleting post ${it.id}"
                        )
                        delete_button.isEnabled = true
                    }
                })
            }
        }

        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = postsAdapter
        }

        addPostButton.setOnClickListener {
            startActivity(addPostIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")

        if (this::postsAdapter.isInitialized) {
            postsAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteCall?.cancel()
        deleteCall = null
    }
}