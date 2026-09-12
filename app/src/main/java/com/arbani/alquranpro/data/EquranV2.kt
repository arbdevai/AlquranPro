package com.arbani.alquranpro.data

/**
 * EQuran.id API v2 contract constants and endpoint helpers.
 */
object EquranV2 {
    const val BASE_URL = "https://equran.id/api/v2/"
    const val SURAT_LIST = "surat"
    fun suratDetail(nomor: Int) = "surat/$nomor"
    fun tafsir(nomor: Int) = "tafsir/$nomor"

    const val ASSET_BASE = "equran/v2"
    const val ASSET_SURAT_INDEX = "$ASSET_BASE/surat.json"
    fun assetSuratDetail(nomor: Int) = "$ASSET_BASE/surat/$nomor.json"
    fun assetTafsir(nomor: Int) = "$ASSET_BASE/tafsir/$nomor.json"
}
