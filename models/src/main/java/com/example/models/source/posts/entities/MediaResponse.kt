package com.example.models.source.posts.entities

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("reddit_video")
    val redditVideo: RedditVideo?
)