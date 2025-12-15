package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.PerpustakaanService;
import java.awt.*;
import java.io.BufferedReader;
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
        modelBuku = new DefaultTableModel(new String[]{"ID", "Judul", "Pengarang", "Stok", "Aksi"}, 0);
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
                    Buku buku = new Buku(
                        parts[0], parts[1], parts[2], parts[3], 
                        Integer.parseInt(parts[4])
                    );
                    service.tambahBuku(buku);
                    modelBuku.addRow(new Object[]{
                        parts[0], parts[1], parts[2], parts[4], "Pinjam"
                    });
                }
            }
        } catch (IOException | NumberFormatException e) {
            // File belum ada
        }
    }

    private void cariBuku(String keyword) {
        modelBuku.setRowCount(0);
        for (Buku buku : service.getDaftarBuku()) {
            if (buku.getJudul().toLowerCase().contains(keyword.toLowerCase()) || 
                keyword.isEmpty()) {
                modelBuku.addRow(new Object[]{
                    buku.toRow()[0], buku.toRow()[1], buku.toRow()[2], 
                    buku.toRow()[4], "Pinjam"
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
        JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam!");
    }

    private void kembalikanBuku() {
        String buku = JOptionPane.showInputDialog(this, "Nama buku yang dikembalikan:");
        if (buku != null && !buku.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Buku '" + buku + "' berhasil dikembalikan!");
        }
    }
}