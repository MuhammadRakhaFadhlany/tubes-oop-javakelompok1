package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import Service.PerpustakaanService;
import java.awt.*;
import java.io.*;

public class DashboardAnggota extends JFrame {

    private PerpustakaanService service = new PerpustakaanService();
    private DefaultTableModel modelBuku;
    private JTable tableBuku;
    private JTextField txtSearch;

    public DashboardAnggota(Anggota anggota) {
        setTitle("Dashboard Anggota - Perpustakaan");
        setSize(900, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Color headerColor = new Color(41, 128, 185);
        Color panelColor  = new Color(245, 246, 250);
        Color buttonBlue  = new Color(52, 152, 219);
        Color buttonRed   = new Color(231, 76, 60);

        /* ================= HEADER ================= */
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("Dashboard Anggota");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Login sebagai: " + anggota.getNama());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(Color.WHITE);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        /* ================= SEARCH ================= */
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(panelColor);

        txtSearch = new JTextField(25);
        JButton btnSearch = new JButton("Cari Buku");
        styleButton(btnSearch, buttonBlue);

        btnSearch.addActionListener(e -> cariBuku(txtSearch.getText()));

        searchPanel.add(new JLabel("Cari Judul:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        /* ================= TABLE ================= */
        modelBuku = new DefaultTableModel(
                new String[]{"ID", "Judul", "Pengarang", "Stok", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableBuku = new JTable(modelBuku);
        tableBuku.setRowHeight(26);
        tableBuku.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableBuku.setSelectionBackground(new Color(52, 152, 219));
        tableBuku.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tableBuku);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        /* ================= BUTTON ================= */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(panelColor);

        JButton btnPinjam = new JButton("Pinjam Buku");
        JButton btnKembali = new JButton("Kembalikan Buku");
        JButton btnLogout = new JButton("Logout");

        styleButton(btnPinjam, buttonBlue);
        styleButton(btnKembali, new Color(46, 204, 113));
        styleButton(btnLogout, buttonRed);

        btnPinjam.addActionListener(e -> pinjamBuku());
        btnKembali.addActionListener(e -> kembalikanBuku());
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        buttonPanel.add(btnPinjam);
        buttonPanel.add(btnKembali);
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataFromFile();
    }

    /* ================= STYLE BUTTON ================= */
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(null);   // biarkan layout mengatur
        btn.setMargin(new Insets(5, 15, 5, 15)); // padding biar rapi
    }

    /* ================= LOAD DATA ================= */
    private void loadDataFromFile() {
        File file = new File("data_buku.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                int stok = Integer.parseInt(p[4]);
                String status = stok > 0 ? "Tersedia" : "Tidak Tersedia";

                Buku buku = new Buku(p[0], p[1], p[2], p[3], stok);
                service.tambahBuku(buku);

                modelBuku.addRow(new Object[]{
                        buku.getIdBuku(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        buku.getStok(),
                        status
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca data buku");
        }
    }

    /* ================= SEARCH ================= */
    private void cariBuku(String keyword) {
        modelBuku.setRowCount(0);

        for (Buku buku : service.getDaftarBuku()) {
            if (buku.getJudul().toLowerCase().contains(keyword.toLowerCase())
                    || keyword.isEmpty()) {

                int stok = buku.getStok();
                String status = stok > 0 ? "Tersedia" : "Tidak Tersedia";

                modelBuku.addRow(new Object[]{
                        buku.getIdBuku(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        stok,
                        status
                });
            }
        }
    }

    /* ================= PINJAM ================= */
    private void pinjamBuku() {
        int row = tableBuku.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu");
            return;
        }

        String id = modelBuku.getValueAt(row, 0).toString();

        for (Buku buku : service.getDaftarBuku()) {
            if (buku.getIdBuku().equals(id)) {
                if (buku.getStok() <= 0) {
                    JOptionPane.showMessageDialog(this, "Stok buku tidak tersedia");
                    return;
                }
                buku.setStok(buku.getStok() - 1);
                break;
            }
        }

        cariBuku(txtSearch.getText());
        JOptionPane.showMessageDialog(this, "Buku berhasil dipinjam");
    }

    /* ================= KEMBALI ================= */
    private void kembalikanBuku() {
        String judul = JOptionPane.showInputDialog(this, "Masukkan judul buku:");
        if (judul == null || judul.trim().isEmpty()) return;

        for (Buku buku : service.getDaftarBuku()) {
            if (buku.getJudul().equalsIgnoreCase(judul.trim())) {
                buku.setStok(buku.getStok() + 1);
                cariBuku(txtSearch.getText());
                JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Buku tidak ditemukan");
    }
}
