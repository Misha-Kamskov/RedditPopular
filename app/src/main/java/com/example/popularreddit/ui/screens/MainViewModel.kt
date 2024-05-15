package com.example.popularreddit.ui.screens

import com.example.mvi.MviViewModel
import com.example.mvi.states.AbstractAction
import com.example.mvi.states.AbstractEvent
import com.example.mvi.states.AbstractState

class MainViewModel : MviViewModel<MainState, MainEvent, MainAction>() {
    override val initialState: MainState
        get() = MainState()

    override fun processAction(action: MainAction) {
        when (action) {
            else -> {}
        }
    }
}


class MainState() : AbstractState()

sealed class MainEvent : AbstractEvent() {

}

sealed class MainAction : AbstractAction() {

}