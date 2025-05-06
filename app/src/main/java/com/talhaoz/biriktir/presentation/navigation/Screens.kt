package com.talhaoz.biriktir.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title: String? = null, val icon: ImageVector? = null, val route: String) {

    // Bottom Nav Screens
    data object BottomBarSavings : Screen("Birikimlerim", Icons.Default.Home, "savings")
    data object BottomBarCreateNewGoal : Screen("Yeni Hedef", Icons.Default.Add, "create_new_goal")
    data object BottomBarProfile : Screen("Hesabim", Icons.Default.Person, "profile")

    // Other Screens
    data object AddSavingEntry : Screen(route = "add_saving_entry/{goalId}/{currencyType}") {
        fun createRoute(goalId: Int, currencyType: String) = "add_saving_entry/$goalId/$currencyType"
    }
    data object SavingGoalDetail : Screen(route = "saving_goal_detail/{goalId}") {
        fun createRoute(goalId: Int) = "saving_goal_detail/$goalId"
    }
}



