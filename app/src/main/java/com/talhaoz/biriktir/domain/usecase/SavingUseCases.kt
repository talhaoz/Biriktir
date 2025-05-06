package com.talhaoz.biriktir.domain.usecase

data class SavingUseCases(
    val getAllGoals: GetAllGoals,
    val insertGoal: InsertGoal,
    val updateSavedAmount: UpdateSavedAmount,
    val deleteGoal: DeleteGoal,
    val getGoalWithEntries: GetGoalWithEntries,
    val insertEntry: InsertEntry,
    val deleteEntry: DeleteEntry
)

