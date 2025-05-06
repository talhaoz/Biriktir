package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.model.SavingGoalDetail
import com.talhaoz.biriktir.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetGoalWithEntries @Inject constructor(
    private val repository: SavingRepository
) {
    operator fun invoke(goalId: Int): Flow<SavingGoalDetail?> =
        flow {
            emit(repository.getGoalWithEntries(goalId))
        }

}

