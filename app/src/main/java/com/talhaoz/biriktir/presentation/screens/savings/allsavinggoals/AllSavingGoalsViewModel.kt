package com.talhaoz.biriktir.presentation.screens.savings.allsavinggoals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.domain.usecase.SavingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllSavingGoalsViewModel @Inject constructor(
    private val savingUseCases: SavingUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<SavingGoalsState>(SavingGoalsState.Loading)
    val uiState: StateFlow<SavingGoalsState> = _uiState.asStateFlow()

    fun getAllGoals() {
        _uiState.value = SavingGoalsState.Loading
        viewModelScope.launch {
            savingUseCases.getAllGoals()
                .catch { e ->
                    _uiState.value = SavingGoalsState.Error(e.message ?: "Bilinmeyen hata")
                }
                .collect { goals ->
                    _uiState.value = SavingGoalsState.Ready(goals)
                }
        }
    }
}
