package com.talhaoz.biriktir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.talhaoz.biriktir.data.local.ThemePreferenceDataStore
import com.talhaoz.biriktir.presentation.navigation.BottomNavigationBar
import com.talhaoz.biriktir.presentation.navigation.Screen
import com.talhaoz.biriktir.presentation.screens.addsaving.AddEntryScreen
import com.talhaoz.biriktir.presentation.screens.createnewgoal.CreateNewGoalScreenNew
import com.talhaoz.biriktir.presentation.screens.profile.ProfileScreen
import com.talhaoz.biriktir.presentation.screens.savings.SingleSavingScreen
import com.talhaoz.biriktir.presentation.screens.savings.allsavings.AllSavingsScreen
import com.talhaoz.biriktir.presentation.screens.savings.allsavings.SavingGoal
import com.talhaoz.biriktir.presentation.theme.ThemeViewModel
import com.talhaoz.biriktir.ui.theme.BiriktirTheme
import com.talhaoz.biriktir.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeViewModel = ThemeViewModel(ThemePreferenceDataStore(baseContext))
        enableEdgeToEdge()

        setContent {
            val theme by themeViewModel.themeState.collectAsState()

            MyAppTheme(theme = theme){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()

    val items = listOf(
        Screen.BottomBarSavings,
        Screen.BottomBarCreateNewGoal,
        Screen.BottomBarProfile,
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = items) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.BottomBarSavings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // BottomBar Screens
            composable(Screen.BottomBarSavings.route) {
                SingleSavingScreen(
                    onAddButtonClicked = { navController.navigate(Screen.AddToExistingSaving.route) },
                    onAllSavingsButtonClicked = { navController.navigate(Screen.AllSavings.route) }
                )
            }
            composable(Screen.BottomBarCreateNewGoal.route) { CreateNewGoalScreenNew(themeViewModel) }
            composable(Screen.BottomBarProfile.route) { ProfileScreen(
                onAvatarClick = {},
                onThemeSelected = { themeViewModel.updateTheme(it) },
                onNotificationSettingsClick = {}
            ) }

            // Other Screens
            composable(Screen.AddToExistingSaving.route) {
                AddEntryScreen(
                    onBackButtonClicked = {
                        navController.navigateUp()
                    }
                )
            }

            val savings = listOf(
                SavingGoal("Tatil", 300.0, 1000.0),
                SavingGoal("Telefon", 1500.0, 2000.0),
            )
            composable(Screen.AllSavings.route) {
                AllSavingsScreen (
                    savings = savings,
                    onAddGoalClick = {},
                    onGoalClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BiriktirTheme {
        MainScreen(themeViewModel = ThemeViewModel(ThemePreferenceDataStore(LocalContext.current)))
    }
}