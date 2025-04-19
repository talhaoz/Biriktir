package com.talhaoz.biriktir.presentation.screens.addsaving

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talhaoz.biriktir.R
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.components.CurrencyDropDown

@Composable
fun AddEntryScreen(
    onBackButtonClicked: () -> Unit // geri dönme callback’i ekledik
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCurrency = Currency.ALT
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        ) {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(id =R.drawable.arrow_back),
                    contentDescription = "Geri",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Giriş Ekle",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Tutar Girişi + Para Birimi Dropdown
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Tutar") },
                modifier = Modifier
                    .weight(1f)
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 28.sp),
                singleLine = true
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

        // Açıklama Girişi
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Açıklama") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Ekle Butonu
        Button(
            onClick = {
                // Giriş kaydedilir
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Ekle", fontSize = 16.sp, color = Color.White)
        }
    }
}




