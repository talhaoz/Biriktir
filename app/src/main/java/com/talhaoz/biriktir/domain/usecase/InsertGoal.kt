package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.domain.repository.SavingRepository
import javax.inject.Inject

class InsertGoal @Inject constructor(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(goal: SavingGoal) = repository.insertGoal(goal)
}

