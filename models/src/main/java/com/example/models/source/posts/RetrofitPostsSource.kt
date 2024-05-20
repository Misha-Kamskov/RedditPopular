package com.example.models.source.posts

import com.example.models.models.posts.PostsSource
import com.example.models.source.base.BaseRetrofitSource
import com.example.models.source.base.RetrofitConfig

class RetrofitPostsSource(config: RetrofitConfig) : BaseRetrofitSource(config), PostsSource {

    private val postsApi = retrofit.create(PostsApi::class.java)

    override suspend fun getTopPosts(after : String?): ApiResponse = wrapRetrofitExceptions {
        postsApi.getTopPosts(after)
    }
}