package com.arbani.alquranpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    MaterialTheme {
        var selected by remember { mutableStateOf<Surah?>(null) }
        if (selected == null) HomeScreen(onSurahClick = { selected = it })
        else ReaderScreen(surah = selected!!, onBack = { selected = null })
    }
}

private data class Surah(val number: Int, val name: String, val arabic: String, val verses: List<String>)

private val surahs = listOf(
    Surah(1, "Al-Fatihah", "الفاتحة", listOf("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ", "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "الرَّحْمَنِ الرَّحِيمِ", "مَالِكِ يَوْمِ الدِّينِ", "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ")),
    Surah(2, "Al-Baqarah", "البقرة", listOf("الم", "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ")),
    Surah(3, "Ali 'Imran", "آل عمران", listOf("الم", "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "نَزَّلَ عَلَيْكَ الْكِتَابَ بِالْحَقِّ مُصَدِّقًا لِّمَا بَيْنَ يَدَيْهِ")),
    Surah(18, "Al-Kahf", "الكهف", listOf("الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا", "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ")),
    Surah(36, "Ya-Sin", "يس", listOf("يس", "وَالْقُرْآنِ الْحَكِيمِ", "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ")),
    Surah(55, "Ar-Rahman", "الرحمن", listOf("الرَّحْمَٰنُ", "عَلَّمَ الْقُرْآنَ", "خَلَقَ الْإِنسَانَ", "عَلَّمَهُ الْبَيَانَ", "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ")),
    Surah(67, "Al-Mulk", "الملك", listOf("تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا")),
    Surah(112, "Al-Ikhlas", "الإخلاص", listOf("قُلْ هُوَ اللَّهُ أَحَدٌ", "اللَّهُ الصَّمَدُ", "لَمْ يَلِدْ وَلَمْ يُولَدْ", "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ")),
    Surah(113, "Al-Falaq", "الفلق", listOf("قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "مِن شَرِّ مَا خَلَقَ", "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ")),
    Surah(114, "An-Nas", "الناس", listOf("قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "مَلِكِ النَّاسِ", "إِلَٰهِ النَّاسِ", "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ"))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun HomeScreen(onSurahClick: (Surah) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = surahs.filter { it.name.contains(query, true) || it.number.toString() == query }
    Scaffold(topBar = { TopAppBar(title = { Text("AlquranPro", fontWeight = FontWeight.Bold) }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Text("Baca Al-Qur'an", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                Text("Temukan ketenangan dalam setiap ayat", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Cari surah") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp))
                Text("Daftar Surah", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
            items(filtered) { surah ->
                Card(onClick = { onSurahClick(surah) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(surah.number.toString(), modifier = Modifier.width(42.dp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Column(modifier = Modifier.weight(1f)) { Text(surah.name, fontWeight = FontWeight.SemiBold); Text("${surah.verses.size} ayat", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) }
                        Text(surah.arabic, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun ReaderScreen(surah: Surah, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(surah.name) }, navigationIcon = { IconButton(onClick = onBack) { Text("‹", fontSize = 32.sp) } }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item { Text(surah.arabic, fontSize = 30.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary); Text("Surah ${surah.number} • ${surah.verses.size} ayat", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(surah.verses.withIndex().toList()) { (index, verse) ->
                Column(modifier = Modifier.fillMaxWidth()) { Text(verse, fontSize = 27.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()); Text("${index + 1}", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp)) }
            }
            item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Kembali ke daftar surah") } }
        }
    }
}
