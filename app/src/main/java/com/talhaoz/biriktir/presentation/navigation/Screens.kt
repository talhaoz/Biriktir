package com.talhaoz.biriktir.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val title: String? = null, val icon: ImageVector? = null, val route: String) {

    // Bottom Nav Screens
    data object BottomBarSavings : Screen("Birikimlerim", Icons.Default.Home, "savings")
    data object BottomBarCreateNewGoal : Screen("Yeni Hedef", Icons.Default.Add, "create_new_goal")
    data object BottomBarProfile : Screen("Hesabim", Icons.Default.Person, "profile")

    // Other Screens
    data object AddToExistingSaving : Screen(route = "add_saving")
    data object AllSavings : Screen(route = "all_savings")

/*    object Detail    : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }*/
    /*object Settings  : Screen("settings")
    object Profile   : Screen("profile")*/
}



