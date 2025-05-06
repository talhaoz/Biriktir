package com.talhaoz.biriktir.presentation.screens.savings.savingdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.presentation.components.CircularProgressBar
import com.talhaoz.biriktir.presentation.components.ConfirmationDialog
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.components.EmptyStatePlaceHolder
import com.talhaoz.biriktir.presentation.components.ErrorMessageCard
import com.talhaoz.biriktir.presentation.components.HeaderTitle
import com.talhaoz.biriktir.presentation.screens.savings.allsavinggoals.SavingGoalsState
import com.talhaoz.biriktir.util.formatAsDate

@Composable
fun SavingDetailScreen(
    goalId: Int,
    viewModel: SavingGoalDetailViewModel = hiltViewModel(),
    onAddEntryButtonClicked: (Currency) -> Unit,
    onBackButtonClicked: () -> Unit,
    onGoalDeleted: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showDeleteGoalDialog by remember { mutableStateOf(false) }
    var showDeleteEntryDialog by remember { mutableStateOf(false) }

    var willBeDeletedEntry: SavingEntry? by remember {  mutableStateOf(null) }

    var isEditMode by remember { mutableStateOf(false) }

    // One time call
    LaunchedEffect(Unit) {
        viewModel.getGoalDetails(goalId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(20.dp)
    ) {
        when (state) {
            is SavingGoalDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SavingGoalDetailState.Ready -> {
                val goalDetails = (state as SavingGoalDetailState.Ready).savingGoalDetails

                val title = goalDetails.goal.title
                val currencyType = goalDetails.goal.currencyType
                val currencySymbol = goalDetails.goal.currencyType.symbol
                val entries = goalDetails.entries
                val currentAmount = goalDetails.entries.sumOf { it.amount }.toInt()
                val targetAmount = goalDetails.goal.targetAmount.toInt()
                val progress = currentAmount.toFloat() / targetAmount

                HeaderTitle(
                    title = title,
                    isMenuVisible = true,
                    onBackButtonClicked = onBackButtonClicked,
                    editModeSwitched = { isEditMode = it },
                    onDeleteClick = { showDeleteGoalDialog = true }
                )

                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        CurrentSavingProgress(
                            progress = progress,
                            currentAmount = currentAmount,
                            targetAmount = targetAmount,
                            currencyType = currencyType,
                            onAddButtonClicked = { onAddEntryButtonClicked(currencyType) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Kayıtlarım",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        if(entries.isEmpty()){
                            EmptyStatePlaceHolder(
                                title = "Hiç kayıt bulunamadı",
                                description = "Yeni bir kayıt ekleyerek birikiminizi başlatabilirsiniz."
                            )
                        }
                    }

                    items(entries) { entry ->
                        /*Row(
                            modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Kart
                            Card(
                                modifier = Modifier
                                    .weight(1f),
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
                                    val formattedDate = entry.date.formatAsDate()
                                    Text(text = formattedDate, fontSize = 14.sp)
                                    Text(
                                        text = "${entry.amount.toInt()} $currencySymbol",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Entry Delete Button
                            AnimatedVisibility(
                                visible = isEditMode,
                                enter = fadeIn() + slideInHorizontally(),
                                exit = fadeOut() + slideOutHorizontally()
                            ) {
                                IconButton(onClick = {
                                    willBeDeletedEntry = entry
                                    showDeleteEntryDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }*/

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Kart
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        val formattedDate = entry.date.formatAsDate()
                                        Text(text = formattedDate, fontSize = 14.sp)
                                        Text(
                                            text = "${entry.amount.toInt()} $currencySymbol",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (!entry.description.isNullOrBlank()) {
                                        var showDescriptionDialog by remember { mutableStateOf(false) }

                                        Text(
                                            text = entry.description!!,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.clickable { showDescriptionDialog = true }
                                        )

                                        if (showDescriptionDialog) {
                                            AlertDialog(
                                                onDismissRequest = { showDescriptionDialog = false },
                                                confirmButton = {
                                                    TextButton(onClick = { showDescriptionDialog = false }) {
                                                        Text("Kapat")
                                                    }
                                                },
                                                title = { Text("Açıklama") },
                                                text = { Text(entry.description!!) }
                                            )
                                        }
                                    }
                                }
                            }

                            // Silme Butonu - Animasyonlu
                            AnimatedVisibility(
                                visible = isEditMode,
                                enter = fadeIn() + slideInHorizontally(),
                                exit = fadeOut() + slideOutHorizontally()
                            ) {
                                IconButton(
                                    onClick = {
                                        willBeDeletedEntry = entry
                                        showDeleteEntryDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Sil",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }

                    }
                }
            }

            is SavingGoalDetailState.Error -> {
                val message = (state as SavingGoalsState.Error).message
                ErrorMessageCard(
                    message = message,
                    onRetryClicked = {
                        viewModel.getGoalDetails(goalId)
                    }
                )
            }
        }

        if(showDeleteGoalDialog){
            ConfirmationDialog(
                title = "Birikimi Sil",
                description = "Tüm kayıtlar silinecek. Bu birikimi silmek istediğine emin misin?",
                onConfirmClicked = {
                    viewModel.deleteGoal(goalId)
                    showDeleteGoalDialog = false
                    onGoalDeleted()
                },
                onCancelClicked = {
                    showDeleteGoalDialog = false
                }
            )
        }

        if(showDeleteEntryDialog){
            willBeDeletedEntry?.let { entry ->
                ConfirmationDialog(
                    title = "Kaydi Sil",
                    description = "Miktar: ${entry.amount.toInt()} \nTarih: ${entry.date.formatAsDate()}\n\nKaydi silmek istediğine emin misin?",
                    onConfirmClicked = {
                        viewModel.deleteEntry(entry.id, goalId)
                        showDeleteEntryDialog = false
                    },
                    onCancelClicked = {
                        showDeleteEntryDialog = false
                    }
                )
            }

        }
    }
}

@Composable
private fun CurrentSavingProgress(
    progress: Float,
    currentAmount: Int,
    targetAmount: Int,
    onAddButtonClicked: () -> Unit,
    currencyType: Currency
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
                amountText = "$currentAmount ${currencyType.symbol}",
                totalText = "$targetAmount ${currencyType.symbol}",
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
                Text("+ Birikim Ekle", fontSize = 16.sp)
            }
        }
    }
}




