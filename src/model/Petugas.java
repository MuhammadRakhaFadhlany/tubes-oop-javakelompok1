package model;

public class Petugas extends User {
    private String idPetugas;
    private String nama;

    public Petugas(String idPetugas, String nama, String username, String password) {
        super(username, password);
        this.idPetugas = idPetugas;
        this.nama = nama;
    }

    @Override
    public String getRole() {
        return "Petugas";
    }

    public void kelolaBuku() {}
    public void kelolaAnggota() {}
    public void kelolaPeminjaman() {}
}

