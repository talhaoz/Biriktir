package com.talhaoz.biriktir.presentation.screens.savings.savingdetail

import com.talhaoz.biriktir.data.local.entity.SavingGoalWithEntriesStorageModel
import com.talhaoz.biriktir.domain.model.SavingGoalDetail

sealed interface SavingGoalDetailState {
    data object Loading : SavingGoalDetailState

    data class Error(val message: String) : SavingGoalDetailState

    data class Ready(val savingGoalDetails: SavingGoalDetail) : SavingGoalDetailState
}
