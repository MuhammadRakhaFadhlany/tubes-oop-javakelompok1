package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.PerpustakaanService;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DashboardAnggota extends JFrame {
    private PerpustakaanService service = new PerpustakaanService();
    private DefaultTableModel modelBuku;
    private JTable tableBuku;

    public DashboardAnggota(Anggota anggota) {
        setTitle("Dashboard Anggota");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.add(new JLabel("Selamat Datang, Anggota!"));
        add(header, BorderLayout.NORTH);

        // Tabel Buku
        modelBuku = new DefaultTableModel(new String[]{"ID", "Judul", "Pengarang", "Stok", "Keterangan"}, 0);
        tableBuku = new JTable(modelBuku);
        add(new JScrollPane(tableBuku), BorderLayout.CENTER);

        // Panel Pencarian
        JPanel searchPanel = new JPanel();
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Cari");
        
        btnSearch.addActionListener(e -> cariBuku(txtSearch.getText()));
        searchPanel.add(new JLabel("Cari: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Panel Tombol
        JPanel buttonPanel = new JPanel();
        JButton btnPinjam = new JButton("Pinjam Buku");
        JButton btnKembalikan = new JButton("Kembalikan");
        JButton btnLogout = new JButton("Logout");

        btnPinjam.addActionListener(e -> pinjamBuku());
        btnKembalikan.addActionListener(e -> kembalikanBuku());
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        buttonPanel.add(btnPinjam);
        buttonPanel.add(btnKembalikan);
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data dari file
        loadDataFromFile();
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data_buku.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    int stok = Integer.parseInt(parts[4]);

                   
                    String keterangan = stok > 0 ? "Tersedia" : "Tidak Tersedia";
                   

                    modelBuku.addRow(new Object[]{
                            parts[0], parts[1], parts[2], stok, keterangan
                    });

                    Buku buku = new Buku(
                            parts[0], parts[1], parts[2], parts[3], stok);
                    service.tambahBuku(buku);

                }
            }
        } catch (FileNotFoundException e) {
            // File belum ada, asumsikan startup awal, tidak ada data yang dimuat.
        } catch (IOException e) {
            // Terjadi error I/O lain saat membaca file.
            e.printStackTrace(); // Penting untuk melihat error I/O lainnya
        } catch (NumberFormatException e) {
            // Data dalam file (parts[4]) tidak valid (bukan angka).
            System.err.println("Peringatan: Data stok tidak valid di salah satu baris.");
        }
    }

    private void cariBuku(String keyword) {
        modelBuku.setRowCount(0);
        for (Buku buku : service.getDaftarBuku()) {
            if (buku.getJudul().toLowerCase().contains(keyword.toLowerCase()) || 
                keyword.isEmpty()) {
                int stok = Integer.parseInt(buku.toRow()[4].toString());

                String keterangan = stok > 0 ? "Tersedia" : "Tidak Tersedia";

            modelBuku.addRow(new Object[]{
                buku.toRow()[0],
                buku.toRow()[1],
                buku.toRow()[2],
                stok,
                keterangan
            });
            }
        }
    }

    private void pinjamBuku() {
        int row = tableBuku.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            return;
        }

        int stok = Integer.parseInt(modelBuku.getValueAt(row, 3).toString());
        if (stok <= 0) {
            JOptionPane.showMessageDialog(this, "Stok habis!");
            return;
        }

        stok--;
        modelBuku.setValueAt(stok, row, 3);

        String ket = stok > 0 ? "Tersedia" : "Tidak Tersedia";
        modelBuku.setValueAt(ket, row, 4);

        JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam!");

    }

    private void kembalikanBuku() {
    String judul = JOptionPane.showInputDialog(this, "Nama buku yang dikembalikan:");

    if (judul == null || judul.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Judul buku tidak boleh kosong!");
        return;
    }

    boolean ditemukan = false;

    for (int i = 0; i < modelBuku.getRowCount(); i++) {
        String judulTabel = modelBuku.getValueAt(i, 1).toString();

        if (judulTabel.equalsIgnoreCase(judul.trim())) {
            int stok = Integer.parseInt(modelBuku.getValueAt(i, 3).toString());
            stok++;

            modelBuku.setValueAt(stok, i, 3);
            modelBuku.setValueAt("Tersedia", i, 4);

            JOptionPane.showMessageDialog(this,
                    "Buku \"" + judulTabel + "\" berhasil dikembalikan!");
            ditemukan = true;
            break;
        }
    }

    if (!ditemukan) {
        JOptionPane.showMessageDialog(this,
                "Buku tidak ditemukan di daftar!");
    }
}

}