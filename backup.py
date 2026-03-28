import tkinter as tk
from tkinter import messagebox
from collections import deque
import pyttsx3

# ======================
# CLASS CUSTOMER
# ======================
class Customer:
    def __init__(self, nomor, nama):
        self.nomor = nomor
        self.nama = nama

    def __str__(self):
        return f"Nomor {self.nomor} - {self.nama}"

# ======================
# CLASS QUEUE
# ======================
class Antrian:
    def __init__(self):
        self.queue = deque()
        self.nomor = 1

    def tambah(self, nama):
        self.queue.append(Customer(self.nomor, nama))
        self.nomor += 1

    def panggil(self):
        if self.queue:
            return self.queue.popleft()
        return None

    def get_all(self):
        return list(self.queue)

    def is_empty(self):
        return len(self.queue) == 0

# ======================
# CLASS SUARA (TTS)
# ======================
class Suara:
    def __init__(self):
        self.engine = pyttsx3.init()

    def bicara(self, teks):
        self.engine.say(teks)
        self.engine.runAndWait()

# ======================
# GUI
# ======================
class SimulasiAntrian:
    def __init__(self, root):
        self.root = root
        self.root.title("Simulasi Antrian Bank")
        self.root.geometry("500x400")

        self.antrian = Antrian()
        self.suara = Suara()

        # INPUT NAMA
        tk.Label(root, text="Masukkan Nama:").pack()
        self.input_nama = tk.Entry(root)
        self.input_nama.pack()

        # BUTTON
        frame_btn = tk.Frame(root)
        frame_btn.pack(pady=10)

        tk.Button(frame_btn, text="Ambil Antrian", command=self.ambil).grid(row=0, column=0, padx=5)
        tk.Button(frame_btn, text="Tampilkan", command=self.tampil).grid(row=0, column=1, padx=5)
        tk.Button(frame_btn, text="Panggil", command=self.panggil).grid(row=0, column=2, padx=5)

        # OUTPUT
        self.text_area = tk.Text(root, height=15)
        self.text_area.pack()

    # ======================
    # FUNCTION
    # ======================
    def ambil(self):
        nama = self.input_nama.get()

        if nama:
            self.antrian.tambah(nama)
            self.text_area.delete(1.0, tk.END)
            self.text_area.insert(tk.END, f"Berhasil ambil antrian untuk {nama}\n")
            self.input_nama.delete(0, tk.END)
        else:
            messagebox.showwarning("Warning", "Nama tidak boleh kosong!")

    def tampil(self):
        self.text_area.delete(1.0, tk.END)

        if self.antrian.is_empty():
            self.text_area.insert(tk.END, "Antrian kosong!\n")
        else:
            self.text_area.insert(tk.END, "Daftar Antrian:\n\n")
            for c in self.antrian.get_all():
                self.text_area.insert(tk.END, str(c) + "\n")

    def panggil(self):
        self.text_area.delete(1.0, tk.END)

        c = self.antrian.panggil()

        if c:
            pesan = f"Memanggil: {c}"
            self.text_area.insert(tk.END, pesan + "\n")

            # SUARA
            teks = f"Nomor {c.nomor}, atas nama {c.nama}, silakan ke loket"
            self.suara.bicara(teks)
        else:
            self.text_area.insert(tk.END, "Antrian kosong!\n")

# ======================
# MAIN
# ======================
if __name__ == "__main__":
    root = tk.Tk()
    app = SimulasiAntrian(root)
    root.mainloop()