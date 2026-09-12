package com.arbani.alquranpro.data

/**
 * EQuran.id API v2 contract documentation + lightweight offline bundle loader.
 *
 * Endpoints (https://equran.id/api/v2/):
 *  - GET /surat                -> list of 114 surahs
 *  - GET /surat/{nomor}        -> detail surah + ayat[] + audio per ayat
 *  - GET /tafsir/{nomor}       -> tafsir per surah (on-demand)
 *
 * Offline-first strategy (implemented via cached JSON bundles):
 *  1. Ship [assets/seed/surah_index.json] for instant cold-start + search.
 *  2. Verse lookup checks bundled [assets/seed/verses_{nomor}.json] first.
 *  3. Network fetch (Retrofit/Ktor in later milestone) only when cache misses.
 *
 * This keeps the critical path dependency-free (no new Gradle deps) so the
 * current CI build (Kotlin 17 toolchain) keeps compiling while the data
 * layer grows incrementally.
 */
object EquranV2 {
    const val BASE_URL = "https://equran.id/api/v2/"
    const val SURAT_LIST = "surat"
    fun suratDetail(nomor: Int) = "surat/$nomor"
    fun tafsir(nomor: Int) = "tafsir/$nomor"

    const val SEED_DIR = "seed"
    const val SURAH_INDEX_ASSET = "$SEED_DIR/surah_index.json"
    fun verseAsset(nomor: Int) = "$SEED_DIR/verses_$nomor.json"

    const val FEATURES_SURAT_LITE = true
    const val FEATURES_VERSUS_AD LIB = true
}
