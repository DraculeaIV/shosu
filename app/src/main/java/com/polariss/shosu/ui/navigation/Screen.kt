package com.polariss.shosu.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Map : Screen("map")
    object Records : Screen("records")
    object Lore : Screen("lore")
    object Execution : Screen("execution")
    object Warden : Screen("warden")
}
