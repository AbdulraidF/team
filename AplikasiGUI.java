package KeuanganApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class AplikasiGUI extends JFrame implements ActionListener {

    private ManajerKeuangan manajer;
    private JLabel labelLaporan;
    private JTextField inputJumlah;
    private JComboBox<String> inputKategori;
    private JButton tombolTambah, tombolSetData, tombolCetak, tombolReset; // <-- TOMBOL RESET DITAMBAHKAN

    // Skema Warna
    private final Color COLOR_PRIMER = new Color(140, 255, 156);
    private final Color COLOR_SEKUNDER = new Color(240, 240, 240);
    private final Color COLOR_DANGER = new Color(200, 50, 50);
    private final Font FONT_BESAR = new Font("Arial", Font.BOLD, 18);

    public AplikasiGUI() {
        manajer = new ManajerKeuangan();

        setTitle("Aplikasi Manajemen Keuangan Harian");
        setSize(850, 550); // Ukuran sedikit diperbesar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_SEKUNDER);

        // 3. Panel NORTH: Input Transaksi dan Set Data
        JPanel panelNorth = new JPanel(new GridLayout(2, 1, 5, 5));
        panelNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelNorth.setBackground(COLOR_SEKUNDER);

        // --- Baris 1: Input Pengeluaran ---
        JPanel panelTransaksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelTransaksi.setBackground(COLOR_SEKUNDER);

        inputJumlah = new JTextField(10);
        String[] kategoriOptions = {"Makan", "Transportasi", "Belanja", "Tagihan", "Lain-lain"};
        inputKategori = new JComboBox<>(kategoriOptions);
        tombolTambah = new JButton("Tambah Pengeluaran");
        tombolTambah.setBackground(COLOR_DANGER);
        tombolTambah.setForeground(Color.WHITE);
        tombolTambah.addActionListener(this);

        panelTransaksi.add(new JLabel("Jumlah:"));
        panelTransaksi.add(inputJumlah);
        panelTransaksi.add(new JLabel("Kategori:"));
        panelTransaksi.add(inputKategori);
        panelTransaksi.add(tombolTambah);

        // --- Baris 2: Set Data, Cetak & Reset ---
        JPanel panelKontrol = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelKontrol.setBackground(COLOR_SEKUNDER);

        tombolSetData = new JButton("Atur Pemasukan & Batas");
        tombolSetData.setBackground(COLOR_PRIMER);
        tombolSetData.setForeground(Color.WHITE);
        tombolSetData.addActionListener(this);

        tombolCetak = new JButton("Cetak Laporan Hari Ini");
        tombolCetak.setBackground(Color.DARK_GRAY);
        tombolCetak.setForeground(Color.WHITE);
        tombolCetak.addActionListener(this);

        tombolReset = new JButton("RESET SEMUA DATA"); // <-- TOMBOL RESET
        tombolReset.setBackground(Color.RED);
        tombolReset.setForeground(Color.WHITE);
        tombolReset.addActionListener(this);

        panelKontrol.add(tombolSetData);
        panelKontrol.add(tombolCetak);
        panelKontrol.add(tombolReset); // <-- DITAMBAHKAN KE PANEL

        panelNorth.add(panelTransaksi);
        panelNorth.add(panelKontrol);

        add(panelNorth, BorderLayout.NORTH);

        // 4. Panel CENTER: Laporan Utama
        JPanel panelLaporanContainer = new JPanel(new GridBagLayout());
        panelLaporanContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelLaporan = new JLabel("Memuat Laporan...", SwingConstants.CENTER);
        labelLaporan.setFont(FONT_BESAR);
        panelLaporanContainer.add(labelLaporan);

        add(panelLaporanContainer, BorderLayout.CENTER);

        updateLaporan();

        setLocationRelativeTo(null); // Posisikan di tengah layar
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tombolTambah) {
            handleTambahPengeluaran();
        } else if (e.getSource() == tombolSetData) {
            handleSetData();
        } else if (e.getSource() == tombolCetak) {
            handleCetakLaporan();
        } else if (e.getSource() == tombolReset) { // <-- TANGANI RESET
            handleResetData();
        }
    }

    // --- Logika Tombol Reset ---
    private void handleResetData() {
        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "ANDA YAKIN INGIN MERESET SEMUA DATA? Pemasukan, Batas, dan Transaksi akan terhapus.",
                "Konfirmasi Hard Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            manajer.resetSemuaData();
            updateLaporan();
            JOptionPane.showMessageDialog(this, "Semua data berhasil di-reset! Silakan atur Pemasukan Bulanan Anda lagi.", "Reset Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // --- Logika Tombol Lainnya (handleSetData, handleTambahPengeluaran, handleCetakLaporan) ---
    private void handleTambahPengeluaran() {
        try {
            double jumlah = Double.parseDouble(inputJumlah.getText());
            String kategori = (String) inputKategori.getSelectedItem();

            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus positif.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manajer.tambahPengeluaran(jumlah, kategori);
            inputJumlah.setText("");
            updateLaporan();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah yang valid (hanya angka).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSetData() {
        String inputMasuk = JOptionPane.showInputDialog(this, "Masukkan Pemasukan Bulanan:", manajer.getPemasukanBulanan());
        if (inputMasuk != null) {
            try {
                double pemasukan = Double.parseDouble(inputMasuk);
                manajer.setPemasukanBulanan(pemasukan);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pemasukan harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String inputBatas = JOptionPane.showInputDialog(this, "Masukkan Batas Pengeluaran Harian:", manajer.getBatasPengeluaranHarian());
        if (inputBatas != null) {
            try {
                double batas = Double.parseDouble(inputBatas);
                manajer.setBatasPengeluaranHarian(batas);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Batas harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        updateLaporan();
    }

    private void handleCetakLaporan() {
        try {
            manajer.cetakLaporanBulanan();
            JOptionPane.showMessageDialog(this, "Laporan berhasil dicetak ke file:\nLaporan_Keuangan_" + manajer.tanggalTerakhirDiperbarui + ".txt", "Cetak Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Logika Update Laporan & Peringatan Bewarna ---
    private void updateLaporan() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        double pengeluaranHariIni = manajer.hitungPengeluaranHariIni();
        double batas = manajer.getBatasPengeluaranHarian();
        double pemasukan = manajer.getPemasukanBulanan();
        double sisaUang = pemasukan - pengeluaranHariIni;

        double persentase = (batas != 0) ? (pengeluaranHariIni / batas) * 100 : 0;
        String peringatan, sisaWarna;

        Color bgColor = Color.WHITE;

        if (persentase >= 100) {
            peringatan = "<html><font color='red'>PERINGATAN KERAS: Pengeluaran MELEBIHI batas harian!</font></html>";
            bgColor = new Color(255, 220, 220);
        } else if (persentase >= 80) {
            peringatan = "<html><font color='#CC8000'>PERINGATAN: Pengeluaran sudah mencapai 80% batas harian!</font></html>";
            bgColor = new Color(255, 255, 200);
        } else {
            peringatan = "Status: Pengeluaran aman.";
        }

        sisaWarna = (sisaUang < 0) ? "red" : "green";

        ((JPanel) labelLaporan.getParent()).setBackground(bgColor);

        String laporan = String.format(
                "<html>" +
                        "<center><h2 style='color:#333333;'>Laporan Keuangan Harian (Tanggal %s)</h2></center>" +
                        "<hr style='border-top: 1px solid #AAAAAA;'>" +
                        "<table width='600' style='font-size:14pt;'>" +
                        "<tr><td width='350'><b>Pemasukan Bulanan:</b></td><td>Rp %s</td></tr>" +
                        "<tr><td><b>Batas Pengeluaran Harian:</b></td><td>Rp %s</td></tr>" +
                        "<tr><td colspan='2'><hr></td></tr>" +
                        "<tr><td><b>PENGELUARAN HARI INI:</b></td><td><font color='red'>Rp %s</font></td></tr>" +
                        "<tr><td><b>Persentase dari Batas:</b></td><td><b>%.2f%%</b></td></tr>" +
                        "<tr><td colspan='2'><hr></td></tr>" +
                        "<tr><td><b>Sisa Uang (Simulasi):</b></td><td><font color='%s'>Rp %s</font></td></tr>" +
                        "<tr><td colspan='2'><center><br><b>%s</b></center></td></tr>" +
                        "</table>" +
                        "</html>",
                manajer.tanggalTerakhirDiperbarui.format(dtFormatter),
                df.format(pemasukan),
                df.format(batas),
                df.format(pengeluaranHariIni),
                persentase,
                sisaWarna,
                df.format(sisaUang),
                peringatan
        );

        labelLaporan.setText(laporan);
    }
}