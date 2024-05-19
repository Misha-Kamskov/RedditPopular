package com.example.popularreddit.models.posts

import com.example.popularreddit.source.posts.ApiResponse

interface PostsSource {

    suspend fun getTopPosts(after : String?): ApiResponse

}