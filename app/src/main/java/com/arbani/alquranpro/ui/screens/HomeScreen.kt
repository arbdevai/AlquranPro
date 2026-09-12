package com.arbani.alquranpro.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.data.OfflineRepository
import com.arbani.alquranpro.data.Surah
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSHeroCard
import com.arbani.alquranpro.ui.components.IOSPill
import com.arbani.alquranpro.ui.theme.EmeraldPrimary
import com.arbani.alquranpro.ui.theme.IOSGradients

enum class HomeScreenMenu(val icon: String, val label: String) {
    QURAN("📖", "Al-Qur'an"),
    SHOLAT("🕌", "Jadwal Sholat"),
    TASBIH("📿", "Tasbih Digital"),
    TAHLIL("🤲", "Tahlil & Doa")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSurahClick: (Int) -> Unit,
    onTasbihClick: () -> Unit,
    onTahlilClick: () -> Unit,
    onPrayerClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val prayer = remember { OfflineRepository.getTodayPrayerSchedule() }
    val surahs = remember { OfflineRepository.surahList }
    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) surahs
        else surahs.filter {
            it.namaLatin.contains(searchQuery, ignoreCase = true) ||
            it.nomor.toString() == searchQuery ||
            it.arti.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Al-Qur'an Pro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Ketenangan hati dalam setiap ayat",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Hero Card 1: Last Read Card
            item {
                IOSHeroCard(
                    brush = IOSGradients.HeroEmerald,
                    onClick = { onSurahClick(1) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IOSPill(
                            text = "Terakhir Dibaca",
                            backgroundColor = Color.White.copy(alpha = 0.25f),
                            contentColor = Color.White
                        )
                        IOSPill(
                            text = "100% Offline",
                            backgroundColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Al-Fatihah",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Ayat 1 dari 7 • Pembukaan",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lanjutkan Membaca ›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCD34D)
                        )
                    }
                }
            }

            // Hero Card 2: Next Prayer Countdown Card
            item {
                IOSHeroCard(
                    brush = IOSGradients.HeroPrayerNight,
                    onClick = onPrayerClick
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IOSPill(
                            text = "Jadwal Sholat",
                            backgroundColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                        Text(
                            text = "Jakarta (WIB)",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Menuju ${prayer.nextPrayerName} (${prayer.nextPrayerTime})",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "-${prayer.countdownFormatted}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Quick Menu Grid 4 items
            item {
                Text(
                    text = "Fitur Utama",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeScreenMenu.entries.forEach { menu ->
                        QuickMenuItem(
                            menu = menu,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                when (menu) {
                                    HomeScreenMenu.QURAN -> onSurahClick(1)
                                    HomeScreenMenu.SHOLAT -> onPrayerClick()
                                    HomeScreenMenu.TASBIH -> onTasbihClick()
                                    HomeScreenMenu.TAHLIL -> onTahlilClick()
                                }
                            }
                        )
                    }
                }
            }

            // Search Bar & Surah List Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Surah",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredSurahs.size} Surah",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari surah (nama atau nomor)...", fontSize = 13.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            items(filteredSurahs) { surah ->
                SurahItemCard(
                    surah = surah,
                    onClick = { onSurahClick(surah.nomor) }
                )
            }
        }
    }
}

@Composable
private fun QuickMenuItem(
    menu: HomeScreenMenu,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "quick_menu_scale"
    )
    val view = LocalView.current

    IOSCard(
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(18.dp),
        onClick = {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = menu.icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = menu.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun SurahItemCard(
    surah: Surah,
    onClick: () -> Unit
) {
    IOSCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah number pill
            IOSPill(
                text = "${surah.nomor}",
                backgroundColor = EmeraldPrimary.copy(alpha = 0.12f),
                contentColor = EmeraldPrimary,
                modifier = Modifier.width(38.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.namaLatin,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "${surah.tempatTurun} • ${surah.jumlahAyat} ayat (${surah.arti})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = surah.nama,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = EmeraldPrimary
            )
        }
    }
}
