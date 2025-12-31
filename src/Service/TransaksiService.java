package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import model.PeminjamanDetail;

public class TransaksiService {
    private ArrayList<PeminjamanDetail> daftarPinjam = new ArrayList<>();
    private static final String DATA_PINJAM = "data_pinjam.txt";

    public void tambahPinjaman(PeminjamanDetail p) {
    daftarPinjam.add(p);

    try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_PINJAM, true))) {
        pw.println(p.getNamaAnggota() + "|" + p.getJudulBuku() + "|" + p.getJumlah());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public ArrayList<PeminjamanDetail> getDaftarPinjam() {
    daftarPinjam.clear();

    File file = new File(DATA_PINJAM);
    if (!file.exists()) return daftarPinjam;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("\\|");

            daftarPinjam.add(new PeminjamanDetail(
                    p[0],
                    p[1],
                    Integer.parseInt(p[2])
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return daftarPinjam;
}

}

