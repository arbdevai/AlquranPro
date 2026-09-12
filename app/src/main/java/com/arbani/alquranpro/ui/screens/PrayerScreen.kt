package com.arbani.alquranpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.data.OfflineRepository
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSHeroCard
import com.arbani.alquranpro.ui.components.IOSPill
import com.arbani.alquranpro.ui.theme.EmeraldPrimary
import com.arbani.alquranpro.ui.theme.IOSGradients

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(onBack: () -> Unit) {
    val schedule = OfflineRepository.getTodayPrayerSchedule()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Sholat", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("‹", fontSize = 32.sp, fontWeight = FontWeight.Light)
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                IOSHeroCard(
                    brush = IOSGradients.HeroPrayerNight
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IOSPill(
                            text = "Jakarta & Sekitarnya (WIB)",
                            backgroundColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                        Text(
                            text = "Offline Mode",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sholat Berikutnya: ${schedule.nextPrayerName}",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = schedule.nextPrayerTime,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hitung mundur: -${schedule.countdownFormatted}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFCD34D)
                    )
                }
            }

            val prayerList = listOf(
                "Imsak" to schedule.imsak,
                "Subuh" to schedule.subuh,
                "Dhuha" to schedule.dhuha,
                "Dzuhur" to schedule.dzuhur,
                "Ashar" to schedule.ashar,
                "Maghrib" to schedule.maghrib,
                "Isya" to schedule.isya
            )

            items(prayerList.size) { index ->
                val (name, time) = prayerList[index]
                val isCurrent = name == schedule.nextPrayerName

                IOSCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = if (isCurrent) EmeraldPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    borderColor = if (isCurrent) EmeraldPrimary else MaterialTheme.colorScheme.outline
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = name,
                                fontSize = 16.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            if (isCurrent) {
                                Text(
                                    text = "Waktu sholat berikutnya",
                                    fontSize = 11.sp,
                                    color = EmeraldPrimary
                                )
                            }
                        }

                        Text(
                            text = time,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
