package com.arbani.alquranpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.data.OfflineRepository
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSPill
import com.arbani.alquranpro.ui.theme.EmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(surahNumber: Int, onBack: () -> Unit) {
    val surah = OfflineRepository.surahList.find { it.nomor == surahNumber }
    if (surah == null) {
        onBack()
        return
    }
    val verses = OfflineRepository.getVerses(surah.nomor)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(surah.namaLatin, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "Surah ke-${surah.nomor} • ${surah.jumlahAyat} ayat",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
            // Header Info Card
            item {
                IOSCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = EmeraldPrimary.copy(alpha = 0.08f),
                    borderColor = EmeraldPrimary.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah.nama,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = surah.namaLatin,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "(${surah.arti}) • Diturunkan di ${surah.tempatTurun}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Verse List
            items(verses) { verse ->
                IOSCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IOSPill(
                                text = "Ayat ${verse.nomorAyat}",
                                backgroundColor = EmeraldPrimary.copy(alpha = 0.12f),
                                contentColor = EmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Arabic Verse
                        Text(
                            text = verse.teksArab,
                            fontSize = 26.sp,
                            lineHeight = 42.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Latin
                        Text(
                            text = verse.teksLatin,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Translation
                        Text(
                            text = verse.teksIndonesia,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
