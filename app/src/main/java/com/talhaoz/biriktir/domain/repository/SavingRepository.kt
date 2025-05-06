package com.talhaoz.biriktir.domain.repository

import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.data.local.entity.SavingGoalWithEntriesStorageModel
import com.talhaoz.biriktir.domain.model.SavingGoalDetail

interface SavingRepository {

    suspend fun getAllGoals(): List<SavingGoal>

    suspend fun insertGoal(goal: SavingGoal)

    suspend fun deleteGoal(goalId: Int)

    suspend fun getGoalWithEntries(goalId: Int): SavingGoalDetail?

    suspend fun insertEntry(entry: SavingEntry)

    suspend fun updateSavedAmount(goalId: Int, amount: Double)

    suspend fun deleteEntry(entryId: Int)
}
