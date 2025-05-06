package com.talhaoz.biriktir.domain.model

data class SavingEntry(
    val id: Int = -1,
    val goalId: Int,
    val amount: Double,
    val description: String?,
    val date: Long
)
