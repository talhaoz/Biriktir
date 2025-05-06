package com.talhaoz.biriktir.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.talhaoz.biriktir.domain.model.SavingGoalDetail

data class SavingGoalWithEntriesStorageModel(
    @Embedded val goal: SavingGoalStorageModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "goalId"
    )
    val entries: List<SavingEntryStorageModel>
) {
    internal fun toDomain() =
        SavingGoalDetail(
            goal = goal.toDomain(),
            entries = entries.map { it.toDomain() }
        )

}

