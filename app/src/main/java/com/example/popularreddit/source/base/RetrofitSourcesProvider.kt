package com.example.popularreddit.source.base

import com.example.popularreddit.source.SourcesProvider
import com.example.popularreddit.models.posts.PostsSource
import com.example.popularreddit.source.posts.RetrofitPostsSource

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