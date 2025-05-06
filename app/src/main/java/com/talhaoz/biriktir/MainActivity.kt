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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.talhaoz.biriktir.notification.SalaryReminderWorker
import com.talhaoz.biriktir.presentation.components.Currency
import com.talhaoz.biriktir.presentation.navigation.BottomNavigationBar
import com.talhaoz.biriktir.presentation.navigation.Screen
import com.talhaoz.biriktir.presentation.permission.RequestNotificationPermission
import com.talhaoz.biriktir.presentation.screens.addsavingentry.AddSavingEntryScreen
import com.talhaoz.biriktir.presentation.screens.createnewgoal.CreateNewGoalScreenNew
import com.talhaoz.biriktir.presentation.screens.profile.ProfileScreen
import com.talhaoz.biriktir.presentation.screens.savings.allsavinggoals.AllSavingsScreen
import com.talhaoz.biriktir.presentation.screens.savings.savingdetail.SavingDetailScreen
import com.talhaoz.biriktir.presentation.theme.ThemeViewModel
import com.talhaoz.biriktir.ui.theme.BiriktirTheme
import com.talhaoz.biriktir.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val theme by themeViewModel.themeState.collectAsState()

            MyAppTheme(theme = theme){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                // Permissions
                RequestNotificationPermission()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, themeViewModel: ThemeViewModel = hiltViewModel()) {
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
            modifier = modifier.padding(innerPadding)
        ) {
            // BottomBar Screens
            composable(Screen.BottomBarSavings.route) {
                AllSavingsScreen (
                    onSavingGoalClick = {
                        navController.navigate(Screen.SavingGoalDetail.createRoute(it))
                    }
                )
            }
            composable(Screen.BottomBarCreateNewGoal.route) {
                CreateNewGoalScreenNew(
                    onGoalCreated = {
                        navController.navigate(Screen.BottomBarSavings.route)
                    }
                )
            }
            composable(Screen.BottomBarProfile.route) { ProfileScreen(
                onAvatarClick = {},
                onThemeSelected = { themeViewModel.updateTheme(it) },
            ) }

            // Other Screens
            composable(
                route = Screen.SavingGoalDetail.route,
                arguments = listOf(navArgument("goalId") { type = NavType.IntType })
            ) {
                val goalId = it.arguments?.getInt("goalId") ?: return@composable
                SavingDetailScreen(
                    goalId = goalId,
                    onAddEntryButtonClicked = { currency ->
                        navController.navigate(Screen.AddSavingEntry.createRoute(goalId,currency.toString()))
                    },
                    onBackButtonClicked = { navController.navigateUp() },
                    onGoalDeleted = {
                        navController.navigate(Screen.BottomBarSavings.route)
                    }
                )
            }

            composable(
                route = Screen.AddSavingEntry.route,
                arguments = listOf(
                    navArgument("goalId") { type = NavType.IntType },
                    navArgument("currencyType") { type = NavType.StringType }
                )
            ) {
                val goalId = it.arguments?.getInt("goalId") ?: return@composable
                val currencyArg = it.arguments?.getString("currencyType")
                val currencyType = currencyArg?.let { enumValueOf<Currency>(it) } ?: Currency.TRY

                AddSavingEntryScreen(
                    goalId = goalId,
                    currencyType = currencyType,
                    onBackButtonClicked = {
                        navController.navigateUp()
                    },
                    onEntryAdded = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BiriktirTheme {
        MainScreen()
    }
}