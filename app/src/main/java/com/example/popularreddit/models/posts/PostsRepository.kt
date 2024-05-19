package com.example.popularreddit.models.posts

import com.example.popularreddit.models.posts.entities.Post

class PostsRepository(private val postsSource: PostsSource) {

    suspend fun getTopPosts(): List<Post> {
        return postsSource.getTopPosts().data.children.map { it.data.toPost() }
    }

}


