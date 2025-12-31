package gui;

import Service.PerpustakaanService;
import Service.TransaksiService;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;



public class DashboardPetugas extends JFrame {
     

    private PerpustakaanService service = new PerpustakaanService();
    private DefaultTableModel model;
    private JTable table;
    private static final String DATA_FILE = "data_buku.txt";
    private static final String DATA_PINJAM = "data_pinjam.txt";
    private static TransaksiService transaksiService = new TransaksiService();
    private DefaultTableModel modelPinjam;
    private JTable tablePinjam;
    
    

    public DashboardPetugas() {
       
        setTitle("Dashboard Petugas - Perpustakaan");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Color headerColor = new Color(41, 128, 185);
        Color panelColor  = new Color(245, 246, 250);

        /* ================= HEADER ================= */
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("Dashboard Petugas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("Kelola data buku perpustakaan");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(Color.WHITE);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblDesc, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        

    // ===== TAB DATA BUKU =====
    model = new DefaultTableModel(
        new String[]{"ID", "Judul", "Pengarang", "Penerbit", "Stok"}, 0) {
        public boolean isCellEditable(int r, int c) {
        return false;
    }
        };
    table = new JTable(model);
    table.setRowHeight(26);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    table.setSelectionBackground(new Color(52, 152, 219));
    JScrollPane scrollPaneBuku = new JScrollPane(table);

    // ===== TAB BUKU DIPINJAM =====
    modelPinjam = new DefaultTableModel(
            new String[]{"Nama Anggota", "Judul Buku", "Jumlah"}, 0
    );
    tablePinjam = new JTable(modelPinjam);
    tablePinjam.setRowHeight(26);
    tablePinjam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tablePinjam.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    JScrollPane scrollPanePinjam = new JScrollPane(tablePinjam);

    // ===== GABUNGKAN KE TAB =====
    JTabbedPane tabs = new JTabbedPane();
    tabs.add("Data Buku", scrollPaneBuku);
    tabs.add("Buku Dipinjam", scrollPanePinjam);

    add(tabs, BorderLayout.CENTER);

        /* ================= BUTTON PANEL ================= */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(panelColor);

        JButton btnTambah = new JButton("Tambah Buku");
        JButton btnEdit   = new JButton("Edit Buku");
        JButton btnHapus  = new JButton("Hapus Buku");
        JButton btnLogout = new JButton("Logout");

        styleButton(btnTambah, new Color(46, 204, 113)); // hijau
        styleButton(btnEdit,   new Color(52, 152, 219)); // biru
        styleButton(btnHapus,  new Color(231, 76, 60));  // merah
        styleButton(btnLogout, new Color(127, 140, 141));// abu

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

        loadDataFromFile();
        loadDataPinjam();
    }

    /* ================= STYLE BUTTON ================= */
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setMargin(new Insets(6, 18, 6, 18));
    }

    /* ================= FORM DIALOG ================= */
    private void tambahBuku() {
        showFormDialog("Tambah Buku", -1);
    }

    private void editBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu");
            return;
        }
        showFormDialog("Edit Buku", row);
    }

    private void showFormDialog(String title, int row) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(440, 320);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JTextField txtId = new JTextField();
        JTextField txtJudul = new JTextField();
        JTextField txtPengarang = new JTextField();
        JTextField txtPenerbit = new JTextField();
        JTextField txtStok = new JTextField();

        if (row != -1) {
            txtId.setText(model.getValueAt(row, 0).toString());
            txtId.setEditable(false);
            txtJudul.setText(model.getValueAt(row, 1).toString());
            txtPengarang.setText(model.getValueAt(row, 2).toString());
            txtPenerbit.setText(model.getValueAt(row, 3).toString());
            txtStok.setText(model.getValueAt(row, 4).toString());
        }

        form.add(new JLabel("ID Buku"));      form.add(txtId);
        form.add(new JLabel("Judul"));        form.add(txtJudul);
        form.add(new JLabel("Pengarang"));    form.add(txtPengarang);
        form.add(new JLabel("Penerbit"));     form.add(txtPenerbit);
        form.add(new JLabel("Stok"));         form.add(txtStok);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal  = new JButton("Batal");

        styleButton(btnSimpan, new Color(52, 152, 219));
        styleButton(btnBatal,  new Color(149, 165, 166));

        btnSimpan.addActionListener(e -> {
            try {
                Buku buku = new Buku(
                        txtId.getText(), txtJudul.getText(), txtPengarang.getText(),
                        txtPenerbit.getText(), Integer.parseInt(txtStok.getText()));

                if (row == -1) {
                    service.tambahBuku(buku);
                    model.addRow(buku.toRow());
                } else {
                    model.setValueAt(txtJudul.getText(), row, 1);
                    model.setValueAt(txtPengarang.getText(), row, 2);
                    model.setValueAt(txtPenerbit.getText(), row, 3);
                    model.setValueAt(txtStok.getText(), row, 4);
                }
                saveDataToFile();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Input tidak valid");
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        footer.add(btnSimpan);
        footer.add(btnBatal);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void hapusBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Hapus buku ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.hapusBuku(row);
            model.removeRow(row);
            saveDataToFile();
        }
    }

    /* ================= FILE HANDLER ================= */
    private void saveDataToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Buku b : service.getDaftarBuku()) {
                pw.println(b.getIdBuku() + "|" + b.getJudul() + "|" +
                        b.getPengarang() + "|" + b.getPenerbit() + "|" + b.getStok());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data");
        }
    }

    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                Buku b = new Buku(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]));
                service.tambahBuku(b);
                model.addRow(b.toRow());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca data");
        }
        
    }
    
   private void loadDataPinjam() {
    modelPinjam.setRowCount(0);
    for (PeminjamanDetail p : transaksiService.getDaftarPinjam()) {
        modelPinjam.addRow(p.toRow());
    }
}

}


