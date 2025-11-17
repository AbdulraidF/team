package KeuanganApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class AplikasiGUI extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(173, 215, 230);
    private static final Color BACKGROUND_START = new Color(240, 248, 255);
    private static final Color BACKGROUND_END = new Color(200, 220, 240);

    public AplikasiGUI() {
        setTitle("AMESA - Aplikasi Manajemen Harian");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width * 0.7), (int) (screenSize.height * 0.85));
        setLocationRelativeTo(null);

        JPanel rootPanel = new GradientPanel(BACKGROUND_START, BACKGROUND_END);
        rootPanel.setLayout(new BorderLayout());

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentPanel(), BorderLayout.CENTER);

        setContentPane(rootPanel);
        setVisible(true);
    }

    // --- 1. KOMPONEN HEADER (Tidak berubah) ---
    private JPanel createHeaderPanel() {
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.setBorder(BorderFactory.createEmptyBorder(30, 80, 20, 80));

        RoundedPanel titlePanel = new RoundedPanel(PRIMARY_COLOR, 20);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel titleLabel = new JLabel("AMESA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Aplikasi Manajemen Harian");
        subTitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(subTitleLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel anggotaTitle = new JLabel("Anggota Kelompok:");
        anggotaTitle.setFont(new Font("Arial", Font.BOLD, 18));
        anggotaTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(anggotaTitle);

        String[] anggota = {"1. M. Irfan Maulana", "2. M Zikri Rahmansyah", "3. Abdul Ra'id Fadhillah", "4. Putri Melany Azhari"};
        for (String nama : anggota) {
            JLabel namaLabel = new JLabel(nama);
            namaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            namaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoPanel.add(namaLabel);
        }

        infoPanel.add(Box.createVerticalStrut(15));

        JLabel groupDosen = new JLabel("Kelompok 6, L1-Teknik Informatika");
        groupDosen.setFont(new Font("Arial", Font.PLAIN, 18));
        groupDosen.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(groupDosen);

        JLabel dosen = new JLabel("Dosen Pengampu: Femilia Hardina Caryn, M.Kom");
        dosen.setFont(new Font("Arial", Font.ITALIC, 18));
        dosen.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(dosen);

        headerContainer.add(titlePanel);
        headerContainer.add(infoPanel);
        return headerContainer;
    }


    // --- 2. KOMPONEN UTAMA (Menu) - Revisi Tombol Ketiga ---
    private JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 80, 80));

        RoundedPanel welcomePanel = new RoundedPanel(PRIMARY_COLOR, 20);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel welcomeTitle = new JLabel("Selamat Datang di Aplikasi AMAN");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Silakan Pilih Menu dibawah ini");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setForeground(Color.DARK_GRAY);

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(instructionLabel);
        welcomePanel.add(Box.createVerticalStrut(20));

        JButton pengeluaranButton = createMenuButton("Pengeluaran Harian");
        JButton monitorButton = createMenuButton("Monitor Pengeluaran");
        // --- REVISI: Mengubah tombol "Tentang Aplikasi" menjadi "Riwayat Keuangan" ---
        JButton riwayatButton = createMenuButton("Riwayat Keuangan");

        ActionListener buttonListener = e -> {
            String command = e.getActionCommand();

            dispose();

            if ("Pengeluaran Harian".equals(command)) {
                new PengeluaranHarian().setVisible(true);
            } else if ("Monitor Pengeluaran".equals(command)) {
                new MonitorKeuangan().setVisible(true);
            } else if ("Riwayat Keuangan".equals(command)) {
                // Masuk ke Halaman Riwayat Keuangan
                // Karena class RiwayatKeuangan belum ada, kita gunakan placeholder sederhana dulu
                new RiwayatKeuangan().setVisible(true);
            }
        };

        pengeluaranButton.addActionListener(buttonListener);
        monitorButton.addActionListener(buttonListener);
        riwayatButton.addActionListener(buttonListener); // Tambahkan listener ke tombol baru

        welcomePanel.add(pengeluaranButton);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(monitorButton);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(riwayatButton); // Tambahkan tombol Riwayat Keuangan

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalGlue());

        return contentPanel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(200, 230, 250));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setMaximumSize(new Dimension(350, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    // --- Inner Class dan Main Method (Tidak berubah) ---
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

    private class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius;
        public RoundedPanel(Color color, int radius) {
            this.backgroundColor = color;
            this.cornerRadius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(backgroundColor);
            graphics.fill(new RoundRectangle2D.Float(0, 0, width-1, height-1, arcs.width, arcs.height));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiGUI());
    }
}

