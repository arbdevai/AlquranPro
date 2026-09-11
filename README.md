# AlquranPro

Aplikasi Al-Qur’an berbasis Kotlin dan Jetpack Compose. Project masih berupa
kerangka awal; data Al-Qur’an offline dan fitur membaca belum tersedia.

## Build APK di GitHub

Build dijalankan melalui GitHub Actions, tanpa compile di lokal.

1. Push perubahan ke branch `main` di `arbdevai/AlquranPro`, atau jalankan workflow
   **Android Build** lewat tab **Actions → Run workflow**.
2. Tunggu workflow selesai dengan status sukses.
3. Download artifact **AlquranPro-debug** dari halaman workflow lalu ekstrak ZIP
   untuk mendapatkan APK debug yang bisa diinstal.

Pull request ke `main` juga menjalankan build. APK ini untuk pengujian;
konfigurasi signing release belum disiapkan.

Workflow memasang Gradle 8.9 dan JDK 17, sesuai Android Gradle Plugin 8.7.3.
Gradle Wrapper belum disertakan; workflow memakai perintah `gradle` yang
disediakan oleh `gradle/actions/setup-gradle`.
