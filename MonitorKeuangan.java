package KeuanganApp;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MonitorKeuangan extends JFrame {

    private ManajemenKeuangan manager = ManajemenKeuangan.getInstance();
    private static final Color PRIMARY_COLOR = new Color(173, 215, 230);
    private static final Color BACKGROUND_START = new Color(240, 248, 255);
    private static final Color BACKGROUND_END = new Color(200, 220, 240);
    private static final double MAX_BAR_HEIGHT = 450;
    private static final int MARGIN_TOP = 50;
    private static final int MARGIN_LEFT = 80;
    private static final int MARGIN_RIGHT = 50;
    private static final int MARGIN_BOTTOM = 80;

    public MonitorKeuangan() {
        setTitle("AMESA - Monitor Pengeluaran Harian");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        JPanel rootPanel = new GradientPanel(BACKGROUND_START, BACKGROUND_END);
        rootPanel.setLayout(new BorderLayout());

        // Tambahkan header dan panel grafik
        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(new ChartPanel(), BorderLayout.CENTER);
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    // ... (createHeaderPanel tidak berubah) ...
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Monitor Pengeluaran Harian (Maks. 7 Hari Input Terakhir)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    // ... (createFooterPanel tidak berubah) ...
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 14));
        homeButton.setBackground(PRIMARY_COLOR);
        homeButton.addActionListener(e -> {
            dispose();
            new AplikasiGUI().setVisible(true);
        });
        footerPanel.add(homeButton);
        return footerPanel;
    }

    /**
     * Inner Class untuk menggambar Grafik Batang
     */
    private class ChartPanel extends JPanel {

        public ChartPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // --- 1. Ambil Data dan Kalkulasi Skala ---

            // REVISI UTAMA: Ambil 7 hari input terakhir
            List<LocalDate> inputDates = manager.get7HariInputTerakhir();

            // Mendapatkan data pengeluaran untuk tanggal-tanggal tersebut
            List<Double> expenses = inputDates.stream()
                    .map(date -> manager.getTotalPengeluaranHariIni(date))
                    .collect(Collectors.toList());

            if (expenses.isEmpty()) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawString("Tidak ada data pengeluaran yang tercatat.", width / 2 - 100, height / 2);
                return;
            }

            double batasHarian = manager.getBatasPengeluaranHarian();

            // Tentukan nilai maksimum (Max Y-Axis)
            double maxExpense = expenses.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0);

            // Max Y-Axis adalah max(pengeluaran tertinggi, batas harian) + sedikit buffer
            double maxYValue = Math.max(maxExpense, batasHarian) * 1.2;
            if (maxYValue == 0) maxYValue = batasHarian * 1.5; // Pastikan tidak nol

            int chartAreaWidth = width - MARGIN_LEFT - MARGIN_RIGHT;
            int chartAreaHeight = height - MARGIN_TOP - MARGIN_BOTTOM;
            int numBars = inputDates.size();
            int barWidth = (numBars > 0) ? chartAreaWidth / (numBars * 2) : 0;
            int barSpacing = (numBars > 1) ? (chartAreaWidth - (numBars * barWidth)) / (numBars - 1) : 0;

            if (numBars <= 1) { // Handle case 1 bar or 0 bars
                barWidth = chartAreaWidth / 4;
                barSpacing = chartAreaWidth / 2;
            } else {
                barSpacing = (chartAreaWidth - (numBars * barWidth)) / (numBars - 1);
            }

            int xOffset = (chartAreaWidth - (numBars * barWidth + (numBars > 1 ? (numBars - 1) * barSpacing : 0))) / 2;
            int xCurrent = MARGIN_LEFT + xOffset;

            // --- 2. Gambar Sumbu X dan Y ---

            // Sumbu Y
            g2d.setColor(Color.BLACK);
            g2d.drawLine(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT, height - MARGIN_BOTTOM);

            // Sumbu X
            g2d.drawLine(MARGIN_LEFT, height - MARGIN_BOTTOM, width - MARGIN_RIGHT, height - MARGIN_BOTTOM);

            // Gambar Label Sumbu Y (4 label + 0)
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= 4; i++) {
                double value = (maxYValue / 4.0) * i;
                int y = height - MARGIN_BOTTOM - (int) ((value / maxYValue) * chartAreaHeight);

                // Coretan pada sumbu Y
                g2d.drawLine(MARGIN_LEFT - 5, y, MARGIN_LEFT + 5, y);

                // Label Angka
                String label = manager.formatRupiah((long)value);
                g2d.drawString(label, MARGIN_LEFT - g2d.getFontMetrics().stringWidth(label) - 10, y + 5);
            }

            // --- 3. Gambar Batas Harian (Garis Merah Putus-putus) ---

            int batasY = height - MARGIN_BOTTOM - (int) ((batasHarian / maxYValue) * chartAreaHeight);

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
            g2d.drawLine(MARGIN_LEFT, batasY, width - MARGIN_RIGHT, batasY);

            // Label Batas Harian
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Batas Harian: " + manager.formatRupiah(batasHarian), width - MARGIN_RIGHT + 5, batasY + 5);

            // Reset Stroke
            g2d.setStroke(new BasicStroke(1));

            // --- 4. Gambar Batang Grafik dan Label X ---

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE\ndd");
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            for (int i = 0; i < numBars; i++) {
                double expense = expenses.get(i);
                LocalDate date = inputDates.get(i);

                int barHeight = (int) ((expense / maxYValue) * chartAreaHeight);
                int barY = height - MARGIN_BOTTOM - barHeight;

                // Tentukan warna: Merah jika melebihi batas, Biru jika aman
                g2d.setColor((expense > batasHarian) ? new Color(220, 0, 0, 200) : new Color(0, 150, 255, 200));
                g2d.fillRect(xCurrent, barY, barWidth, barHeight);

                // Tambahkan outline
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRect(xCurrent, barY, barWidth, barHeight);

                // Label Nilai di atas batang
                g2d.setColor(Color.BLACK);
                String valueLabel = manager.formatRupiah(expense);
                g2d.drawString(valueLabel, xCurrent + (barWidth / 2) - (g2d.getFontMetrics().stringWidth(valueLabel) / 2), barY - 10);

                // Label Sumbu X (Tanggal: EEE\ndd)
                String dateLabel = date.format(formatter);
                String[] lines = dateLabel.split("\n");

                int xLabel = xCurrent + (barWidth / 2) - (g2d.getFontMetrics().stringWidth(lines[0]) / 2);
                int yLabel1 = height - MARGIN_BOTTOM + 15;

                // Garis 1 (Nama Hari)
                g2d.drawString(lines[0], xLabel, yLabel1);

                // Garis 2 (Tanggal)
                int xLabel2 = xCurrent + (barWidth / 2) - (g2d.getFontMetrics().stringWidth(lines[1]) / 2);
                g2d.drawString(lines[1], xLabel2, yLabel1 + 15);

                // Pindah ke posisi batang berikutnya
                xCurrent += barWidth + barSpacing;
            }
        }
    }

    // Inner Class untuk Gradient Background (Tidak Berubah)
    private class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;
        public GradientPanel(Color c1, Color c2) {
            this.color1 = c1;
            this.color2 = c2;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}
