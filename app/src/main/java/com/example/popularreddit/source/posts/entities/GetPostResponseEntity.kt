package com.example.popularreddit.source.posts.entities

import com.example.popularreddit.models.Utils
import com.example.popularreddit.models.posts.entities.Post
import com.google.gson.annotations.SerializedName

data class GetPostResponseEntity(
    @SerializedName("subreddit_name_prefixed")
    val author: String,

    @SerializedName("created_utc")
    val timeOfCreation: Double,

    @SerializedName("num_comments")
    val numComments: Long,

    @SerializedName("thumbnail")
    val thumbnail: String?,

    @SerializedName("url_overridden_by_dest")
    val urlDest: String?,

    @SerializedName("thumbnail_width")
    val thumbnailWidth: Float?,

    @SerializedName("thumbnail_height")
    val thumbnailHeight: Float?,

    @SerializedName("is_video")
    val isVideo: Boolean,

    @SerializedName("fallback_url")
    val videoUrl: String?,

    ) {
    fun toPost(): Post {
        return Post(
            authorName = author,
            timeOfCreation = Utils.getDifferenceInHours(timeOfCreation),
            numComments = numComments,
            thumbnail = thumbnail,
            urlDest = urlDest,
            widthThumbnail = thumbnailWidth,
            heightThumbnail = thumbnailHeight,
            isVideo = isVideo,
            videoUrl = videoUrl
        )
    }
}

