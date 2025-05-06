package com.talhaoz.biriktir.presentation.screens.addsavingentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.usecase.SavingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSavingEntryViewModel @Inject constructor(
    private val savingUseCases: SavingUseCases
) : ViewModel() {

    fun insertEntry(savingEntry: SavingEntry) {
        viewModelScope.launch {
            savingUseCases.getAllGoals().collect{ goals ->
                var savedAmount = goals.find { it.id == savingEntry.goalId }?.savedAmount
                savedAmount = savedAmount?.plus(savingEntry.amount)
                if(savedAmount != null)
                    savingUseCases.updateSavedAmount(
                        goalId = savingEntry.goalId,
                        newAmount = savedAmount
                    )
            }
            savingUseCases.insertEntry(savingEntry)
        }
    }
}

