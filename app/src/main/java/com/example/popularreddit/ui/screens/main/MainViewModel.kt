package com.example.popularreddit.ui.screens.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.models.models.posts.PostsRepository
import com.example.models.models.posts.entities.Post
import com.example.models.source.BackendException
import com.example.models.source.ConnectionException
import com.example.models.source.ParseBackendResponseException
import com.example.mvi.MviViewModel
import com.example.mvi.states.AbstractAction
import com.example.mvi.states.AbstractEvent
import com.example.mvi.states.AbstractState
import com.example.popularreddit.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : MviViewModel<MainState, MainEvent, MainAction>() {

    override val initialState: MainState
        get() = MainState()

    override fun processAction(action: MainAction) {
        when (action) {
            is MainAction.GetTopPosts -> getTopPosts()
        }
    }

    private fun getTopPosts() {
        viewModelScope.launch {
            try {
                val postsFlow = postsRepository.getTopPosts()
                updateState(MainState(posts = postsFlow))
            } catch (e: Exception) {
                updateState(MainState())
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
    val posts: Flow<PagingData<Post>> = emptyFlow()
) : AbstractState()

sealed class MainEvent : AbstractEvent() {
    data class ShowError(val message: Int) : MainEvent()
}

sealed class MainAction : AbstractAction() {
    data object GetTopPosts : MainAction()
}