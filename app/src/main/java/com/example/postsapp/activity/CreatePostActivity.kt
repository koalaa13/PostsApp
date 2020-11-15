package com.example.postsapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.postsapp.R
import com.example.postsapp.application.MyApp
import com.example.postsapp.model.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePostActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreatePostActivity"
    }

    private var call: Call<Post>? = null

    private fun clearFields() {
        userIdInput.text.clear()
        titleInput.text.clear()
        textInput.text.clear()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_create_post)

        val context = this
        publishPostButton.setOnClickListener {
            Log.i(TAG, "Publish post button pressed")
            publishPostButton.isEnabled = false
            var errorMessage = ""
            var userId = 0
            val title: String = titleInput.text.toString()
            val text: String = textInput.text.toString()


            if (text.isEmpty()) {
                errorMessage = "Text should be not empty"
            }

            if (title.isEmpty()) {
                errorMessage = "Title should be not empty"
            }

            if (userIdInput.text.isEmpty()) {
                errorMessage = "UserId should be not empty"
            }

            try {
                userId = userIdInput.text.toString().toInt()
            } catch (e: NumberFormatException) {
                errorMessage = "UserId should be a number"
            }

            clearFields()

            if (errorMessage.isEmpty()) {
                Log.i(TAG, "Error didn't happen")
                val newPost = Post(id = null, userId = userId, title = title, body = text)
                call = MyApp.instance.postsService.addPost(newPost)
                call?.enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        MyApp.instance.infoMessage(context, "Successfully added new post")
                        MyApp.instance.posts.add(0, response.body()!!)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        MyApp.instance.infoMessage(context, "Error occurred while adding new post")
                    }
                })
            } else {
                MyApp.instance.infoMessage(context, errorMessage)
            }
            publishPostButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        call?.cancel()
        call = null
    }
}