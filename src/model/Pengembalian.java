package model;

import java.util.Date;

public class Pengembalian {
    private String idPengembalian;
    private Date tanggalKembali;
    private Buku buku;

    public Pengembalian(String idPengembalian, Buku buku) {
        this.idPengembalian = idPengembalian;
        this.buku = buku;
        this.tanggalKembali = new Date();
    }

    public void updatePengembalian() {
        buku.tambahStok();
    }
}

