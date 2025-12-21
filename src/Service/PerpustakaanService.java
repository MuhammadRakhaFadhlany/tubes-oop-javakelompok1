package Service;

import java.util.ArrayList;
import model.*;

public class PerpustakaanService {
    private ArrayList<Buku> daftarBuku = new ArrayList<>();

    public void tambahBuku(Buku b) {
        daftarBuku.add(b);
    }

    public void hapusBuku(int index) {
        daftarBuku.remove(index);
    }

    public ArrayList<Buku> getDaftarBuku() {
        return daftarBuku;
    }

    public Buku cariBuku(String keyword) {
        for (Buku b : daftarBuku) {
            if (b.getJudul().toLowerCase().contains(keyword.toLowerCase())) {
                return b;
            }
        }
        return null;
    }
}
