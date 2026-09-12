package com.arbani.alquranpro.ui.navigation

sealed class NavigationCommand(
    val route: String
) {
    data object Home : NavigationCommand("home")
    object SurahDetail : NavigationCommand("surah/{surahNumber}")

    fun createRoute(surahNumber: Int) = "surah/$surahNumber"
}