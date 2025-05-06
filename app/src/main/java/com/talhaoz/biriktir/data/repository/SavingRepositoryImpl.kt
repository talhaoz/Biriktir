package com.talhaoz.biriktir.data.repository

import com.talhaoz.biriktir.data.local.dao.SavingEntryDao
import com.talhaoz.biriktir.data.local.dao.SavingGoalDao
import com.talhaoz.biriktir.data.local.entity.SavingEntryStorageModel
import com.talhaoz.biriktir.data.local.entity.SavingGoalStorageModel
import com.talhaoz.biriktir.data.local.entity.SavingGoalWithEntriesStorageModel
import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.domain.model.SavingGoalDetail
import com.talhaoz.biriktir.domain.repository.SavingRepository
import javax.inject.Inject


class SavingRepositoryImpl @Inject constructor(
    private val savingGoalDao: SavingGoalDao,
    private val savingEntryDao: SavingEntryDao
) : SavingRepository {

    override suspend fun getAllGoals(): List<SavingGoal> =
        savingGoalDao.getAllGoals().map { it.toDomain() }

    override suspend fun insertGoal(goal: SavingGoal) {
        savingGoalDao.insertGoal(SavingGoalStorageModel.fromDomain(goal))
    }

    override suspend fun updateSavedAmount(goalId: Int, amount: Double) {
        savingGoalDao.updateSavedAmount(goalId, amount)
    }

    override suspend fun deleteGoal(goalId: Int) {
        savingGoalDao.deleteGoal(goalId)
    }

    override suspend fun getGoalWithEntries(goalId: Int): SavingGoalDetail? =
        savingGoalDao.getGoalWithEntries(goalId)?.toDomain()

    override suspend fun insertEntry(entry: SavingEntry) {
        savingEntryDao.insertEntry(SavingEntryStorageModel.fromDomain(entry))
    }

    override suspend fun deleteEntry(entryId: Int) {
        savingEntryDao.deleteEntryAndUpdateGoal(entryId)
    }
}

