import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import com.sun.speech.freetts.*;


// =======================
// CLASS CUSTOMER
// =======================
class Customer {
    int nomor;
    String nama;

    public Customer(int nomor, String nama) {
        this.nomor = nomor;
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "Nomor " + nomor + " - " + nama;
    }
}

// =======================
// CLASS QUEUE
// =======================
class Antrian {
    private LinkedList<Customer> queue = new LinkedList<>();
    private int nomor = 1;

    public void tambah(String nama) {
        queue.add(new Customer(nomor++, nama));
    }

    public Customer panggil() {
        if (!queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    public LinkedList<Customer> getAll() {
        return queue;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// =======================
// GUI
// =======================
public class SimulasiAntrian extends JFrame {

    private Antrian antrian = new Antrian();

    private JTextArea area = new JTextArea();
    private JTextField inputNama = new JTextField();

    // =======================
    // 🔊 METHOD SUARA
    // =======================
    public void suara(String text) {
        Voice voice;
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice("kevin16");

        if (voice != null) {
            voice.allocate();
            voice.speak(text);
            voice.deallocate();
        } else {
            System.out.println("Voice tidak ditemukan!");
        }
    }

    public SimulasiAntrian() {
        setTitle("Simulasi Antrian Bank");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        // ================= HEADER =================
        JLabel title = new JLabel("SISTEM ANTRIAN BANK", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(title, BorderLayout.NORTH);

        // ================= PANEL INPUT =================
        JPanel panelInput = new JPanel(new GridLayout(2,1,5,5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input"));

        panelInput.add(new JLabel("Masukkan Nama:"));
        panelInput.add(inputNama);

        // ================= PANEL BUTTON =================
        JPanel panelTombol = new JPanel(new GridLayout(1,3,10,10));
        panelTombol.setBorder(BorderFactory.createTitledBorder("Menu"));

        JButton btnAmbil = new JButton("Ambil");
        JButton btnTampil = new JButton("Tampilkan");
        JButton btnPanggil = new JButton("Panggil");

        panelTombol.add(btnAmbil);
        panelTombol.add(btnTampil);
        panelTombol.add(btnPanggil);

        // ================= PANEL ATAS =================
        JPanel panelAtas = new JPanel(new BorderLayout(5,5));
        panelAtas.add(panelInput, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.CENTER);

        // ================= AREA OUTPUT =================
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder("Output Antrian"));

        add(scroll, BorderLayout.SOUTH);

        // ================= EVENT =================

        // AMBIL
        btnAmbil.addActionListener(e -> {
            String nama = inputNama.getText();

            if (!nama.isEmpty()) {
                antrian.tambah(nama);
                area.setText("Berhasil ambil antrian untuk: " + nama);
                inputNama.setText("");
            } else {
                area.setText("Nama tidak boleh kosong!");
            }
        });

        // TAMPILKAN
        btnTampil.addActionListener(e -> {
            if (antrian.isEmpty()) {
                area.setText("Antrian kosong!");
                return;
            }

            String hasil = "Daftar Antrian:\n\n";
            for (Customer c : antrian.getAll()) {
                hasil += c.toString() + "\n";
            }

            area.setText(hasil);
        });

        // PANGGIL + 🔊 SUARA
        btnPanggil.addActionListener(e -> {
            Customer c = antrian.panggil();

            if (c != null) {

                String teks = "Number " + c.nomor +
                              " please come forward. " +
                              "Customer " + c.nama;

                // 🔊 jalankan suara di thread
                new Thread(() -> suara(teks)).start();

                area.setText("Memanggil:\n" + c.toString());
            } else {
                area.setText("Antrian kosong!");
            }
        });
    }

    public static void main(String[] args) {
        new SimulasiAntrian().setVisible(true);
    }
}