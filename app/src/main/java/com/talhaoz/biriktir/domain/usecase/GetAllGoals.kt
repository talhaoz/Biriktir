package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllGoals @Inject constructor(
    private val repository: SavingRepository
) {
    operator fun invoke(): Flow<List<SavingGoal>> = flow { emit(repository.getAllGoals()) }
}

