package com.arbani.alquranpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.arbani.alquranpro.ui.screens.*
import com.arbani.alquranpro.ui.theme.AlquranProTheme

sealed interface Screen {
    data object Home : Screen
    data class Reader(val surahNumber: Int) : Screen
    data object Tasbih : Screen
    data object Tahlil : Screen
    data object Prayer : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlquranProApp()
        }
    }
}

@Composable
fun AlquranProApp() {
    AlquranProTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

        when (val screen = currentScreen) {
            is Screen.Home -> {
                HomeScreen(
                    onSurahClick = { surahNum ->
                        currentScreen = Screen.Reader(surahNum)
                    },
                    onTasbihClick = {
                        currentScreen = Screen.Tasbih
                    },
                    onTahlilClick = {
                        currentScreen = Screen.Tahlil
                    },
                    onPrayerClick = {
                        currentScreen = Screen.Prayer
                    }
                )
            }
            is Screen.Reader -> {
                ReaderScreen(
                    surahNumber = screen.surahNumber,
                    onBack = { currentScreen = Screen.Home }
                )
            }
            is Screen.Tasbih -> {
                TasbihScreen(
                    onBack = { currentScreen = Screen.Home }
                )
            }
            is Screen.Tahlil -> {
                TahlilScreen(
                    onBack = { currentScreen = Screen.Home }
                )
            }
            is Screen.Prayer -> {
                PrayerScreen(
                    onBack = { currentScreen = Screen.Home }
                )
            }
        }
    }
}
