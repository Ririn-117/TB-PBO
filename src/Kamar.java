import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

// Subclass Kamar
class Kamar extends Kos implements CrudOperations {
    int nomorKamar;
    boolean tersedia;
    double harga;
    String username;
    String password;
    String captcha;
    Date tanggalRegistrasi;  // Untuk mencatat waktu pendaftaran kamar

    public Kamar(String alamat, int jumlahKamar, int nomorKamar, boolean tersedia, double harga, String username, String password, String captcha, Date tanggalRegistrasi) {
        super(alamat, jumlahKamar);
        this.nomorKamar = nomorKamar;
        this.tersedia = tersedia;
        this.harga = harga;
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    @Override
    public void create() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO kamar (alamat, jumlah_kamar, nomor_kamar, tersedia, harga, username, password, captcha, tanggal_registrasi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, alamat);
            stmt.setInt(2, jumlahKamar);
            stmt.setInt(3, nomorKamar);
            stmt.setBoolean(4, tersedia);
            stmt.setDouble(5, harga);
            stmt.setString(6, username);
            stmt.setString(7, password);
            stmt.setString(8, captcha);
            stmt.setDate(9, new java.sql.Date(tanggalRegistrasi.getTime()));
            stmt.executeUpdate();
            System.out.println("Data kamar berhasil ditambahkan ke database.");
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan data ke database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @Override
    public void read() {
        System.out.println("Nomor Kamar: " + nomorKamar + ", Alamat: " + alamat + ", Tersedia: " + tersedia + ", Harga: " + harga + ", Username: " + username + ", Password: " + password + ", Captcha: " + captcha + ", Tanggal Registrasi: " + tanggalRegistrasi);
    }

    @Override
    public void update() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement stmt = conn.prepareStatement("UPDATE kamar SET harga = ?, username = ?, password = ?, captcha = ?, tanggal_registrasi = ? WHERE nomor_kamar = ?")) {
            stmt.setDouble(1, harga);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, captcha);
            stmt.setDate(5, new java.sql.Date(tanggalRegistrasi.getTime()));
            stmt.setInt(6, nomorKamar);
            stmt.executeUpdate();
            System.out.println("Data kamar berhasil diperbarui di database.");
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui data di database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @Override
    public void delete() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM kamar WHERE nomor_kamar = ?")) {
            stmt.setInt(1, nomorKamar);
            stmt.executeUpdate();
            System.out.println("Data kamar berhasil dihapus dari database.");
        } catch (SQLException e) {
            System.err.println("Gagal menghapus data dari database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
