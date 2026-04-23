package com.polariss.shosu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.polariss.shosu.ui.screens.*

@Composable
fun ShosuNavHost(
    navController: NavHostController,
    onSettingsClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { screen -> navController.navigate(screen.route) },
                onSettingsClick = onSettingsClick
            )
        }
        composable(Screen.Lore.route) { 
            LoreScreen()
        }
        composable(Screen.Execution.route) { ExecutionScreen() }
        composable(Screen.Warden.route) { WardenConsoleScreen() }
    }
}
