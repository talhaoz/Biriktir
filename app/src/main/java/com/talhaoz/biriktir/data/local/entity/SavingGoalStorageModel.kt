package com.talhaoz.biriktir.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.presentation.components.Currency

@Entity(tableName = "saving_goals")
data class SavingGoalStorageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val currency: Currency
) {
    internal fun toDomain() =
        SavingGoal(
            id = id,
            title = title,
            targetAmount = targetAmount,
            savedAmount = savedAmount,
            currencyType = currency
        )

    companion object {
        fun  fromDomain(
            savingGoal: SavingGoal
        ): SavingGoalStorageModel =
            SavingGoalStorageModel(
                title = savingGoal.title,
                targetAmount = savingGoal.targetAmount,
                savedAmount = savingGoal.savedAmount,
                currency = savingGoal.currencyType
            )
    }
}
