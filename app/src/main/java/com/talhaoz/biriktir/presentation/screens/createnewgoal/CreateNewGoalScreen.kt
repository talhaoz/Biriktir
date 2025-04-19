package com.talhaoz.biriktir.presentation.screens.createnewgoal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.components.CurrencyDropDown
import com.talhaoz.biriktir.presentation.theme.ThemeViewModel

@Composable
fun CreateNewGoalScreenNew(themeViewModel: ThemeViewModel) {

    val context = LocalContext.current
    var goalName by remember { mutableStateOf("") }
    var goalAmount by remember { mutableStateOf("") }
    var goalDate by remember { mutableStateOf("") }
    var selectedCurrency = Currency.TRY

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val currentTheme by themeViewModel.themeState.collectAsState()


    val interactionSource = remember { MutableInteractionSource() }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, selectedDay ->
            goalDate = "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
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
            // Hedef Adı
            Text(text = "Hedef Adı", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = goalName,
                onValueChange = { goalName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hedef Tutar
            /*Text(text = "Hedef Tutar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .width(64.dp)
                        .background(Color(0xFFEDEDED), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("EUR", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }*/
            // Hedef Tutar ve Para Birimi
            Text(text = "Hedef Tutar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))

                CurrencyDropDown {
                    selectedCurrency = it
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hedef Tarihi
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
                placeholder = { Text("GG.AA.YYYY") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Oluştur", fontSize = 16.sp, color = Color.White)
        }
    }
}



