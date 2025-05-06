package com.talhaoz.biriktir.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.talhaoz.biriktir.data.local.entity.SavingGoalStorageModel
import com.talhaoz.biriktir.data.local.entity.SavingGoalWithEntriesStorageModel

@Dao
interface SavingGoalDao {
    @Query("SELECT * FROM saving_goals")
    suspend fun getAllGoals(): List<SavingGoalStorageModel>

    @Transaction
    @Query("SELECT * FROM saving_goals WHERE id = :goalId")
    suspend fun getGoalWithEntries(goalId: Int): SavingGoalWithEntriesStorageModel?

    @Insert
    suspend fun insertGoal(goal: SavingGoalStorageModel): Long

    @Query("UPDATE saving_goals SET savedAmount = :amount WHERE id = :goalId")
    suspend fun updateSavedAmount(goalId: Int, amount: Double)

    @Query("DELETE FROM saving_goals WHERE id = :goalId")
    suspend fun deleteGoal(goalId : Int)

}
