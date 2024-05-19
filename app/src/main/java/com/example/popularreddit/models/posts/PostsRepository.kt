package com.example.popularreddit.models.posts

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.popularreddit.models.posts.entities.Post
import com.example.popularreddit.source.wrapBackendExceptions
import kotlinx.coroutines.flow.Flow

class PostsRepository(private val postsSource: PostsSource) {

    fun getTopPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PostsPagingSource(postsSource) }
        ).flow
    }

}


