package com.example.models.models.posts.entities


data class Post(
    val authorName : String,
    val timeOfCreation : Long,
    val numComments : Long,
    val selfText: String,
    val thumbnail : String?,
    val urlDest : String?,
    val widthThumbnail: Float?,
    val heightThumbnail: Float?,
    val isVideo: Boolean,
    val videoUrl: String?,
)
