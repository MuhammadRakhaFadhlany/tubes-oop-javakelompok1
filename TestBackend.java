
import backend.*;
import Service.PerpustakaanService;

public class TestBackend {
    public static void main(String[] args) {

        System.out.println("=== TEST BACKEND PERPUSTAKAAN ===");
        PerpustakaanService service = new PerpustakaanService();

        
        Buku buku1 = new Buku("B01", "Java OOP", "Eko", "Informatika", 1);
        service.tambahBuku(buku1);
        System.out.println("Tambah buku: " + buku1.getJudul());

        System.out.println("Stok awal: " + buku1.cekStok());

        try {
            Peminjaman p = new Peminjaman("PJM01", buku1);
            p.simpanPeminjaman();
            System.out.println("Buku dipinjam");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            Peminjaman p2 = new Peminjaman("PJM02", buku1);
            p2.simpanPeminjaman();
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        Pengembalian k = new Pengembalian("KMB01", buku1);
        k.updatePengembalian();
        System.out.println("Buku dikembalikan");

        System.out.println("Stok akhir: " + buku1.cekStok());

        System.out.println("=== TEST SELESAI ===");
    }
}
