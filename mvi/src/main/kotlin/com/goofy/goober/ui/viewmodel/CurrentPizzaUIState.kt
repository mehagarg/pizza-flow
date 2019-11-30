package com.goofy.goober.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.goofy.goober.model.PizzaAction
import com.goofy.goober.model.PizzaState
import com.goofy.goober.model.Transition

class PizzaUi(private val externalEventConsumer: (Transition) -> Unit) {

    val state: MutableLiveData<PizzaState> = MutableLiveData<PizzaState>().apply { value =
        PizzaState.UnInitialized
    }

    operator fun invoke(block: PizzaUi.() -> Unit) = block()

    fun reduce(action: PizzaAction) {
        val fromState = state.value
        fromState?.reduce(action)?.also { toState ->
            if (fromState != toState) state.value = toState

            Transition(
                fromState = fromState,
                toState = toState,
                action = action
            ).also { externalEventConsumer(it) }
        }
    }
}