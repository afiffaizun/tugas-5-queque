import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SimulasiAntrian extends JFrame {

    Queue<String[]> queue = new LinkedList<>();
    int nomor = 1;

    JTextField inputNama;
    JTextArea areaAntrian;

    // Warna tema biar konsisten
    private final Color WARNA_PRIMER = new Color(41, 128, 185); // Biru
    private final Color WARNA_AKSEN = new Color(46, 204, 113); // Hijau
    private final Color WARNA_BAHAYA = new Color(231, 76, 60); // Merah
    private final Color WARNA_NETRAL = new Color(127, 140, 141); // Abu-abu
    private final Color WARNA_BG = new Color(245, 247, 250); // Abu muda

    public SimulasiAntrian() {
        setTitle("Simulasi Antrian Bank");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengah layar
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(WARNA_BG);

        // ===== PANEL ATAS =====
        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(new BoxLayout(panelAtas, BoxLayout.Y_AXIS));
        panelAtas.setBorder(new EmptyBorder(15, 15, 10, 15));
        panelAtas.setBackground(Color.WHITE);

        JLabel label = new JLabel("Masukkan Nama Nasabah");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAtas.add(label);
        panelAtas.add(Box.createRigidArea(new Dimension(0, 8)));

        inputNama = new JTextField();
        inputNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputNama.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAtas.add(inputNama);
        panelAtas.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel Tombol
        JPanel panelTombol = new JPanel(new GridLayout(2, 2, 10, 10)); // Jadi 2x2 grid
        panelTombol.setBackground(Color.WHITE);
        panelTombol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnAmbil = buatTombol("Ambil Antrian", WARNA_AKSEN);
        JButton btnPanggil = buatTombol("Panggil Berikutnya", WARNA_BAHAYA);
        JButton btnTampil = buatTombol("Refresh", WARNA_PRIMER);
        JButton btnReset = buatTombol("Reset Antrian", WARNA_NETRAL); // Tombol baru

        panelTombol.add(btnAmbil);
        panelTombol.add(btnPanggil);
        panelTombol.add(btnTampil);
        panelTombol.add(btnReset);

        panelAtas.add(panelTombol);
        add(panelAtas, BorderLayout.NORTH);

        // ===== AREA ANTRIAN =====
        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBorder(new EmptyBorder(0, 15, 15, 15));
        panelTengah.setBackground(WARNA_BG);

        JLabel labelDaftar = new JLabel("Daftar Antrian Saat Ini");
        labelDaftar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelDaftar.setBorder(new EmptyBorder(0, 0, 8, 0));
        panelTengah.add(labelDaftar, BorderLayout.NORTH);

        areaAntrian = new JTextArea();
        areaAntrian.setEditable(false);
        areaAntrian.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaAntrian.setMargin(new Insets(10, 10, 10, 10));
        areaAntrian.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(areaAntrian);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panelTengah.add(scroll, BorderLayout.CENTER);

        add(panelTengah, BorderLayout.CENTER);

        // ===== EVENT =====
        btnAmbil.addActionListener(e -> {
            String nama = inputNama.getText().trim();
            if (nama.equals("")) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] data = {String.valueOf(nomor), nama};
            queue.add(data);
            nomor++;
            inputNama.setText("");
            updateTextArea();
            inputNama.requestFocus();
        });

        inputNama.addActionListener(e -> btnAmbil.doClick());

        btnTampil.addActionListener(e -> updateTextArea());

        btnPanggil.addActionListener(e -> {
            if (queue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Antrian sudah kosong", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] data = queue.poll();
            String teks = "Nomor " + data[0] + " atas nama " + data[1] + ", silakan ke loket";
            JOptionPane.showMessageDialog(this, teks, "Panggilan", JOptionPane.PLAIN_MESSAGE);
            speak(teks);
            updateTextArea();
        });

        // Event tombol reset
        btnReset.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin mau reset semua antrian? Nomor akan mulai dari 1 lagi.",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                queue.clear();
                nomor = 1;
                updateTextArea();
                JOptionPane.showMessageDialog(this, "Antrian berhasil direset.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        updateTextArea();
    }

    private JButton buatTombol(String text, Color warna) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(warna);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void updateTextArea() {
        if (queue.isEmpty()) {
            areaAntrian.setText(" Belum ada antrian.\n Silakan ambil nomor terlebih dahulu.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String[] data : queue) {
            sb.append(String.format(" %2d. Nomor %-3s : %s\n", i++, data[0], data[1]));
        }
        areaAntrian.setText(sb.toString());
    }

    public void speak(String text) {
        new Thread(() -> {
            try {
                String safeText = text.replace("\"", "");
                ProcessBuilder pb = new ProcessBuilder("espeak", "-s", "120", "-p", "50", "-v", "id", safeText);
                Process process = pb.start();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new SimulasiAntrian().setVisible(true);
        });
    }
}