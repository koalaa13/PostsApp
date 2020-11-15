package com.example.postsapp.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.postsapp.R
import com.example.postsapp.application.MyApp
import com.example.postsapp.model.Post
import kotlinx.android.synthetic.main.activity_loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LoadingActivity"
    }

    private lateinit var errorActivityIntent: Intent
    private lateinit var postsActivityIntent: Intent
    private var call: Call<ArrayList<Post>>? = null

    private fun startActivityWithDelay(delay: Long, intent: Intent) {
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, delay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        this.errorActivityIntent = Intent(this, ErrorActivity::class.java)
        this.postsActivityIntent = Intent(this, PostsActivity::class.java)

        ObjectAnimator.ofFloat(loading_text, View.ALPHA, 0f, 1f, 0f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = Animation.INFINITE
            duration = 5000
            start()
        }

        call = MyApp.instance.postsService.getAllPosts()
        call?.enqueue(object : Callback<ArrayList<Post>> {
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                Log.i(TAG, "Successfully downloaded posts")
                MyApp.instance.posts = response.body()!!
                startActivityWithDelay(2000L, postsActivityIntent)
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.i(TAG, "Callback failure")
                startActivityWithDelay(5000L, errorActivityIntent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        call?.cancel()
        call = null
    }
}