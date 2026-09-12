package com.arbani.alquranpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.R
import com.arbani.alquranpro.data.OfflineRepository
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSHeroCard
import com.arbani.alquranpro.ui.components.IOSPill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(onBack: () -> Unit) {
    val schedule = remember { OfflineRepository.getTodayPrayerSchedule() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Sholat", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                IOSHeroCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IOSPill(text = "Jakarta (WIB)")
                        Text(
                            text = "Jadwal statis offline",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sholat Berikutnya: ${schedule.nextPrayerName}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = schedule.nextPrayerTime,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hitung mundur: -${schedule.countdownFormatted}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
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

                IOSCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = name,
                                fontSize = 16.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                            )
                            if (isCurrent) {
                                Text(
                                    text = "Waktu sholat berikutnya",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Text(text = time, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
