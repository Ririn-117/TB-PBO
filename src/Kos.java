// Kelas induk Kos untuk properti dasar
public class Kos {
    String alamat;
    int jumlahKamar;

    // Konstruktor
    public Kos(String alamat, int jumlahKamar) {
        this.alamat = alamat;
        this.jumlahKamar = jumlahKamar;
    }

    // Getter dan setter (opsional)
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getJumlahKamar() {
        return jumlahKamar;
    }

    public void setJumlahKamar(int jumlahKamar) {
        this.jumlahKamar = jumlahKamar;
    }
}
