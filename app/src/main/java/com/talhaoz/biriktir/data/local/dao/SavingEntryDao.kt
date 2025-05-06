package com.talhaoz.biriktir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.talhaoz.biriktir.data.local.entity.SavingEntryStorageModel

@Dao
interface SavingEntryDao {
    @Query("SELECT * FROM saving_entries WHERE id = :entryId LIMIT 1")
    suspend fun getEntryById(entryId: Int): SavingEntryStorageModel?

    @Query("SELECT * FROM saving_entries WHERE goalId = :goalId ORDER BY date DESC")
    fun getEntries(goalId: Int): List<SavingEntryStorageModel>

    @Insert
    suspend fun insertEntry(entry: SavingEntryStorageModel): Long

    @Query("DELETE FROM saving_entries WHERE id = :entryId")
    suspend fun deleteEntryById(entryId: Int)

    @Query("UPDATE saving_goals SET savedAmount = savedAmount - :amount WHERE id = :goalId")
    suspend fun decreaseGoalAmount(goalId: Int, amount: Double)

    @Transaction
    suspend fun deleteEntryAndUpdateGoal(entryId: Int) {
        val entry = getEntryById(entryId)
        if (entry != null) {
            deleteEntryById(entryId)
            decreaseGoalAmount(entry.goalId, entry.amount)
        }
    }
}
