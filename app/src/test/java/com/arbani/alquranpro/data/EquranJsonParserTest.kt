package com.arbani.alquranpro.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class EquranJsonParserTest {

    private val assetDir = File("src/main/assets/equran/v2")

    @Test
    fun parseSurahList_parsesAll114Surahs() {
        val file = File(assetDir, "surat.json")
        assertTrue("surat.json must exist in assets", file.exists())
        val list = EquranJsonParser.parseSurahList(file.readText())
        assertEquals(114, list.size)
        assertEquals(1, list.first().nomor)
        assertEquals("Al-Fatihah", list.first().namaLatin)
        assertEquals(114, list.last().nomor)
        assertEquals("An-Nas", list.last().namaLatin)
    }

    @Test
    fun parseSurahDetail_parsesAlFatihahAndAnNasCorrectly() {
        val fatihah = EquranJsonParser.parseSurahDetail(
            File(assetDir, "surat/1.json").readText(),
            expectedNomor = 1,
            expectedAyatCount = 7
        )
        assertEquals(7, fatihah.ayat.size)
        assertEquals("بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ", fatihah.ayat[0].teksArab)

        val nas = EquranJsonParser.parseSurahDetail(
            File(assetDir, "surat/114.json").readText(),
            expectedNomor = 114,
            expectedAyatCount = 6
        )
        assertEquals(6, nas.ayat.size)
    }

    @Test
    fun parseTafsir_parsesAllEntriesMatchingVerseCount() {
        val tafsirFatihah = EquranJsonParser.parseTafsir(
            File(assetDir, "tafsir/1.json").readText(),
            expectedNomor = 1,
            expectedAyatCount = 7
        )
        assertEquals(7, tafsirFatihah.size)
        assertEquals(1, tafsirFatihah[0].ayat)
        assertTrue(tafsirFatihah[0].teks.isNotBlank())
    }
}
