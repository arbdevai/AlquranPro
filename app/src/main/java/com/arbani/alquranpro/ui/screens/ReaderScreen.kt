package com.arbani.alquranpro.ui.screens

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbani.alquranpro.R
import com.arbani.alquranpro.data.QuranRepository
import com.arbani.alquranpro.data.SurahDetail
import com.arbani.alquranpro.data.TafsirItem
import com.arbani.alquranpro.data.Verse
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSPill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReaderScreen(
    surahNumber: Int,
    onBack: () -> Unit,
    repository: QuranRepository? = null
) {
    val context = LocalContext.current
    val repo = remember(repository) { repository ?: QuranRepository(context) }
    var detail by remember { mutableStateOf<SurahDetail?>(null) }
    var tafsirMap by remember { mutableStateOf<Map<Int, String>?>(null) }
    var expandedAyah by remember { mutableStateOf<Int?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var playingAyah by remember { mutableStateOf<Int?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    LaunchedEffect(surahNumber, repo) {
        loadError = null
        try {
            val d = repo.getSurahDetail(surahNumber)
            detail = d
            withContext(Dispatchers.IO) {
                saveLastRead(context, surahNumber, 1)
            }
        } catch (e: Exception) {
            loadError = e.message ?: "Gagal memuat surah $surahNumber"
        }
    }

    fun toggleTafsir(ayatNumber: Int) {
        if (expandedAyah == ayatNumber) {
            expandedAyah = null
            return
        }
        expandedAyah = ayatNumber
        if (tafsirMap == null) {
            scope.launch {
                try {
                    val tafsirList = repo.getTafsir(surahNumber)
                    tafsirMap = tafsirList.associate { it.ayat to it.teks }
                } catch (_: Exception) {
                    tafsirMap = emptyMap()
                }
            }
        }
    }

    fun playAudio(ayahNumber: Int, url: String?) {
        if (url.isNullOrBlank()) return
        if (playingAyah == ayahNumber) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            playingAyah = null
            return
        }
        try {
            mediaPlayer?.release()
            val mp = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    playingAyah = ayahNumber
                }
                setOnCompletionListener {
                    playingAyah = null
                    it.release()
                    mediaPlayer = null
                }
                setOnErrorListener { _, _, _ ->
                    playingAyah = null
                    true
                }
                prepareAsync()
            }
            mediaPlayer = mp
        } catch (_: Exception) {
            playingAyah = null
        }
    }

    Scaffold { padding ->
        when {
            loadError != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Kembali")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kembali", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Terjadi kesalahan:", fontWeight = FontWeight.Bold)
                    Text(loadError!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onBack) {
                        Text("Kembali")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            detail == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Kembali")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memuat surah $surahNumber...", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            else -> {
                val surah = detail!!.surah
                val verses = detail!!.ayat

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "Kembali")
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(surah.namaLatin, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(
                                    "Surah ke-${surah.nomor} • ${surah.jumlahAyat} ayat",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        IOSCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                            borderColor = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = surah.nama,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = surah.namaLatin,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "(${surah.arti}) • Diturunkan di ${surah.tempatTurun}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    items(verses, key = { it.nomorAyat }) { verse ->
                        val isExpanded = expandedAyah == verse.nomorAyat
                        val isPlaying = playingAyah == verse.nomorAyat
                        val audioUrl = verse.audio?.get("05") ?: verse.audio?.values?.firstOrNull()

                        IOSCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IOSPill(
                                        text = "Ayat ${verse.nomorAyat}",
                                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (audioUrl != null) {
                                            IconButton(onClick = { playAudio(verse.nomorAyat, audioUrl) }) {
                                                Icon(
                                                    painter = painterResource(if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play_arrow),
                                                    contentDescription = if (isPlaying) "Hentikan audio" else "Putar audio",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                        TextButton(onClick = { toggleTafsir(verse.nomorAyat) }) {
                                            Text("Tafsir", fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                painter = painterResource(if (isExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = verse.teksArab,
                                    fontSize = 26.sp,
                                    lineHeight = 42.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = verse.teksLatin,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = verse.teksIndonesia,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp)
                                    ) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Tafsir Kemenag RI (Ayat ${verse.nomorAyat})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val tafsirText = tafsirMap?.get(verse.nomorAyat)
                                        if (tafsirText == null) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Memuat tafsir...", fontSize = 12.sp)
                                            }
                                        } else {
                                            Text(tafsirText, fontSize = 12.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
