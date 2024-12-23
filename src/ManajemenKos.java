import java.security.SecureRandom;  // Untuk menghasilkan CAPTCHA acak
import java.sql.Connection;         // Untuk koneksi ke database PostgreSQL
import java.sql.Date;               // Untuk tipe data tanggal
import java.sql.DriverManager;     // Untuk menghubungkan aplikasi dengan database
import java.sql.PreparedStatement; // Untuk menyiapkan dan mengeksekusi query
import java.sql.ResultSet;         // Untuk mengambil hasil query dari database
import java.sql.SQLException;      // Untuk menangani error SQL
import java.sql.Statement;         // Untuk menjalankan pernyataan SQL
import java.text.SimpleDateFormat; // Untuk memformat tanggal dan waktu
import java.util.Scanner;          // Untuk input pengguna

public class ManajemenKos {

    // Program utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Objek untuk input dari pengguna

        // Menampilkan Tanggal dan Waktu saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");  // Format tanggal
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");       // Format waktu
        Date date = new Date(System.currentTimeMillis());  // Mengambil tanggal saat ini
        
        System.out.println("Tanggal: " + dateFormat.format(date));  // Menampilkan tanggal
        System.out.println("Waktu: " + timeFormat.format(date));    // Menampilkan waktu

        // Login terlebih dahulu
        if (!login(scanner)) {
            System.out.println("Login gagal! Program dihentikan.");
            return;
        }

        // Menu utama untuk mengelola kamar
        while (true) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Lihat Semua Kamar");
            System.out.println("2. Tambah Data Kamar");
            System.out.println("3. Update Data Kamar");
            System.out.println("4. Hapus Kamar");
            System.out.println("5. Keluar");

            System.out.print("Pilih operasi: ");
            int pilihan = scanner.nextInt();  // Input pilihan menu
            scanner.nextLine();  // Konsumsi newline

            // Percabangan untuk memilih menu
            switch (pilihan) {
                case 1:
                    lihatKamar();  // Menampilkan semua kamar
                    break;
                case 2:
                    tambahKamar(scanner);  // Menambah kamar baru
                    break;
                case 3:
                    updateKamar(scanner);  // Mengupdate kamar
                    break;
                case 4:
                    hapusKamar(scanner);  // Menghapus kamar
                    break;
                case 5:
                    System.out.println("Keluar dari program.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Fungsi Login dengan CAPTCHA acak
    private static boolean login(Scanner scanner) {
        String captcha = generateCaptcha();  // Menghasilkan CAPTCHA acak
        System.out.println("\nSELAMAT DATANG DI PROGRAM MANAJEMEN KOS UTAERIN");
        System.out.println("\nSILAHKAN LOG IN TERLEBIH DAHULU");
        System.out.print("Username: ");
        String username = scanner.nextLine();  // Input username
        System.out.print("Password: ");
        String password = scanner.nextLine();  // Input password
        System.out.println("Captcha: " + captcha);  // Menampilkan CAPTCHA

        System.out.print("Masukkan Captcha (case insensitive): ");
        String userCaptcha = scanner.nextLine();  // Input CAPTCHA dari pengguna

        // Validasi login (username, password, dan CAPTCHA)
        if (username.equals("Admin_Ririn") && password.equals("Ririn123") && userCaptcha.equalsIgnoreCase(captcha)) {
            System.out.println("Log in berhasil!");
            return true;
        } else {
            System.out.println("Captcha salah atau kredensial tidak valid.");
            return false;
        }
    }

    // Fungsi untuk menghasilkan CAPTCHA secara acak
    private static String generateCaptcha() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";  // Karakter yang digunakan untuk CAPTCHA
        SecureRandom random = new SecureRandom();  // Objek untuk menghasilkan angka acak
        StringBuilder captcha = new StringBuilder(6);  // StringBuilder untuk membangun CAPTCHA
        
        // Perulangan untuk menghasilkan CAPTCHA 6 karakter
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());  // Mengambil indeks acak
            captcha.append(characters.charAt(index));  // Menambahkan karakter acak
        }
        
