package com.talhaoz.biriktir.domain.model

data class SavingGoalDetail(
    val goal: SavingGoal,
    val entries: List<SavingEntry>
)
