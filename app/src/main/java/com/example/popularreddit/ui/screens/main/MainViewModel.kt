package com.example.popularreddit.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.example.mvi.MviViewModel
import com.example.mvi.states.AbstractAction
import com.example.mvi.states.AbstractEvent
import com.example.mvi.states.AbstractState
import com.example.popularreddit.models.posts.PostsRepository
import com.example.popularreddit.models.posts.entities.Post
import dagger.hilt.android.lifecycle.HiltViewModel
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
            val posts = postsRepository.getTopPosts()
            updateState(MainState(posts))
        }
    }
}


class MainState(val posts: List<Post> = emptyList()) : AbstractState()

sealed class MainEvent : AbstractEvent()

sealed class MainAction : AbstractAction() {
    data object GetTopPosts : MainAction()
}