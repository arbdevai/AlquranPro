package com.arbani.alquranpro.data

/**
 * Clean data models for EQuran v2 API responses and offline storage.
 */

data class Surah(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlahAyat: Int,
    val tempatTurun: String,
    val arti: String,
    val deskripsi: String = "",
    val audioFull: Map<String, String>? = null
)

data class Verse(
    val nomorAyat: Int,
    val teksArab: String,
    val teksLatin: String,
    val teksIndonesia: String,
    val audio: Map<String, String>? = null
)

data class TafsirItem(
    val ayat: Int,
    val teks: String
)

data class SurahDetail(
    val surah: Surah,
    val ayat: List<Verse>
)

data class TahlilItem(
    val id: Int,
    val judul: String,
    val arab: String,
    val latin: String,
    val arti: String
)

data class PrayerSchedule(
    val subuh: String,
    val dhuha: String,
    val dzuhur: String,
    val ashar: String,
    val maghrib: String,
    val isya: String,
    val imsak: String,
    val nextPrayerName: String,
    val nextPrayerTime: String,
    val countdownFormatted: String
)
