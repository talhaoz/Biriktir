package com.talhaoz.biriktir.presentation.screens.addsavingentry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.presentation.components.ConfirmationDialog
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.components.CurrencyDropDown
import com.talhaoz.biriktir.presentation.components.HeaderTitle

@Composable
fun AddSavingEntryScreen(
    goalId: Int,
    currencyType : Currency,
    viewModel: AddSavingEntryViewModel = hiltViewModel(),
    onBackButtonClicked: () -> Unit,
    onEntryAdded: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description: String? by remember { mutableStateOf(null) }
    var selectedCurrency = currencyType
    val currencySymbol = currencyType.symbol

    var showDialog by remember { mutableStateOf(false) }

    // Error
    var showErrorAmount by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(20.dp),
    ) {

        HeaderTitle(
            title = "Giriş Ekle",
            onBackButtonClicked = onBackButtonClicked
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        amount = newValue
                        showErrorAmount = false
                    }
                },
                trailingIcon = {
                    Text(
                        text = currencySymbol,
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = showErrorAmount
            )

            Spacer(modifier = Modifier.width(8.dp))

            CurrencyDropDown(
                isDisabled = true,
                selectedCurrency = selectedCurrency
            ) { currency ->
                selectedCurrency = currency
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = description?: "",
            label = { Text("Açıklama") },
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if(amount.isBlank())
                    showErrorAmount = true
                else
                    showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Ekle", fontSize = 16.sp, color = Color.White)
        }

        if(showDialog){
            ConfirmationDialog(
                title = "Birikimi Kaydet",
                description = "Bu birikimi kaydetmek istediğine emin misin?",
                onConfirmClicked = {
                    viewModel.insertEntry(
                        SavingEntry(
                            goalId = goalId,
                            amount = amount.toDouble(),
                            description = description,
                            date = System.currentTimeMillis()
                        )
                    )
                    showDialog = false
                    onEntryAdded()
                },
                onCancelClicked = {
                    showDialog = false
                }
            )
        }
    }
}




