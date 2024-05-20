package com.example.models.source.base

import com.example.models.source.SourcesProvider
import com.example.models.models.posts.PostsSource
import com.example.models.source.posts.RetrofitPostsSource

/**
 * Creating sources based on Retrofit .
 */
class RetrofitSourcesProvider(
    private val config: RetrofitConfig
) : SourcesProvider {

    override fun getPostsSource(): PostsSource {
        return RetrofitPostsSource(config)
    }

}