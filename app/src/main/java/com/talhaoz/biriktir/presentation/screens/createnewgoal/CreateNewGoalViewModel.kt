package com.talhaoz.biriktir.presentation.screens.createnewgoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.domain.usecase.SavingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewGoalViewModel @Inject constructor(
    private val savingUseCases: SavingUseCases
) : ViewModel() {

    fun insertGoal(goal: SavingGoal) {
        viewModelScope.launch {
            savingUseCases.insertGoal(goal)
        }
    }
}
