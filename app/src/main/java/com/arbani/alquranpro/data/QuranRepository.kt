package com.arbani.alquranpro.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/** Reads the validated EQuran snapshot lazily; launching the app never needs a network. */
class QuranRepository(context: Context) {
    private val assets = context.applicationContext.assets
    private val mutex = Mutex()
    private var index: List<Surah>? = null
    private val details = linkedMapOf<Int, SurahDetail>()
    private val tafsirs = linkedMapOf<Int, List<TafsirItem>>()

    suspend fun getSurahList(): List<Surah> = withContext(Dispatchers.IO) {
        mutex.withLock { loadIndex() }
    }

    suspend fun getSurahDetail(nomor: Int): SurahDetail = withContext(Dispatchers.IO) {
        require(nomor in 1..114)
        mutex.withLock {
            details[nomor] ?: EquranJsonParser.parseSurahDetail(
                readAsset(EquranV2.assetSuratDetail(nomor)), nomor,
                loadIndex().first { it.nomor == nomor }.jumlahAyat
            ).also {
                if (details.size >= 4) details.remove(details.keys.first())
                details[nomor] = it
            }
        }
    }

    suspend fun getTafsir(nomor: Int): List<TafsirItem> = withContext(Dispatchers.IO) {
        require(nomor in 1..114)
        mutex.withLock {
            tafsirs[nomor] ?: EquranJsonParser.parseTafsir(
                readAsset(EquranV2.assetTafsir(nomor)), nomor,
                loadIndex().first { it.nomor == nomor }.jumlahAyat
            ).also {
                if (tafsirs.size >= 2) tafsirs.remove(tafsirs.keys.first())
                tafsirs[nomor] = it
            }
        }
    }

    private fun loadIndex(): List<Surah> = index ?: EquranJsonParser.parseSurahList(
        readAsset(EquranV2.ASSET_SURAT_INDEX)
    ).also { index = it }

    private fun readAsset(path: String): String =
        assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() }
}
