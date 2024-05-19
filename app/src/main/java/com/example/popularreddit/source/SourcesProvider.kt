package com.example.popularreddit.source

import com.example.popularreddit.models.posts.PostsSource

interface SourcesProvider {

    fun getPostsSource() : PostsSource

}