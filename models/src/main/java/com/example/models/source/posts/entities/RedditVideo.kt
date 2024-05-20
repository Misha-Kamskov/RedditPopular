package com.example.models.source.posts.entities

import com.google.gson.annotations.SerializedName

data class RedditVideo(
    @SerializedName("fallback_url")
    val fallbackUrl: String?
)
