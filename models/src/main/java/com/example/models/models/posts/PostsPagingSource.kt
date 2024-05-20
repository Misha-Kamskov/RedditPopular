package com.example.models.models.posts

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.models.models.posts.entities.Post


class PostsPagingSource(private val postsSource: PostsSource) : PagingSource<String, Post>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            val nextPage = params.key
            val response = postsSource.getTopPosts(nextPage)
            LoadResult.Page(
                data = response.data.children.map { it.data.toPost() },
                prevKey = null, // Reddit API does not support going backwards in pages
                nextKey = response.data.after
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}