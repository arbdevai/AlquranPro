package com.arbani.alquranpro.data

/**
 * Offline repository for Tahlil and Prayer schedule.
 * All Quran data has been migrated to [QuranRepository] with real EQuran v2 bundle.
 */
object OfflineRepository {

    val tahlilData = listOf(
        TahlilItem(
            1,
            "Pengantar Al-Fatihah",
            "إِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى مُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ وَآلِهِ وَأَزْوَاجِهِ وَذُرِّيَّاتِهِ وَأَهْلِ بَيْتِهِ الْكِرَامِ، شَيْءٌ لِلَّهِ لَهُمُ الْفَاتِحَةُ",
            "Ila hadratin-nabiyyil-mustafa Muhammadin sallallahu 'alaihi wa sallama wa alihi...",
            "Kepada yang terhormat Nabi Musthafa Muhammad SAW dan keluarga serta para sahabatnya, bacaan Al-Fatihah ini kami hadiahkan..."
        ),
        TahlilItem(
            2,
            "Pembacaan Surah Al-Ikhlas (3x)",
            "قُلْ هُوَ اللَّهُ أَحَدٌ ۚ اللَّهُ الصَّمَدُ ۚ لَمْ يَلِدْ وَلَمْ يُولَدْ ۚ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            "Qul huwallahu ahad. Allahus-samad. Lam yalid wa lam yulad. Wa lam yakul lahu kufuwan ahad.",
            "Katakanlah: Dialah Allah Yang Maha Esa. Allah tempat meminta. Tidak beranak dan tidak diperanakkan."
        ),
        TahlilItem(
            3,
            "Kalimat Tahlil (Utama)",
            "لَا إِلَٰهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
            "La ilaha illallah, Muhammadur-Rasulullah",
            "Tiada Tuhan selain Allah, Nabi Muhammad adalah utusan Allah."
        ),
        TahlilItem(
            4,
            "Istighfar & Doa Arwah",
            "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ",
            "Astaghfirullahal-'Azim alladhi la ilaha illa huwal-hayyul-qayyumu wa atubu ilaih",
            "Aku memohon ampun kepada Allah Yang Maha Agung, tiada Tuhan selain Dia Yang Maha Hidup lagi Terus-menerus mengurus makhluk-Nya."
        )
    )

    fun getTodayPrayerSchedule(): PrayerSchedule {
        return PrayerSchedule(
            subuh = "04:38",
            dhuha = "06:00",
            dzuhur = "11:58",
            ashar = "15:12",
            maghrib = "18:02",
            isya = "19:11",
            imsak = "04:28",
            nextPrayerName = "Ashar",
            nextPrayerTime = "15:12",
            countdownFormatted = "02:14:35"
        )
    }
}
