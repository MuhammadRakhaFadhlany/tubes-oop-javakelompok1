package model;

public class PeminjamanDetail {

    private String namaAnggota;
    private String judulBuku;
    private int jumlah;

    public PeminjamanDetail(String namaAnggota, String judulBuku, int jumlah) {
        this.namaAnggota = namaAnggota;
        this.judulBuku = judulBuku;
        this.jumlah = jumlah;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public int getJumlah() {
        return jumlah;
    }

    public Object[] toRow() {
        return new Object[]{ namaAnggota, judulBuku, jumlah };
    }
}

