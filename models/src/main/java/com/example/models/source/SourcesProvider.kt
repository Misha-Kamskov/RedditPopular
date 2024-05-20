package com.example.models.source

import com.example.models.models.posts.PostsSource

interface SourcesProvider {

    fun getPostsSource() : PostsSource

}