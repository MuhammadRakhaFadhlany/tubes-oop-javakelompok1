package backend;
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

    public Object[] toRow() {
        return new Object[]{idBuku, judul, pengarang, penerbit, stok};
    }

    public String getJudul() {
        return judul;
    }
}
