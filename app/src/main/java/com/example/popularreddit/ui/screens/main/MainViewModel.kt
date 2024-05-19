package com.example.popularreddit.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.example.mvi.MviViewModel
import com.example.mvi.states.AbstractAction
import com.example.mvi.states.AbstractEvent
import com.example.mvi.states.AbstractState
import com.example.popularreddit.R
import com.example.popularreddit.models.posts.PostsRepository
import com.example.popularreddit.models.posts.entities.Post
import com.example.popularreddit.source.BackendException
import com.example.popularreddit.source.ConnectionException
import com.example.popularreddit.source.ParseBackendResponseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) :
    MviViewModel<MainState, MainEvent, MainAction>() {

    override val initialState: MainState
        get() = MainState()

    override fun processAction(action: MainAction) {
        when (action) {
            is MainAction.GetTopPosts -> getTopPosts()
        }
    }

    private fun getTopPosts() {
        viewModelScope.launch {
            val posts: List<Post>
            updateState(MainState(loading = true))
            delay(1000)
            try {
                posts = postsRepository.getTopPosts()
                updateState(MainState(posts, loading = false, retryButtonVisible = false))
            } catch (e: Exception) {
                updateState(MainState(loading = false, retryButtonVisible = true))
                when (e) {
                    is ConnectionException -> sendEvent(MainEvent.ShowError(R.string.error_message_connection))
                    is BackendException -> sendEvent(MainEvent.ShowError(R.string.error_message_backend_exception))
                    is ParseBackendResponseException -> sendEvent(MainEvent.ShowError(R.string.error_message_parse_exception))

                }
            }
        }
    }
}


class MainState(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = true,
    val retryButtonVisible: Boolean = false
) : AbstractState()

sealed class MainEvent : AbstractEvent() {
    data class ShowError(val message: Int) : MainEvent()
}

sealed class MainAction : AbstractAction() {
    data object GetTopPosts : MainAction()
}