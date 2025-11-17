package KeuanganApp;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ManajemenKeuangan {

    private static ManajemenKeuangan instance;

    // --- Data Inti ---
    private static final double DEFAULT_PEMASUKAN = 2000000.0; // Konstanta untuk nilai default
    private double pemasukanBulanan = DEFAULT_PEMASUKAN; // Nilai default
    private double batasPengeluaranHarian = pemasukanBulanan / 30.0;

    // Map untuk menyimpan pengeluaran harian. Key: Tanggal, Value: Total Pengeluaran Hari Itu
    private final Map<LocalDate, Double> dataMap = new HashMap<>();
    private String status = "BELUM DI CEK";

    /**
     * Konstruktor privat untuk implementasi Singleton.
     */
    private ManajemenKeuangan() {
        // Data map secara default kosong.
    }

    /**
     * Mendapatkan instance tunggal dari ManajemenKeuangan (Singleton).
     * @return Instance ManajemenKeuangan.
     */
    public static ManajemenKeuangan getInstance() {
        if (instance == null) {
            instance = new ManajemenKeuangan();
        }
        return instance;
    }

    // --- Getters dan Setters ---

    public double getPemasukanBulanan() {
        return pemasukanBulanan;
    }

    public void setPemasukanBulanan(double pemasukanBulanan) {
        this.pemasukanBulanan = pemasukanBulanan;
        // Batas pengeluaran harian = Pemasukan/30
        this.batasPengeluaranHarian = pemasukanBulanan / 30.0;
    }

    public double getBatasPengeluaranHarian() {
        return batasPengeluaranHarian;
    }

    public String getStatus() {
        return status;
    }

    // --- Logika Pengeluaran Harian ---

    /**
     * Menambahkan pengeluaran untuk tanggal tertentu.
     */
    public void tambahPengeluaran(LocalDate tanggal, double pengeluaranBaru) {
        // Ambil total pengeluaran yang sudah ada untuk tanggal tersebut
        double currentTotal = dataMap.getOrDefault(tanggal, 0.0);

        // Tambahkan pengeluaran baru
        double newTotal = currentTotal + pengeluaranBaru;

        // Simpan kembali ke map
        dataMap.put(tanggal, newTotal);
    }

    /**
     * Mendapatkan total pengeluaran untuk tanggal tertentu.
     */
    public double getTotalPengeluaranHariIni(LocalDate tanggal) {
        return dataMap.getOrDefault(tanggal, 0.0);
    }

    // --- Logika Monitoring (7 Hari Input Terakhir) ---

    /**
     * Mendapatkan daftar maksimal 7 tanggal pengeluaran terakhir yang telah diinput.
     * Tanggal diurutkan dari yang paling lama ke yang paling baru (sesuai urutan grafik).
     * @return List<LocalDate> yang berisi maksimal 7 tanggal terakhir dengan pengeluaran > 0.
     */
    public List<LocalDate> get7HariInputTerakhir() {
        return dataMap.keySet().stream()
                // Filter: hanya ambil tanggal yang memiliki pengeluaran > 0
                .filter(date -> dataMap.get(date) > 0)
                // Urutkan tanggal dari yang paling lama ke yang paling baru
                .sorted(Comparator.naturalOrder())
                // Kumpulkan hasilnya ke List
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            int size = list.size();
                            if (size <= 7) {
                                return list; // Jika 7 atau kurang, kembalikan semua
                            } else {
                                // Jika lebih dari 7, ambil 7 elemen terakhir
                                return list.subList(size - 7, size);
                            }
                        }
                ));
    }

    // --- Logika Status ---

    /**
     * Memeriksa status pengeluaran harian (Aman/Tidak Aman).
     */
    public void cekStatus(LocalDate tanggal) {
        double totalPengeluaran = getTotalPengeluaranHariIni(tanggal);
        if (pemasukanBulanan == 0.0) {
            this.status = "Pemasukan belum diset";
        } else if (totalPengeluaran <= batasPengeluaranHarian) {
            this.status = "AMAN";
        } else {
            this.status = "TIDAK AMAN (Melebihi Batas)";
        }
    }

    // --- Logika Reset (BARU) ---
    /**
     * Mereset semua data keuangan ke nilai default.
     * Pemasukan Bulanan diatur ulang menjadi 2.000.000 dan semua data harian dihapus.
     */
    public void resetSemuaData() {
        this.pemasukanBulanan = DEFAULT_PEMASUKAN;
        this.batasPengeluaranHarian = DEFAULT_PEMASUKAN / 30.0;
        this.dataMap.clear(); // Hapus semua data pengeluaran harian
        this.status = "BELUM DI CEK";
    }

    // --- Utility ---

    /**
     * Mengubah nilai double menjadi format Rupiah (contoh: Rp 1.000.000).
     */
    public static String formatRupiah(double value) {
        if (value < 0) {
            value = 0;
        }
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        // Hapus simbol mata uang, hanya sisakan "Rp " dan angkanya
        String formatted = formatRupiah.format(value).replaceAll("Rp\\s*", "Rp ");
        return formatted.substring(0, formatted.length() - 3); // Hapus ",00"
    }

    /**
     * Overload untuk format long/int
     */
    public static String formatRupiah(long value) {
        if (value < 0) {
            value = 0;
        }
        return formatRupiah((double) value);
    }
}
