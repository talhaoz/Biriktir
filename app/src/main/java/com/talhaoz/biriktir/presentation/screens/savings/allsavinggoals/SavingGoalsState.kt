package com.talhaoz.biriktir.presentation.screens.savings.allsavinggoals

import com.talhaoz.biriktir.domain.model.SavingGoal

sealed interface SavingGoalsState {
    data object Loading : SavingGoalsState

    data class Error(val message: String) : SavingGoalsState

    data class Ready(val savingGoals: List<SavingGoal>) : SavingGoalsState
}