package com.talhaoz.biriktir.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.talhaoz.biriktir.domain.model.SavingEntry

@Entity(
    tableName = "saving_entries",
    foreignKeys = [ForeignKey(
        entity = SavingGoalStorageModel::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["goalId"])]
)
data class SavingEntryStorageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalId: Int,
    val amount: Double,
    val description: String?,
    val date: Long
) {
    internal fun toDomain() =
        SavingEntry(
            id = id,
            goalId = goalId,
            amount = amount,
            description = description,
            date = date
        )

    companion object {
        fun  fromDomain(
            savingEntry: SavingEntry
        ): SavingEntryStorageModel =
            SavingEntryStorageModel(
                goalId = savingEntry.goalId,
                amount = savingEntry.amount,
                description = savingEntry.description,
                date = savingEntry.date
            )
    }
}
