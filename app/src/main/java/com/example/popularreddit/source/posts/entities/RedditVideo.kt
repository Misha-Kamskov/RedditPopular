package com.example.popularreddit.source.posts.entities

import com.google.gson.annotations.SerializedName

data class RedditVideo(
    @SerializedName("fallback_url")
    val fallbackUrl: String?
)
