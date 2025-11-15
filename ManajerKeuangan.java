package KeuanganApp;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManajerKeuangan {

    private final String NAMA_FILE = "data_keuangan.txt";
    private double pemasukanBulanan = 0.0;
    private double batasPengeluaranHarian = 50000.0; // Nilai default
    private List<Transaksi> daftarTransaksi = new ArrayList<>();

    public LocalDate tanggalTerakhirDiperbarui = LocalDate.now();

    public ManajerKeuangan() {
        bacaDataDariFile();
        cekResetHarian();
    }

    // --- METODE I/O ---
    private void bacaDataDariFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(NAMA_FILE))) {
            String baris;

            // Membaca Pemasukan, Batas, dan Tanggal Terakhir
            if ((baris = br.readLine()) != null) { pemasukanBulanan = Double.parseDouble(baris.split(":")[1].trim()); }
            if ((baris = br.readLine()) != null) { batasPengeluaranHarian = Double.parseDouble(baris.split(":")[1].trim()); }
            if ((baris = br.readLine()) != null) { tanggalTerakhirDiperbarui = LocalDate.parse(baris.split(":")[1].trim()); }

            // Membaca Transaksi Harian
            while ((baris = br.readLine()) != null) {
                if (!baris.trim().isEmpty()) {
                    Transaksi t = Transaksi.fromString(baris);
                    if (t != null && t.tanggal.isEqual(tanggalTerakhirDiperbarui)) {
                        daftarTransaksi.add(t);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            simpanDataKeFile(); // Membuat file baru jika belum ada
        } catch (Exception e) {
            System.err.println("Gagal membaca data: " + e.getMessage());
        }
    }

    public void simpanDataKeFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(NAMA_FILE))) {
            // Tulis data permanen
            pw.println("PemasukanBulanan:" + pemasukanBulanan);
            pw.println("BatasPengeluaranHarian:" + batasPengeluaranHarian);
            pw.println("TanggalTerakhirDiperbarui:" + LocalDate.now());

            // Tulis Transaksi Harian
            for (Transaksi t : daftarTransaksi) {
                if (t.tanggal.isEqual(LocalDate.now())) {
                    pw.println(t.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    // --- LOGIKA UTAMA & RESET OTOMATIS ---
    private void cekResetHarian() {
        if (!tanggalTerakhirDiperbarui.isEqual(LocalDate.now())) {
            daftarTransaksi.clear();
            tanggalTerakhirDiperbarui = LocalDate.now();
            simpanDataKeFile();
        }
    }

    public void tambahPengeluaran(double jumlah, String kategori) {
        if (jumlah > 0) {
            daftarTransaksi.add(new Transaksi("Pengeluaran", kategori, jumlah, LocalDate.now()));
            simpanDataKeFile();
        }
    }

    public double hitungPengeluaranHariIni() {
        double total = 0;
        for (Transaksi t : daftarTransaksi) {
            if (t.jenis.equals("Pengeluaran") && t.tanggal.isEqual(LocalDate.now())) {
                total += t.jumlah;
            }
        }
        return total;
    }

    // --- FITUR HARD RESET MANUAL ---
    public void resetSemuaData() {
        this.pemasukanBulanan = 0.0;
        this.batasPengeluaranHarian = 50000.0;
        this.daftarTransaksi.clear();
        this.tanggalTerakhirDiperbarui = LocalDate.now();

        File dataFile = new File(NAMA_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        simpanDataKeFile();
    }

    // --- FITUR CETAK LAPORAN ---
    public void cetakLaporanBulanan() throws IOException {
        String laporanFile = "Laporan_Keuangan_" + LocalDate.now() + ".txt";
        double pengeluaranKeseluruhan = hitungPengeluaranHariIni();
        double sisaUang = pemasukanBulanan - pengeluaranKeseluruhan;

        try (PrintWriter pw = new PrintWriter(new FileWriter(laporanFile))) {
            pw.println("==========================================");
            pw.println("      LAPORAN KEUANGAN");
            pw.println("==========================================");
            pw.println("Pemasukan Bulanan: Rp " + getPemasukanBulanan());
            pw.println("Batas Pengeluaran Harian: Rp " + batasPengeluaranHarian);
            pw.println("------------------------------------------");
            pw.println("Pengeluaran Hari Ini: Rp " + pengeluaranKeseluruhan);
            pw.println("Sisa Uang (Simulasi): Rp " + sisaUang);
            pw.println("==========================================");
        }
    }

    // --- GETTER & SETTER ---
    public double getPemasukanBulanan() { return pemasukanBulanan; }
    public double getBatasPengeluaranHarian() { return batasPengeluaranHarian; }

    public void setPemasukanBulanan(double pemasukan) {
        this.pemasukanBulanan = pemasukan;
        simpanDataKeFile();
    }

    public void setBatasPengeluaranHarian(double batas) {
        this.batasPengeluaranHarian = batas;
        simpanDataKeFile();
    }
}