package com.talhaoz.biriktir.data.local.converters

import androidx.room.TypeConverter
import com.talhaoz.biriktir.presentation.components.Currency

class CurrencyConverter {
    @TypeConverter
    fun fromCurrency(value: Currency): String = value.name

    @TypeConverter
    fun toCurrency(value: String): Currency = enumValueOf(value)
}
