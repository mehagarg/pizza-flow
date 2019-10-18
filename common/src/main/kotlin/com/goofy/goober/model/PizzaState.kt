package com.goofy.goober.model

sealed class PizzaState {

    abstract fun reduce(action: PizzaAction): PizzaState

    object UnInitialized: PizzaState() {
        override fun reduce(action: PizzaAction): PizzaState {
            return when(action) {
                PizzaAction.ShowWelcomeScreen -> WelcomeScreen

                is PizzaAction.ContinueCustomizing,
                is PizzaAction.FinishCustomizing -> this
            }
        }
    }

    object WelcomeScreen: PizzaState() {
        override  fun reduce(action: PizzaAction): PizzaState {
            return when(action) {
                is PizzaAction.ContinueCustomizing -> StillCustomizing(
                    choicesMadeSoFar = emptyList(),
                    currentQuestion = action.question
                )

                PizzaAction.ShowWelcomeScreen,
                is PizzaAction.FinishCustomizing -> this
            }
        }
    }

    data class StillCustomizing(val choicesMadeSoFar: List<String>, val currentQuestion: Question) :
        PizzaState() {
        override  fun reduce(action: PizzaAction): PizzaState {
            return when(action) {
                is PizzaAction.ContinueCustomizing -> {
                    val newChoices = action.previousChoice
                        ?.let { choicesMadeSoFar + it }
                        ?: choicesMadeSoFar
                    action.question.state(newChoices = newChoices)
                }
                is PizzaAction.FinishCustomizing -> FinishedCustomizing(result = action.result)

                PizzaAction.ShowWelcomeScreen -> this
            }
        }
    }

    data class FinishedCustomizing(val result: String) : PizzaState() {
        override  fun reduce(action: PizzaAction): PizzaState {
            return when(action) {
                // TODO: Add functionality to start over
                is PizzaAction.ContinueCustomizing,
                PizzaAction.ShowWelcomeScreen,
                is PizzaAction.FinishCustomizing -> this
            }
        }
    }
}