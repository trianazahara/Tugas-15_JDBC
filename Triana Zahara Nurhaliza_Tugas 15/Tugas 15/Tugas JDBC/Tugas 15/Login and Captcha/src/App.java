import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;

public class App {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/minimarket";
    static final String USER = "root";
    static final String PASS = "";

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //polymorphisme
        DataCustomer Pelanggan = new Customer();

        Login Kasir = new Login();
        Kasir.login();

        //inputkan data
        Pelanggan.inputDataPelanggan();
        
        //mencetak struk
        Pelanggan.Struk();

        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "INSERT INTO pelanggan (kode_barang, nama_pelanggan, no_hp, alamat) VALUE ('%s', '%s', '%s', '%s' )";
            sql = String.format(sql, Pelanggan.getKodeBarang(), Pelanggan.getNama(), Pelanggan.getNoHP(), Pelanggan.getAlamat());
            stmt.execute(sql);

            while (!conn.isClosed()) {
               showMenu();
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
        e.printStackTrace();
        }

        scanner.close();

    }

    static Scanner scanner = new Scanner(System.in); 
    
    static void showMenu() {
        System.out.println("\n========= MENU UTAMA =========");
        System.out.println("1. Show Data");
        System.out.println("2. Edit Data");
        System.out.println("3. Delete Data");
        System.out.println("0. Keluar");
        System.out.println("");
        System.out.print("PILIHAN> ");

        try {
            Integer pilihan = Integer.parseInt(scanner.nextLine());

            switch (pilihan) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    showData();
                    break;
                case 2:
                    updateData();
                    break;
                case 3:
                    deleteData();
                    break;
                default:
                    System.out.println("Pilihan salah!");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void showData() {
        String sql = "SELECT * FROM pelanggan";

        try {
            rs = stmt.executeQuery(sql);
            
            System.out.println("|          MINIMARKET            |");
            System.out.println("+--------------------------------+");

            while (rs.next()) {
                String kode_barang = rs.getString("kode_barang");
                String nama_pelanggan = rs.getString("nama_pelanggan");
                String no_hp = rs.getString("no_hp");
                String alamat = rs.getString("alamat");
                
                System.out.println(String.format("%s. %s -- %s -- (%s)", kode_barang, nama_pelanggan, alamat, no_hp));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void updateData() {
        try {
            
            // ambil input dari user
            System.out.print("Kode Barang yang mau diedit : ");
            String kode_barang = scanner.nextLine();            
            System.out.print("Nama pelanggan : ");
            String nama_pelanggan = scanner.nextLine().trim();
            System.out.print("No Hp : ");
            String no_hp = scanner.nextLine();
            System.out.print("Alamat : ");
            String alamat = scanner.nextLine().trim();

            // query update
            String sql = "UPDATE pelanggan SET nama_pelanggan='%s', no_hp='%s', alamat='%s' WHERE kode_barang='%s'";
            sql = String.format(sql, nama_pelanggan, no_hp, alamat, kode_barang);

            // update data Data
            stmt.execute(sql);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteData() {
        try {
            
            // ambil input dari user
            System.out.print("Kode Barang yang mau dihapus : ");
            String kode_barang = (scanner.nextLine());
            
            // buat query hapus
            String sql = String.format("DELETE FROM pelanggan WHERE kode_barang='%s'", kode_barang);

            // hapus data
            stmt.execute(sql);
            
            System.out.println("Data telah terhapus...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}