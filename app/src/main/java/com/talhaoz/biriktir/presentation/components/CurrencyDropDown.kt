package com.talhaoz.biriktir.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencyDropDown(
    isDisabled: Boolean = false,
    selectedCurrency: Currency = Currency.TRY,
    onSelectOfCurrency: (Currency) -> Unit
) {
    val currencyOptions = setOf(Currency.TRY,Currency.ALT, Currency.EUR,Currency.USD,Currency.GBP)
    var expanded = remember { mutableStateOf(false) }
    var _selectedCurrency = remember { mutableStateOf(selectedCurrency) }

    Box(
        modifier = Modifier
            .height(56.dp)
            .width(80.dp)
            .background(Color(0xFFEDEDED), RoundedCornerShape(12.dp))
            .clickable { expanded.value = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = _selectedCurrency.value.code,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if(!isDisabled){
                Icon(
                    imageVector = if (expanded.value) Icons.Default.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded.value && !isDisabled,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.width(80.dp)
        ) {
            currencyOptions.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency.code) },
                    onClick = {
                        _selectedCurrency.value = currency
                        onSelectOfCurrency(currency)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

enum class Currency(val code: String, val symbol: String){
    TRY("TRY", "₺"),
    ALT("ALT","gram "),
    EUR("EUR","€"),
    USD("USD","$"),
    GBP("GBP","£")
}
