package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.repository.SavingRepository
import javax.inject.Inject

class UpdateSavedAmount @Inject constructor(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(goalId: Int, newAmount: Double) {
        repository.updateSavedAmount(goalId, newAmount)
    }
}
