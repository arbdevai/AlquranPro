package com.arbani.alquranpro.data

import org.json.JSONArray
import org.json.JSONObject

/**
 * Shared EQuran v2 JSON parser used by both bundled assets and optional network responses.
 * Strict: throws IllegalArgumentException on malformed or inconsistent data.
 */
object EquranJsonParser {

    private fun getAudioMap(obj: JSONObject, key: String): Map<String, String>? {
        if (!obj.has(key) || obj.isNull(key)) return null
        val audioObj = obj.optJSONObject(key) ?: return null
        val audioKeys = audioObj.keys()
        val map = mutableMapOf<String, String>()
        while (audioKeys.hasNext()) {
            val reciterKey = audioKeys.next()
            val url = audioObj.optString(reciterKey, "")
            if (url.isNotBlank()) {
                map[reciterKey] = url
            }
        }
        return map.ifEmpty { null }
    }

    fun parseSurahList(jsonText: String): List<Surah> {
        val root = JSONObject(jsonText)
        if (root.optInt("code") != 200) {
            throw IllegalArgumentException("Invalid EQuran envelope code: ${root.optInt("code")}")
        }
        val data = root.optJSONArray("data")
            ?: throw IllegalArgumentException("Missing data array in /surat response")

        val surahs = mutableListOf<Surah>()
        for (i in 0 until data.length()) {
            val item = data.getJSONObject(i)
            surahs.add(parseSurahObject(item))
        }

        if (surahs.size != 114) {
            throw IllegalArgumentException("Expected 114 surahs, got ${surahs.size}")
        }

        val numbers = surahs.map { it.nomor }.toSet()
        if (numbers.size != 114 || numbers.minOrNull() != 1 || numbers.maxOrNull() != 114) {
            throw IllegalArgumentException("Surah numbers must be unique 1..114")
        }

        return surahs.sortedBy { it.nomor }
    }

    private fun parseSurahObject(item: JSONObject): Surah {
        val nomor = item.optInt("nomor", -1)
        if (nomor < 1 || nomor > 114) {
            throw IllegalArgumentException("Invalid surah nomor: $nomor")
        }
        return Surah(
            nomor = nomor,
            nama = item.optString("nama", ""),
            namaLatin = item.optString("namaLatin", ""),
            jumlahAyat = item.optInt("jumlahAyat", -1),
            tempatTurun = item.optString("tempatTurun", ""),
            arti = item.optString("arti", ""),
            deskripsi = item.optString("deskripsi", ""),
            audioFull = getAudioMap(item, "audioFull")
        )
    }

    fun parseSurahDetail(jsonText: String, expectedNomor: Int, expectedAyatCount: Int? = null): SurahDetail {
        val root = JSONObject(jsonText)
        if (root.optInt("code") != 200) {
            throw IllegalArgumentException("Invalid EQuran envelope code")
        }
        val data = root.optJSONObject("data")
            ?: throw IllegalArgumentException("Missing data object in /surat/$expectedNomor response")

        val surah = parseSurahObject(data)

        if (surah.nomor != expectedNomor) {
            throw IllegalArgumentException("Surah number mismatch: expected $expectedNomor, got ${surah.nomor}")
        }

        val ayatArray: JSONArray = data.optJSONArray("ayat")
            ?: throw IllegalArgumentException("Missing ayat array for surah $expectedNomor")

        if (ayatArray.length() != surah.jumlahAyat) {
            throw IllegalArgumentException(
                "Surah $expectedNomor verse count mismatch: expected ${surah.jumlahAyat}, got ${ayatArray.length()}"
            )
        }

        if (expectedAyatCount != null && ayatArray.length() != expectedAyatCount) {
            throw IllegalArgumentException("Surah $expectedNomor does not match index count $expectedAyatCount")
        }

        val verses = mutableListOf<Verse>()
        for (i in 0 until ayatArray.length()) {
            val v = ayatArray.getJSONObject(i)
            val nomorAyat = v.optInt("nomorAyat", -1)
            if (nomorAyat != i + 1) {
                throw IllegalArgumentException("Surah $expectedNomor verse index mismatch at position ${i + 1}")
            }
            val teksArab = v.optString("teksArab", "")
            val teksLatin = v.optString("teksLatin", "")
            val teksIndonesia = v.optString("teksIndonesia", "")
            if (teksArab.isBlank() || teksLatin.isBlank() || teksIndonesia.isBlank()) {
                throw IllegalArgumentException("Surah $expectedNomor verse $nomorAyat has blank text fields")
            }
            verses.add(
                Verse(
                    nomorAyat = nomorAyat,
                    teksArab = teksArab,
                    teksLatin = teksLatin,
                    teksIndonesia = teksIndonesia,
                    audio = getAudioMap(v, "audio")
                )
            )
        }

        return SurahDetail(surah = surah, ayat = verses)
    }

    fun parseTafsir(jsonText: String, expectedNomor: Int, expectedAyatCount: Int? = null): List<TafsirItem> {
        val root = JSONObject(jsonText)
        if (root.optInt("code") != 200) {
            throw IllegalArgumentException("Invalid EQuran envelope code for tafsir")
        }
        val data = root.optJSONObject("data")
            ?: throw IllegalArgumentException("Missing data object in /tafsir/$expectedNomor response")

        if (data.optInt("nomor", -1) != expectedNomor) {
            throw IllegalArgumentException("Tafsir surah number mismatch: expected $expectedNomor")
        }

        val tafsirArray: JSONArray = data.optJSONArray("tafsir")
            ?: throw IllegalArgumentException("Missing tafsir array for surah $expectedNomor")

        val items = mutableListOf<TafsirItem>()
        for (i in 0 until tafsirArray.length()) {
            val t = tafsirArray.getJSONObject(i)
            val ayat = t.optInt("ayat", -1)
            val teks = t.optString("teks", "")
            if (ayat < 1) {
                throw IllegalArgumentException("Surah $expectedNomor tafsir has invalid ayat number")
            }
            if (teks.isBlank()) {
                throw IllegalArgumentException("Surah $expectedNomor tafsir ayat $ayat has blank text")
            }
            items.add(TafsirItem(ayat = ayat, teks = teks))
        }

        val sorted = items.sortedBy { it.ayat }
        if (expectedAyatCount != null && sorted.size != expectedAyatCount) {
            throw IllegalArgumentException(
                "Surah $expectedNomor tafsir size mismatch: expected $expectedAyatCount, got ${sorted.size}"
            )
        }
        return sorted
    }
}
