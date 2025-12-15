package gui;

import service.PerpustakaanService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class DashboardPetugas extends JFrame {
    PerpustakaanService service = new PerpustakaanService();
    DefaultTableModel model;

    public DashboardPetugas() {
        setTitle("Dashboard Petugas");
        setSize(600,400);

        model = new DefaultTableModel(
            new String[]{"ID","Judul","Pengarang","Penerbit","Stok"},0
        );

        JTable table = new JTable(model);
        JButton btnTambah = new JButton("Tambah Buku");

        btnTambah.addActionListener(e -> tambah());

        add(new JScrollPane(table));
        add(btnTambah,"South");
    }

    private void tambah() {
        Buku b = new Buku("B01","Pemrograman Java","Eko","Informatika",1);
        service.tambahBuku(b);
        model.addRow(b.toRow());
    }
}
