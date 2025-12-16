package gui;

import service.PerpustakaanService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import java.awt.*;
import java.io.*;

public class DashboardPetugas extends JFrame {
    PerpustakaanService service = new PerpustakaanService();
    DefaultTableModel model;
    JTable table;
    private static final String DATA_FILE = "data_buku.txt";

    public DashboardPetugas() {
        setTitle("Dashboard Petugas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.add(new JLabel("SISTEM PERPUSTAKAAN - PETUGAS"));
        add(header, BorderLayout.NORTH);

        // Tabel Buku
        model = new DefaultTableModel(new String[]{"ID", "Judul", "Pengarang", "Penerbit", "Stok"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol Aksi
        JPanel buttonPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnLogout = new JButton("Logout");

        btnTambah.addActionListener(e -> tambahBuku());
        btnEdit.addActionListener(e -> editBuku());
        btnHapus.addActionListener(e -> hapusBuku());
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data dari file
        loadDataFromFile();
    }

    private void tambahBuku() {
        JDialog dialog = new JDialog(this, "Tambah Buku", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField txtId = new JTextField();
        JTextField txtJudul = new JTextField();
        JTextField txtPengarang = new JTextField();
        JTextField txtPenerbit = new JTextField();
        JTextField txtStok = new JTextField();

        dialog.add(new JLabel("ID Buku:"));
        dialog.add(txtId);
        dialog.add(new JLabel("Judul:"));
        dialog.add(txtJudul);
        dialog.add(new JLabel("Pengarang:"));
        dialog.add(txtPengarang);
        dialog.add(new JLabel("Penerbit:"));
        dialog.add(txtPenerbit);
        dialog.add(new JLabel("Stok:"));
        dialog.add(txtStok);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");

        btnSimpan.addActionListener(e -> {
            try {
                Buku buku = new Buku(
                    txtId.getText(),
                    txtJudul.getText(),
                    txtPengarang.getText(),
                    txtPenerbit.getText(),
                    Integer.parseInt(txtStok.getText())
                );
                service.tambahBuku(buku);
                model.addRow(buku.toRow());
                saveDataToFile();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Input tidak valid!");
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.add(btnSimpan);
        dialog.add(btnBatal);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Buku", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField txtId = new JTextField(model.getValueAt(row, 0).toString());
        JTextField txtJudul = new JTextField(model.getValueAt(row, 1).toString());
        JTextField txtPengarang = new JTextField(model.getValueAt(row, 2).toString());
        JTextField txtPenerbit = new JTextField(model.getValueAt(row, 3).toString());
        JTextField txtStok = new JTextField(model.getValueAt(row, 4).toString());

        txtId.setEditable(false);

        dialog.add(new JLabel("ID Buku:"));
        dialog.add(txtId);
        dialog.add(new JLabel("Judul:"));
        dialog.add(txtJudul);
        dialog.add(new JLabel("Pengarang:"));
        dialog.add(txtPengarang);
        dialog.add(new JLabel("Penerbit:"));
        dialog.add(txtPenerbit);
        dialog.add(new JLabel("Stok:"));
        dialog.add(txtStok);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");

        btnSimpan.addActionListener(e -> {
            model.setValueAt(txtJudul.getText(), row, 1);
            model.setValueAt(txtPengarang.getText(), row, 2);
            model.setValueAt(txtPenerbit.getText(), row, 3);
            model.setValueAt(txtStok.getText(), row, 4);
            saveDataToFile();
            dialog.dispose();
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.add(btnSimpan);
        dialog.add(btnBatal);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void hapusBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku dulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.hapusBuku(row);
            model.removeRow(row);
            saveDataToFile();
        }
    }

    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Buku buku : service.getDaftarBuku()) {
                writer.println(buku.toRow()[0] + "|" + buku.toRow()[1] + "|" + 
                               buku.toRow()[2] + "|" + buku.toRow()[3] + "|" + buku.toRow()[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Buku buku = new Buku(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
                    service.tambahBuku(buku);
                    model.addRow(buku.toRow());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}