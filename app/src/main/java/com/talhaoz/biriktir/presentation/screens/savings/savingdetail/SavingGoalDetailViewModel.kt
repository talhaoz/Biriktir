package com.talhaoz.biriktir.presentation.screens.savings.savingdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.domain.usecase.SavingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingGoalDetailViewModel @Inject constructor(
    private val savingUseCases: SavingUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<SavingGoalDetailState>(SavingGoalDetailState.Loading)
    val uiState: StateFlow<SavingGoalDetailState> = _uiState.asStateFlow()

    fun getGoalDetails(goalId: Int?) {
        if(goalId == null) return
        _uiState.value = SavingGoalDetailState.Loading
        viewModelScope.launch {
            savingUseCases.getGoalWithEntries(goalId)
                .catch { e ->
                    _uiState.value = SavingGoalDetailState.Error(e.message ?: "Bilinmeyen hata")
                }
                .collect { goalDetails ->
                    if(goalDetails != null){
                        _uiState.value = SavingGoalDetailState.Ready(goalDetails)
                    } else {
                        _uiState.value = SavingGoalDetailState.Error("Bilinmeyen hata")
                    }
                }
        }
    }

    fun deleteGoal(goalId: Int){
        viewModelScope.launch {
            savingUseCases.deleteGoal(goalId)
        }
    }

    fun deleteEntry(entryId: Int, goalId: Int){
        viewModelScope.launch {
            savingUseCases.deleteEntry(entryId)
            getGoalDetails(goalId)
        }
    }
}

