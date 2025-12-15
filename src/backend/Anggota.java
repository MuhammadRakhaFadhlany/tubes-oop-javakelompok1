package backend;

public class Anggota extends User {
    private String idAnggota;
    private String nama;

    public Anggota(String idAnggota, String nama, String username, String password) {
        super(username, password);
        this.idAnggota = idAnggota;
        this.nama = nama;
    }

    @Override
    public String getRole() {
        return "Anggota";
    }

    public void cariBuku() {}
    public void pinjamBuku() {}
    public void kembalikanBuku() {}
}

