package com.example.postsapp.service

import com.example.postsapp.model.Post
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PostsService {
    @GET("posts")
    fun getAllPosts(): Call<ArrayList<Post>>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("posts")
    fun addPost(@Body postData: Post): Call<Post>
}