package com.example.popularreddit.source.posts

import com.example.popularreddit.source.posts.entities.GetPostResponseEntity
import retrofit2.http.GET

interface PostsApi {

    @GET("/top/.json")
    suspend fun getTopPosts(): ApiResponse

}

data class ApiResponse(
    val kind: String,
    val data: ListingData
)

data class ListingData(
    val after: String?,
    val dist: Int,
    val children: List<PostWrapper>
)

data class PostWrapper(
    val kind: String,
    val data: GetPostResponseEntity
)

