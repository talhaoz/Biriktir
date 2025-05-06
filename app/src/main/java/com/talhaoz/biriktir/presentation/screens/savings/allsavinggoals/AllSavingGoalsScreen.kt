package com.talhaoz.biriktir.presentation.screens.savings.allsavinggoals

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.talhaoz.biriktir.domain.model.SavingGoal
import com.talhaoz.biriktir.presentation.components.EmptyStatePlaceHolder
import com.talhaoz.biriktir.presentation.components.ErrorMessageCard

@Composable
fun AllSavingsScreen(
    viewModel: AllSavingGoalsViewModel = hiltViewModel(),
    onSavingGoalClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // One time call
    LaunchedEffect(Unit) {
        viewModel.getAllGoals()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)) // Arka plan
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Text(
            text = "Birikim Hedeflerim",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1C)
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (state) {
            is SavingGoalsState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SavingGoalsState.Ready -> {
                val goals = (state as SavingGoalsState.Ready).savingGoals

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        if(goals.isEmpty()){
                            EmptyStatePlaceHolder(
                                modifier = Modifier.padding(top = 200.dp),
                                title = "Hiç birikim hedefi eklemedin.",
                                description = "Yeni bir hedef ekleyerek birikime başlayabilirsin."
                            )
                        }
                    }
                    items(goals) { goal ->
                        GoalCard(goal = goal, onClick = { onSavingGoalClick(goal.id) })
                    }
                }
            }

            is SavingGoalsState.Error -> {
                val message = (state as SavingGoalsState.Error).message
                ErrorMessageCard(
                    message = message,
                    onRetryClicked = {
                        viewModel.getAllGoals()
                    }
                )
            }
        }
    }
}

@Composable
fun GoalCard(goal: SavingGoal, onClick: () -> Unit) {
    val currencySymbol = goal.currencyType.symbol
    val savedAmount = goal.savedAmount
    val targetAmount = goal.targetAmount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = goal.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { (savedAmount / targetAmount).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFFE1E1D9),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${savedAmount.toInt()} $currencySymbol".format(goal.savedAmount),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${targetAmount.toInt()} $currencySymbol".format(goal.targetAmount),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

