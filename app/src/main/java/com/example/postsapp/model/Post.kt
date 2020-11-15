package com.example.postsapp.model

import com.google.gson.annotations.SerializedName

class Post(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)