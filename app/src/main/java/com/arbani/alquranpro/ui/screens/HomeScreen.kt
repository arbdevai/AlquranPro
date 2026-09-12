package com.arbani.alquranpro.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.arbani.alquranpro.data.Surah
import com.arbani.alquranpro.ui.components.IOSCard
import com.arbani.alquranpro.ui.components.IOSPill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class HomeScreenMenu(val iconRes: Int, val label: String) {
    QURAN(R.drawable.ic_menu_book, "Al-Qur'an"),
    SHOLAT(R.drawable.ic_schedule, "Jadwal Sholat"),
    TASBIH(R.drawable.ic_touch_app, "Tasbih Digital"),
    TAHLIL(R.drawable.ic_auto_stories, "Tahlil & Doa")
}

private const val LAST_READ_PREFS = "reading_progress"
private const val LAST_READ_SURAH = "surah"
private const val LAST_READ_AYAH = "ayah"

fun saveLastRead(context: Context, surahNumber: Int, ayahNumber: Int) {
    val prefs: SharedPreferences = context.getSharedPreferences(LAST_READ_PREFS, Context.MODE_PRIVATE)
    prefs.edit().putInt(LAST_READ_SURAH, surahNumber).putInt(LAST_READ_AYAH, ayahNumber).apply()
}

fun loadLastRead(context: Context): Pair<Int, Int>? {
    val prefs: SharedPreferences = context.getSharedPreferences(LAST_READ_PREFS, Context.MODE_PRIVATE)
    if (!prefs.contains(LAST_READ_SURAH)) return null
    return prefs.getInt(LAST_READ_SURAH, 1) to prefs.getInt(LAST_READ_AYAH, 1)
}

@Composable
fun HomeScreen(
    onSurahClick: (Int) -> Unit,
    onTasbihClick: () -> Unit,
    onTahlilClick: () -> Unit,
    onPrayerClick: () -> Unit,
    repository: QuranRepository? = null
) {
    val context = LocalContext.current
    val repo = remember(repository) { repository ?: QuranRepository(context) }
    var searchQuery by remember { mutableStateOf("") }
    var surahs by remember { mutableStateOf<List<Surah>?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var lastRead by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(repo) {
        lastRead = withContext(Dispatchers.IO) { loadLastRead(context) }
        try {
            surahs = repo.getSurahList()
        } catch (e: Exception) {
            loadError = e.message ?: "Gagal memuat daftar surah"
        }
    }

    fun refresh() {
        scope.launch {
            loadError = null
            try {
                surahs = repo.getSurahList()
            } catch (e: Exception) {
                loadError = e.message ?: "Gagal memuat daftar surah"
            }
        }
    }

    val filteredSurahs = remember(searchQuery, surahs) {
        val list = surahs ?: emptyList()
        if (searchQuery.isBlank()) list
        else list.filter {
            it.namaLatin.contains(searchQuery, ignoreCase = true) ||
                it.nomor.toString() == searchQuery ||
                it.arti.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(bottom = 14.dp)) {
                    Text("Al-Qur'an Pro", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    Text(
                        "Ketenangan hati dalam setiap ayat",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                val lastSurah = surahs?.find { it.nomor == lastRead?.first }
                IOSCard(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    borderColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { onSurahClick(lastRead?.first ?: 1) }
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IOSPill(
                                text = if (lastRead == null) "Mulai Membaca" else "Terakhir Dibaca",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                "114 surah tersedia offline",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = lastSurah?.namaLatin ?: "Al-Fatihah",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = if (lastRead == null) "Ketuk untuk mulai membaca dari awal"
                            else "Ayat ${lastRead?.second} • ${lastSurah?.arti ?: ""}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (lastRead == null) "Mulai Membaca" else "Lanjutkan Membaca",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text("Fitur Utama", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        HomeScreenMenu.entries.forEach { menu ->
                            QuickMenuItem(
                                menu = menu,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    when (menu) {
                                        HomeScreenMenu.QURAN -> scope.launch { listState.animateScrollToItem(3) }
                                        HomeScreenMenu.SHOLAT -> onPrayerClick()
                                        HomeScreenMenu.TASBIH -> onTasbihClick()
                                        HomeScreenMenu.TAHLIL -> onTahlilClick()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Daftar Surah", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (surahs == null) "Memuat..." else "${filteredSurahs.size} Surah",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari surah (nama atau nomor)...", fontSize = 13.sp) },
                        leadingIcon = { Icon(painterResource(R.drawable.ic_search), contentDescription = null) },
                        singleLine = true,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            when {
                loadError != null -> item {
                    IOSCard(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Gagal memuat data: $loadError", fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = ::refresh) {
                                Icon(painterResource(R.drawable.ic_refresh), contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Coba Lagi")
                            }
                        }
                    }
                }
                surahs == null -> item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        repeat(6) {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(68.dp)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
                filteredSurahs.isEmpty() -> item {
                    Text(
                        "Tidak ada surah yang cocok.",
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(20.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> items(filteredSurahs, key = { it.nomor }) { surah ->
                    SurahItemRow(surah = surah, onClick = { onSurahClick(surah.nomor) })
                }
            }
        }
    }
}

@Composable
private fun QuickMenuItem(menu: HomeScreenMenu, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val container = when (menu) {
        HomeScreenMenu.QURAN -> MaterialTheme.colorScheme.primaryContainer
        HomeScreenMenu.SHOLAT -> MaterialTheme.colorScheme.secondaryContainer
        HomeScreenMenu.TASBIH, HomeScreenMenu.TAHLIL -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val content = when (menu) {
        HomeScreenMenu.QURAN -> MaterialTheme.colorScheme.onPrimaryContainer
        HomeScreenMenu.SHOLAT -> MaterialTheme.colorScheme.onSecondaryContainer
        HomeScreenMenu.TASBIH, HomeScreenMenu.TAHLIL -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    IOSCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(container, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(menu.iconRes), contentDescription = menu.label, tint = content)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(menu.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun SurahItemRow(surah: Surah, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IOSPill(text = "${surah.nomor}", modifier = Modifier.width(38.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(surah.namaLatin, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    "${surah.tempatTurun} • ${surah.jumlahAyat} ayat (${surah.arti})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(surah.nama, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
