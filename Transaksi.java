package KeuanganApp;

import java.time.LocalDate;

public class Transaksi {

    public String jenis; // "Pengeluaran" atau "Pemasukan"
    public String kategori;
    public double jumlah;
    public LocalDate tanggal;

    public Transaksi(String jenis, String kategori, double jumlah, LocalDate tanggal) {
        this.jenis = jenis;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    @Override
    public String toString() {
        // Format penyimpanan: Jenis,Kategori,Jumlah,Tanggal
        return jenis + "," + kategori + "," + jumlah + "," + tanggal;
    }

    // Method untuk membaca data dari file teks
    public static Transaksi fromString(String baris) {
        try {
            String[] parts = baris.split(",");
            String jenis = parts[0];
            String kategori = parts[1];
            double jumlah = Double.parseDouble(parts[2]);
            LocalDate tanggal = LocalDate.parse(parts[3]);
            return new Transaksi(jenis, kategori, jumlah, tanggal);
        } catch (Exception e) {
            // Mengabaikan baris yang tidak valid
            return null;
        }
    }
}