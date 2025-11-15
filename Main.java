package KeuanganApp;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Tampilkan Splash Screen di Thread yang berbeda (Opsional, tapi lebih baik)
        // Atau langsung di main thread sebelum memuat aplikasi utama.

        SplashScreen splash = new SplashScreen();
        splash.showSplash(); // Tampilkan dan tunggu 3 detik

        // 2. Setelah splash screen selesai, jalankan GUI utama di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AplikasiGUI(); // Aplikasi utama dimuat
            }
        });
    }
}