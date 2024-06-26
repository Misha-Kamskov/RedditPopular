package com.example.models.source.posts

import com.example.models.source.posts.entities.GetPostResponseEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface PostsApi {

    @GET("/top/.json")
    suspend fun getTopPosts(
        @Query("after") after: String? = null
    ): ApiResponse

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

