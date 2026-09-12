package com.arbani.alquranpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.arbani.alquranpro.data.QuranRepository
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
    private val repository by lazy { QuranRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlquranProApp(repository)
        }
    }
}

@Composable
fun AlquranProApp(repository: QuranRepository? = null) {
    AlquranProTheme {
        var currentScreenName by rememberSaveable { mutableStateOf("home") }
        var currentSurahNumber by rememberSaveable { mutableIntStateOf(1) }

        BackHandler(enabled = currentScreenName != "home") {
            currentScreenName = "home"
        }

        when (currentScreenName) {
            "home" -> {
                HomeScreen(
                    onSurahClick = { surahNum ->
                        currentSurahNumber = surahNum
                        currentScreenName = "reader"
                    },
                    onTasbihClick = { currentScreenName = "tasbih" },
                    onTahlilClick = { currentScreenName = "tahlil" },
                    onPrayerClick = { currentScreenName = "prayer" },
                    repository = repository
                )
            }
            "reader" -> {
                ReaderScreen(
                    surahNumber = currentSurahNumber,
                    onBack = { currentScreenName = "home" },
                    repository = repository
                )
            }
            "tasbih" -> {
                TasbihScreen(onBack = { currentScreenName = "home" })
            }
            "tahlil" -> {
                TahlilScreen(onBack = { currentScreenName = "home" })
            }
            "prayer" -> {
                PrayerScreen(onBack = { currentScreenName = "home" })
            }
        }
    }
}
