import tkinter as tk
from tkinter import messagebox
from collections import deque
import pyttsx3
import threading

class SimulasiAntrian:
    def __init__(self, root):
        self.root = root
        self.root.title("Simulasi Antrian Bank")
        self.root.geometry("500x450")
        self.root.configure(bg="#F5F7FA")

        self.queue = deque()
        self.nomor = 1

        # ===== INPUT =====
        frame_atas = tk.Frame(root, bg="white", padx=15, pady=10)
        frame_atas.pack(fill="x", pady=(10, 0))

        label = tk.Label(frame_atas, text="Masukkan Nama Nasabah",
                         font=("Segoe UI", 12, "bold"), bg="white")
        label.pack(anchor="w")

        self.input_nama = tk.Entry(frame_atas, font=("Segoe UI", 12))
        self.input_nama.pack(fill="x", pady=8)
        self.input_nama.bind("<Return>", lambda e: self.ambil_antrian())

        # ===== TOMBOL =====
        frame_tombol = tk.Frame(frame_atas, bg="white")
        frame_tombol.pack(fill="x")

        btn_ambil = self.buat_tombol(frame_tombol, "Ambil Antrian", "#2ECC71", self.ambil_antrian)
        btn_panggil = self.buat_tombol(frame_tombol, "Panggil Berikutnya", "#E74C3C", self.panggil)
        btn_refresh = self.buat_tombol(frame_tombol, "Refresh", "#2980B9", self.update_text)
        btn_reset = self.buat_tombol(frame_tombol, "Reset Antrian", "#7F8C8D", self.reset)

        btn_ambil.grid(row=0, column=0, padx=5, pady=5, sticky="ew")
        btn_panggil.grid(row=0, column=1, padx=5, pady=5, sticky="ew")
        btn_refresh.grid(row=1, column=0, padx=5, pady=5, sticky="ew")
        btn_reset.grid(row=1, column=1, padx=5, pady=5, sticky="ew")

        # ===== AREA ANTRIAN =====
        frame_tengah = tk.Frame(root, bg="#F5F7FA", padx=15, pady=10)
        frame_tengah.pack(fill="both", expand=True)

        label_daftar = tk.Label(frame_tengah, text="Daftar Antrian Saat Ini",
                                font=("Segoe UI", 12, "bold"), bg="#F5F7FA")
        label_daftar.pack(anchor="w")

        self.area = tk.Text(frame_tengah, font=("Consolas", 12), height=15)
        self.area.pack(fill="both", expand=True, pady=5)
        self.area.config(state="disabled")

        self.update_text()

    def buat_tombol(self, parent, text, color, command):
        return tk.Button(parent, text=text, bg=color, fg="white",
                         font=("Segoe UI", 10, "bold"),
                         command=command, relief="flat")

    def ambil_antrian(self):
        nama = self.input_nama.get().strip()
        if not nama:
            messagebox.showwarning("Peringatan", "Nama tidak boleh kosong!")
            return

        self.queue.append((self.nomor, nama))
        self.nomor += 1
        self.input_nama.delete(0, tk.END)
        self.update_text()

    def panggil(self):
        if not self.queue:
            messagebox.showinfo("Info", "Antrian sudah kosong")
            return

        nomor, nama = self.queue.popleft()

        teks = f"Nomor {nomor}, atas nama {nama}. Silakan menuju loket."

        messagebox.showinfo("Panggilan", teks)

        threading.Thread(target=self.speak, args=(teks,)).start()
        self.update_text()

    def reset(self):
        konfirmasi = messagebox.askyesno("Konfirmasi", "Yakin reset antrian?")
        if konfirmasi:
            self.queue.clear()
            self.nomor = 1
            self.update_text()
            messagebox.showinfo("Sukses", "Antrian direset")

    def update_text(self):
        self.area.config(state="normal")
        self.area.delete(1.0, tk.END)

        if not self.queue:
            self.area.insert(tk.END, "Belum ada antrian.\nSilakan ambil nomor.")
        else:
            for i, (nomor, nama) in enumerate(self.queue, start=1):
                self.area.insert(tk.END, f"{i:2d}. Nomor {nomor:<3} : {nama}\n")

        self.area.config(state="disabled")

    def speak(self, text):
        engine = pyttsx3.init()

        engine.setProperty('rate', 120)  # lebih lambat

        # Tambahkan jeda (gunakan koma atau titik)
        text = text.replace(",", ", ").replace(".", "... ")

        engine.say(text)
        engine.runAndWait()


# ===== MAIN =====
if __name__ == "__main__":
    root = tk.Tk()
    app = SimulasiAntrian(root)
    root.mainloop()