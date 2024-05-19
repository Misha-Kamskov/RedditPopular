package com.example.popularreddit.models.posts

import com.example.popularreddit.models.posts.entities.Post
import com.example.popularreddit.source.wrapBackendExceptions

class PostsRepository(private val postsSource: PostsSource) {

    suspend fun getTopPosts(): List<Post> {
        wrapBackendExceptions {
            return postsSource.getTopPosts().data.children.map { it.data.toPost() }
        }
    }

}


