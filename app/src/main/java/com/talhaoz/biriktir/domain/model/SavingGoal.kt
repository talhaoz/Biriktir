package com.talhaoz.biriktir.domain.model

import com.talhaoz.biriktir.presentation.components.Currency

data class SavingGoal(
    val id: Int = -1,
    val title: String,
    val currencyType: Currency,
    val savedAmount: Double,
    val targetAmount: Double
)