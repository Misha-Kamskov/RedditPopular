package com.example.models.models.posts

import com.example.models.source.posts.ApiResponse

interface PostsSource {

    suspend fun getTopPosts(after : String?): ApiResponse

}