package com.example.postsapp.application

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.example.postsapp.model.Post
import com.example.postsapp.service.PostsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    lateinit var postsService: PostsService
        private set

    lateinit var posts: ArrayList<Post>

    private class RetrofitClientInstance {
        companion object {
            private lateinit var retrofit: Retrofit
            private const val BASE_URL = "https://jsonplaceholder.typicode.com"

            fun getRetrofitInstance(): Retrofit {
                if (!this::retrofit.isInitialized) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retrofit
            }
        }
    }

    fun infoMessage(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        posts = ArrayList()
        postsService = RetrofitClientInstance.getRetrofitInstance().create(PostsService::class.java)
    }
}