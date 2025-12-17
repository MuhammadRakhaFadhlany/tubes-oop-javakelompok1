package model;

public class Buku {

    private String idBuku;
    private String judul;
    private String pengarang;
    private String penerbit;
    private int stok;

    public Buku(String idBuku, String judul, String pengarang, String penerbit, int stok) {
        this.idBuku = idBuku;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.stok = stok;
    }

    /* ================= GETTER ================= */

    public String getIdBuku() {
        return idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public int getStok() {
        return stok;
    }

    /* ================= SETTER ================= */

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    /* ================= LOGIC ================= */

    public int cekStok() {
        return stok;
    }

    public void tambahStok() {
        stok++;
    }

    public void kurangiStok() throws Exception {
        if (stok <= 0) {
            throw new Exception("Stok buku kosong!");
        }
        stok--;
    }

    /* ================= TABLE ================= */

    public Object[] toRow() {
        return new Object[]{
            idBuku, judul, pengarang, penerbit, stok
        };
    }
}
