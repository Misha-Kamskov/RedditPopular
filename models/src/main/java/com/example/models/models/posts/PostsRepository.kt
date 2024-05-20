package com.example.models.models.posts

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.models.models.posts.entities.Post
import com.example.models.source.wrapBackendExceptions
import kotlinx.coroutines.flow.Flow

class PostsRepository(private val postsSource: PostsSource) {

    fun getTopPosts(): Flow<PagingData<Post>> {
        wrapBackendExceptions {
            return Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                ),
                pagingSourceFactory = { PostsPagingSource(postsSource) }
            ).flow
        }
    }

}


