package com.example.popularreddit.source.posts

import com.example.popularreddit.models.posts.PostsSource
import com.example.popularreddit.models.posts.entities.Post
import retrofit2.Retrofit
import com.example.popularreddit.source.base.BaseRetrofitSource
import com.example.popularreddit.source.base.RetrofitConfig

class RetrofitPostsSource(config: RetrofitConfig) : BaseRetrofitSource(config), PostsSource {

    private val postsApi = retrofit.create(PostsApi::class.java)

    override suspend fun getTopPosts(after : String?): ApiResponse = wrapRetrofitExceptions {
        postsApi.getTopPosts(after)
    }
}