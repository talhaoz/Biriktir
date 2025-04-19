package com.talhaoz.biriktir.presentation.screens.savings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talhaoz.biriktir.presentation.components.CircularProgressBar

data class Entry(
    val date: String,
    val amount: String
)

@Composable
fun SingleSavingScreen(
    onAddButtonClicked: () -> Unit,
    onAllSavingsButtonClicked: () -> Unit
) {
    val entries = listOf(
        Entry("24 April 2024", "100 EUR"),
        Entry("23 April 2024", "50 EUR"),
        Entry("24 April 2024", "100 EUR"),
        Entry("23 April 2024", "50 EUR"),
        Entry("24 April 2024", "100 EUR"),
        Entry("23 April 2024", "50 EUR"),
    )

    val currentAmount = 4000
    val targetAmount = 5000
    val progress = currentAmount.toFloat() / targetAmount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Biriktir",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onAllSavingsButtonClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "List",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyColumn{
            item {
                Spacer(modifier = Modifier.height(20.dp))
                CurrentSavingProgress(
                    progress = progress,
                    currentAmount = currentAmount,
                    targetAmount = targetAmount,
                    onAddButtonClicked = onAddButtonClicked
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Kayıtlarım",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(entries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = entry.date, fontSize = 14.sp)
                        Text(
                            text = entry.amount,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentSavingProgress(
    progress: Float,
    currentAmount: Int,
    targetAmount: Int,
    onAddButtonClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Birikim Durumu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            CircularProgressBar(
                percentage = progress,
                amountText = "$currentAmount EUR",
                totalText = "$targetAmount EUR",
                primaryColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onAddButtonClicked()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("+ Giriş Ekle", fontSize = 16.sp)
            }
        }
    }
}

