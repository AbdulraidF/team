package KeuanganApp;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SplashScreen extends JWindow {

    // Durasi tampilan splash screen dalam milidetik (3 detik)
    private static final int DURATION = 3000;

    public SplashScreen() {
        // Menggunakan JWindow agar tidak memiliki border (frame)

        // --- Setup Panel Konten ---
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(50, 150, 200), 10)); // Border biru

        // Judul Utama
        JLabel labelJudul = new JLabel("<html><center><h1 style='color:#333333;'>Money Manager App</h1><p style='color:#777777;'>Loading Data Keuangan...</p></center></html>", SwingConstants.CENTER);
        labelJudul.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(labelJudul, BorderLayout.CENTER);

        // Informasi Tambahan
        JLabel labelInfo = new JLabel("© 2025 | Versi 1.0", SwingConstants.CENTER);
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(labelInfo, BorderLayout.SOUTH);

        // Menambahkan panel ke JWindow
        this.add(panel);

        // Mengatur ukuran dan posisi di tengah layar
        setSize(500, 300);
        setLocationRelativeTo(null); // Pusat di tengah layar
    }

    public void showSplash() {
        this.setVisible(true);

        // Timer untuk menyembunyikan splash screen setelah DURATION
        try {
            Thread.sleep(DURATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.setVisible(false);
        this.dispose(); // Melepaskan sumber daya window
    }
}