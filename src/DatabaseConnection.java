import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try (FileInputStream fis = new FileInputStream("dbconfig.properties")) {
            // Membaca file properties
            Properties props = new Properties();
            props.load(fis);

            // Mengambil properti dari file
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Membuat koneksi ke database
            conn = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
        }
        return conn;
    }
}
