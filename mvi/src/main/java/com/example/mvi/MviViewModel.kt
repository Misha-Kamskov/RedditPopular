package com.example.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.mvi.states.AbstractAction
import com.example.mvi.states.AbstractEvent
import com.example.mvi.states.AbstractState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<State : AbstractState, Event : AbstractEvent, Action : AbstractAction> :
    ViewModel() {
    
    protected abstract val initialState: State
    protected abstract fun processAction(action: Action)

    private var lastState: State = initialState

    private val _state = MutableStateFlow(initialState)
    val state : StateFlow<State> = _state.asStateFlow()

    private val _events = Channel<Event?>(Channel.BUFFERED)
    val event  = _events.receiveAsFlow()

    private val _viewActions = Channel<Action>(Channel.BUFFERED)
    init {
        viewModelScope.launch {
            _viewActions.consumeEach { action ->
                processAction(action)
            }
        }
    }

    protected open fun updateState(state: State) {
        _state.update { state }
        
    }

    protected fun sendEvent(event: Event) = viewModelScope.launch {
        _events.send(null)
        _events.send(event)
    }

    fun applyAction(action: Action) = viewModelScope.launch {
        _viewActions.send(action)
    }
}