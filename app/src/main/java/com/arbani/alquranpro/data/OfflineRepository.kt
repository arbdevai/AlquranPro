package com.arbani.alquranpro.data

data class Surah(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlahAyat: Int,
    val tempatTurun: String,
    val arti: String,
    val deskripsi: String = ""
)

data class Verse(
    val nomorAyat: Int,
    val teksArab: String,
    val teksLatin: String,
    val teksIndonesia: String
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

object OfflineRepository {

    val surahList = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", 7, "Mekah", "Pembukaan"),
        Surah(2, "البقرة", "Al-Baqarah", 286, "Madinah", "Sapi Betina"),
        Surah(3, "آل عمران", "Ali 'Imran", 200, "Madinah", "Keluarga Imran"),
        Surah(4, "النساء", "An-Nisa'", 176, "Madinah", "Wanita"),
        Surah(5, "المائدة", "Al-Ma'idah", 120, "Madinah", "Hidangan"),
        Surah(6, "الأنعام", "Al-An'am", 165, "Mekah", "Binatang Ternak"),
        Surah(7, "الأعراف", "Al-A'raf", 206, "Mekah", "Tempat Tertinggi"),
        Surah(8, "الأنفال", "Al-Anfal", 75, "Madinah", "Rampasan Perang"),
        Surah(9, "التوبة", "At-Tawbah", 129, "Madinah", "Pengampunan"),
        Surah(10, "يونس", "Yunus", 109, "Mekah", "Nabi Yunus"),
        Surah(18, "الكهف", "Al-Kahf", 110, "Mekah", "Gua"),
        Surah(36, "يس", "Ya-Sin", 83, "Mekah", "Ya-Sin"),
        Surah(55, "الرحمن", "Ar-Rahman", 78, "Madinah", "Yang Maha Pemurah"),
        Surah(56, "الواقعة", "Al-Waqi'ah", 96, "Mekah", "Hari Kiamat"),
        Surah(67, "الملك", "Al-Mulk", 30, "Mekah", "Kerajaan"),
        Surah(112, "الإخلاص", "Al-Ikhlas", 4, "Mekah", "Murni / Memurnikan"),
        Surah(113, "الفلق", "Al-Falaq", 5, "Mekah", "Waktu Subuh"),
        Surah(114, "الناس", "An-Nas", 6, "Mekah", "Manusia")
    )

    fun getVerses(surahNumber: Int): List<Verse> {
        return when (surahNumber) {
            1 -> listOf(
                Verse(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang."),
                Verse(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-hamdu lillahi rabbil-'alamin", "Segala puji bagi Allah, Tuhan seluruh alam."),
                Verse(3, "الرَّحْمَٰنِ الرَّحِيمِ", "Ar-rahmanir-rahim", "Yang Maha Pengasih lagi Maha Penyayang."),
                Verse(4, "مَالِكِ يَوْمِ الدِّينِ", "Maliki yaumid-din", "Pemilik hari pembalasan."),
                Verse(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaka na'budu wa iyyaka nasta'in", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami mohon pertolongan."),
                Verse(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-siratal-mustaqim", "Tunjukilah kami jalan yang lurus,"),
                Verse(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siratalladhina an'amta 'alaihim ghairil-maghdubi 'alaihim wa lad-dallin", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai dan bukan (pula jalan) mereka yang sesat.")
            )
            36 -> listOf(
                Verse(1, "يس", "Ya-sin", "Ya Sin"),
                Verse(2, "وَالْقُرْآنِ الْحَكِيمِ", "Wal-qur'anil-hakim", "Demi Al-Qur'an yang penuh hikmah,"),
                Verse(3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Innaka laminal-mursalin", "sungguh, engkau (Muhammad) adalah salah seorang dari rasul-rasul,"),
                Verse(4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "'Ala siratim mustaqim", "(yang berada) di atas jalan yang lurus,")
            )
            67 -> listOf(
                Verse(1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Tabarakalladhi biyadihil-mulku wa huwa 'ala kulli shay'in qadir", "Maha Suci Allah yang menguasai (segala) kerajaan, dan Dia Maha Kuasa atas segala sesuatu."),
                Verse(2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا", "Alladhi khalaqal-mawta wal-hayata liyabluwakum ayyukum ahsanu 'amala", "Yang menciptakan mati dan hidup, untuk menguji kamu, siapa di antara kamu yang lebih baik amalnya."),
            )
            112 -> listOf(
                Verse(1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwallahu ahad", "Katakanlah (Muhammad), \"Dialah Allah, Yang Maha Esa.\""),
                Verse(2, "اللَّهُ الصَّمَدُ", "Allahus-samad", "Allah tempat meminta segala sesuatu."),
                Verse(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yulad", "(Allah) tidak beranak dan tidak pula diperanakkan,"),
                Verse(4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakul lahu kufuwan ahad", "dan tidak ada sesuatu pun yang setara dengan Dia.")
            )
            else -> listOf(
                Verse(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillahir-rahmanir-rahim", "Dengan nama Allah Yang Maha Pengasih lagi Maha Penyayang."),
                Verse(2, "آيَةٌ كَرِيمَةٌ مِنْ هَذِهِ السُّورَةِ", "Ayatun karimatun min hadhihis-surah", "Ayat suci dari surah ini.")
            )
        }
    }

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
