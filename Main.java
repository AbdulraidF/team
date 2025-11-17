package KeuanganApp;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AplikasiGUI gui =  new AplikasiGUI();

                gui.setVisible(true);
            }
        });
    }
}



