package com.talhaoz.biriktir.presentation.screens.createnewgoal

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.presentation.components.ConfirmationDialog
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.components.CurrencyDropDown
import java.util.Calendar

@Composable
fun CreateNewGoalScreenNew(
    viewModel: CreateNewGoalViewModel = hiltViewModel(),
    onGoalCreated: () -> Unit
) {
    var goalName by remember { mutableStateOf("") }
    var goalAmount by remember { mutableStateOf("") }
    var goalDate by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf(Currency.TRY) }
    var currencySymbol by remember { mutableStateOf(selectedCurrency.symbol) }

    var showDialog by remember { mutableStateOf(false) }

    // Error
    var showErrorName by remember { mutableStateOf(false) }
    var showErrorAmount by remember { mutableStateOf(false) }
    var showErrorDate by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val interactionSource = remember { MutableInteractionSource() }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, selectedDay ->
            goalDate = "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
            showErrorDate = false
        },
        year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Birikim Hedefi\nOluştur",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(text = "Hedef Adı", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = goalName,
                onValueChange = {
                    goalName = it
                    showErrorName = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = showErrorName
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Hedef Tutar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            goalAmount = newValue
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

                CurrencyDropDown {
                    selectedCurrency = it
                    currencySymbol = it.symbol
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Hedef Tarihi", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = goalDate,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    datePickerDialog.show()
                                }
                            }
                        }
                    },
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("GG.AA.YYYY") },
                isError = showErrorDate
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if(goalName.isBlank())
                    showErrorName = true
                if(goalAmount.isBlank())
                    showErrorAmount = true
                if(goalDate.isBlank())
                    showErrorDate = true

                if(goalName.isNotBlank() && goalName.isNotBlank() && goalDate.isNotBlank())
                    showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Oluştur", fontSize = 16.sp, color = Color.White)
        }

        if(showDialog){
            ConfirmationDialog(
                title = "Hedefi Kaydet",
                description = "Bu hedefi oluşturmak istediğine emin misin?",
                onConfirmClicked = {
                    viewModel.insertGoal(
                        SavingGoal(
                            title = goalName,
                            savedAmount = 0.0,
                            targetAmount = goalAmount.toDouble(),
                            currencyType = selectedCurrency
                        )
                    )
                    showDialog = false
                    onGoalCreated()
                },
                onCancelClicked = {
                    showDialog = false
                }
            )
        }
    }
}