        return captcha.toString();  // Mengembalikan CAPTCHA
    }

    // Fungsi untuk menampilkan semua kamar dari database
    private static void lihatKamar() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             Statement stmt = conn.createStatement()) {
            
            System.out.println("\nInformasi kamar di database:");
            ResultSet rs = stmt.executeQuery("SELECT * FROM kamar");  // Menjalankan query untuk mengambil semua data kamar
            // Perulangan untuk menampilkan setiap kamar
            while (rs.next()) {
                System.out.println("ID Kamar: " + rs.getString("id_kamar") +
                        ", Nama Kamar: " + rs.getString("nama_kamar") +
                        ", Tipe Kamar: " + rs.getString("tipe_kamar") +
                        ", Harga/Bulan: " + rs.getDouble("harga_per_bulan") +
                        ", Status Kamar: " + rs.getString("status_kamar") +
                        ", Fasilitas: " + rs.getString("fasilitas") +
                        ", Ukuran Kamar: " + rs.getString("ukuran_kamar") +
                        ", Tanggal: " + rs.getDate("tanggal"));
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan: " + e.getMessage());  // Exception handling jika terjadi kesalahan SQL
        }
    }

    // Fungsi untuk menambah kamar ke database
    private static void tambahKamar(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO kamar (id_kamar, nama_kamar, tipe_kamar, harga_per_bulan, status_kamar, fasilitas, ukuran_kamar, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            // Input data kamar dari pengguna
            System.out.print("ID Kamar: ");
            String idKamar = scanner.nextLine();
            System.out.print("Nama Kamar: ");
            String namaKamar = scanner.nextLine();
            System.out.print("Tipe Kamar: ");
            String tipeKamar = scanner.nextLine();
            System.out.print("Harga per bulan: ");
            double hargaPerBulan = scanner.nextDouble();
            scanner.nextLine();  // Konsumsi newline
            System.out.print("Status Kamar (kosong/terisi): ");
            String statusKamar = scanner.nextLine();
            System.out.print("Fasilitas: ");
            String fasilitas = scanner.nextLine();
            System.out.print("Ukuran Kamar: ");
            String ukuranKamar = scanner.nextLine();

            // Menyimpan data kamar ke database
            pstmt.setString(1, idKamar);
            pstmt.setString(2, namaKamar);
            pstmt.setString(3, tipeKamar);
            pstmt.setDouble(4, hargaPerBulan);
            pstmt.setString(5, statusKamar);
            pstmt.setString(6, fasilitas);
            pstmt.setString(7, ukuranKamar);
            pstmt.setDate(8, new java.sql.Date(System.currentTimeMillis()));  // Menyimpan tanggal saat ini

            pstmt.executeUpdate();  // Eksekusi query untuk menambah data
            System.out.println("Kamar berhasil ditambahkan.");
        } catch (SQLException e) {
            System.err.println("Kesalahan: " + e.getMessage());  // Exception handling jika terjadi kesalahan SQL
        }
    }

    // Fungsi untuk mengupdate data kamar di database
    private static void updateKamar(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement pstmt = conn.prepareStatement("UPDATE kamar SET nama_kamar = ?, tipe_kamar = ?, status_kamar = ?, fasilitas = ?, ukuran_kamar = ? WHERE id_kamar = ?")) {
            
            System.out.print("Masukkan ID Kamar yang ingin diupdate: ");
            String idKamar = scanner.nextLine();

            // Input data baru untuk kamar yang ingin diupdate
            System.out.print("Masukkan nama kamar baru: ");
            String namaKamar = scanner.nextLine();
            System.out.print("Masukkan tipe kamar baru: ");
            String tipeKamar = scanner.nextLine();
            System.out.print("Masukkan status kamar baru (kosong/terisi): ");
            String statusKamar = scanner.nextLine();
            System.out.print("Masukkan fasilitas baru: ");
            String fasilitas = scanner.nextLine();
            System.out.print("Masukkan ukuran kamar baru: ");
            String ukuranKamar = scanner.nextLine();

            // Menyimpan perubahan data ke database
            pstmt.setString(1, namaKamar);
            pstmt.setString(2, tipeKamar);
            pstmt.setString(3, statusKamar);
            pstmt.setString(4, fasilitas);
            pstmt.setString(5, ukuranKamar);
            pstmt.setString(6, idKamar);
            pstmt.executeUpdate();  // Eksekusi query untuk update data

            System.out.println("Data kamar berhasil diupdate.");
        } catch (SQLException e) {
            System.err.println("Kesalahan: " + e.getMessage());  // Exception handling jika terjadi kesalahan SQL
        }
    }

    // Fungsi untuk menghapus kamar dari database
    private static void hapusKamar(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_kos", "postgres", "130904");
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM kamar WHERE id_kamar = ?")) {
            
            System.out.print("Masukkan ID Kamar yang ingin dihapus: ");
            String idKamar = scanner.nextLine();

            pstmt.setString(1, idKamar);  // Mengatur parameter ID kamar
            pstmt.executeUpdate();  // Eksekusi query untuk menghapus kamar

            System.out.println("Kamar berhasil dihapus.");
        } catch (SQLException e) {
            System.err.println("Kesalahan: " + e.getMessage());  // Exception handling jika terjadi kesalahan SQL
        }
    }
}
