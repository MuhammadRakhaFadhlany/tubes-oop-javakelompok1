package backend;
import java.util.Date;

public class Peminjaman {
    private String idPeminjaman;
    private Date tanggalPinjam;
    private String status;
    private Buku buku;

    public Peminjaman(String idPeminjaman, Buku buku) {
        this.idPeminjaman = idPeminjaman;
        this.buku = buku;
        this.tanggalPinjam = new Date();
        this.status = "Dipinjam";
    }

    public void simpanPeminjaman() throws Exception {
        buku.kurangiStok();
    }

    public Buku getBuku() {
        return buku;
    }
}
