import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SimulasiAntrian extends JFrame {

    Queue<String[]> queue = new LinkedList<>();
    int nomor = 1;

    JTextField inputNama;
    JTextArea areaAntrian;

    public SimulasiAntrian() {
        setTitle("Simulasi Antrian Bank");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== PANEL ATAS =====
        JPanel panelAtas = new JPanel(new GridLayout(3, 1, 5, 5));

        JLabel label = new JLabel("Masukkan Nama:", JLabel.CENTER);
        panelAtas.add(label);

        inputNama = new JTextField();
        panelAtas.add(inputNama);

        JPanel panelTombol = new JPanel(new FlowLayout());

        JButton btnAmbil = new JButton("Ambil Antrian");
        JButton btnTampil = new JButton("Tampilkan");
        JButton btnPanggil = new JButton("Panggil");

        panelTombol.add(btnAmbil);
        panelTombol.add(btnTampil);
        panelTombol.add(btnPanggil);

        panelAtas.add(panelTombol);
        add(panelAtas, BorderLayout.NORTH);

        // ===== AREA ANTRIAN =====
        areaAntrian = new JTextArea();
        areaAntrian.setEditable(false);
        areaAntrian.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaAntrian);
        add(scroll, BorderLayout.CENTER);

        // ===== EVENT =====

        // Ambil Antrian
        btnAmbil.addActionListener(e -> {
            String nama = inputNama.getText().trim();

            if (nama.equals("")) {
                JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong!");
                return;
            }

            String[] data = {String.valueOf(nomor), nama};
            queue.add(data);
            nomor++;

            inputNama.setText("");
            updateTextArea();
        });

        // Tampilkan
        btnTampil.addActionListener(e -> updateTextArea());

        // Panggil
        btnPanggil.addActionListener(e -> {
            if (queue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Antrian kosong");
                return;
            }

            String[] data = queue.poll();
            String teks = "Nomor " + data[0] + " atas nama " + data[1] + ", silakan ke loket";

            JOptionPane.showMessageDialog(null, teks);

            speak(teks); 

            updateTextArea();
        });
    }

    // ===== UPDATE TEXT AREA =====
    public void updateTextArea() {
        areaAntrian.setText("Daftar Antrian:\n\n");

        for (String[] data : queue) {
            areaAntrian.append("Nomor " + data[0] + " - " + data[1] + "\n");
        }
    }

    // ===== TEXT TO SPEECH (LINUX - STABIL) =====
    public void speak(String text) {
        try {
            String safeText = text.replace("\"", "");

            ProcessBuilder pb = new ProcessBuilder(
                    "espeak", "-s", "120", "-p", "50", "-v", "id", safeText
            );

            Process process = pb.start();
            process.waitFor(); // ⬅️ tunggu sampai suara selesai

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulasiAntrian().setVisible(true);
        });
    }
}