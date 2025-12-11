import java.sql.*;
import java.util.*;
public class HotelManagementSystem {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/paymentsdb?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";    
    private static final String JDBC_PASSWORD = ""; 
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) {
      
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found on classpath. Place the connector jar and re-run.");
            e.printStackTrace();
            return;
        }

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS paymentsdb");
            st.execute("USE paymentsdb");
            st.execute("CREATE TABLE IF NOT EXISTS bookings ("
                    + "bookingId INT PRIMARY KEY, "
                    + "guestName VARCHAR(100) NOT NULL, "
                    + "roomNumber INT NOT NULL, "
                    + "roomType VARCHAR(20) NOT NULL, "
                    + "days INT NOT NULL, "
                    + "totalBill DOUBLE NOT NULL, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");
        } catch (SQLException e) {
            System.err.println("Failed to create/check bookings table. Check DB connection & credentials.");
            e.printStackTrace();
            return;
        }

        Scanner sc = new Scanner(System.in);
        BookingDAO dao = new BookingDAO();

        while (true) {
            System.out.println("=== Hotel Management System ===");
            System.out.println("1. Add Booking");
            System.out.println("2. Update Booking");
            System.out.println("3. Remove Booking");
            System.out.println("4. Search Booking");
            System.out.println("5. List All Bookings");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            int choice;
            try { choice = Integer.parseInt(line); }
            catch (NumberFormatException nfe) { System.out.println("Invalid input.\n"); continue; }

            try {
                if (choice == 1) {
                    System.out.print("Enter Booking ID: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter Room Number: ");
                    int room = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Room Type (AC/Non-AC): ");
                    String type = sc.nextLine().trim();
                    System.out.print("Enter Number of Days: ");
                    int days = Integer.parseInt(sc.nextLine().trim());

                    int ratePerDay = type.equalsIgnoreCase("AC") ? 2000 : 1000;
                    double totalBill = ratePerDay * days;

                    Booking b = new Booking(id, name, room, type, days, totalBill);
                    if (dao.addBooking(b)) System.out.println("Booking added. Total Bill: " + totalBill + "\n");
                    else System.out.println("Failed to add booking (maybe duplicate ID).\n");

                } else if (choice == 2) {
                    System.out.print("Enter Booking ID to update: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Booking existing = dao.getBookingById(id);
                    if (existing == null) { System.out.println("Booking not found.\n"); continue; }

                    System.out.print("Enter new Guest Name: ");
                    existing.guestName = sc.nextLine().trim();
                    System.out.print("Enter new Room Number: ");
                    existing.roomNumber = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter new Room Type (AC/Non-AC): ");
                    existing.roomType = sc.nextLine().trim();
                    System.out.print("Enter new Number of Days: ");
                    existing.days = Integer.parseInt(sc.nextLine().trim());

                    int ratePerDay = existing.roomType.equalsIgnoreCase("AC") ? 2000 : 1000;
                    existing.totalBill = ratePerDay * existing.days;

                    if (dao.updateBooking(existing)) System.out.println("Booking updated. New Total Bill: " + existing.totalBill + "\n");
                    else System.out.println("Update failed.\n");

                } else if (choice == 3) {
                    System.out.print("Enter Booking ID to remove: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    if (dao.removeBooking(id)) System.out.println("Booking removed.\n");
                    else System.out.println("Booking not found or delete failed.\n");

                } else if (choice == 4) {
                    System.out.print("Enter Booking ID to search: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Booking b = dao.getBookingById(id);
                    if (b != null) { System.out.println("Booking found:"); b.display(); System.out.println(); }
                    else System.out.println("Booking not found.\n");

                } else if (choice == 5) {
                    List<Booking> all = dao.getAllBookings();
                    if (all.isEmpty()) System.out.println("No bookings.\n");
                    else {
                        System.out.println("All bookings:");
                        for (Booking bk : all) bk.display();
                        System.out.println();
                    }

                } else if (choice == 6) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice.\n");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number input. Try again.\n");
            }
        }

        sc.close();
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    static class Booking {
        int bookingId;
        String guestName;
        int roomNumber;
        String roomType;
        int days;
        double totalBill;

        Booking(int bookingId, String guestName, int roomNumber, String roomType, int days, double totalBill) {
            this.bookingId = bookingId;
            this.guestName = guestName;
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.days = days;
            this.totalBill = totalBill;
        }

        void display() {
            System.out.println("Booking ID: " + bookingId +
                    " | Guest: " + guestName +
                    " | Room: " + roomNumber +
                    " | Type: " + roomType +
                    " | Days: " + days +
                    " | Total Bill: " + totalBill);
        }
    }

    static class BookingDAO {

        boolean addBooking(Booking b) {
            String sql = "INSERT INTO bookings (bookingId, guestName, roomNumber, roomType, days, totalBill) VALUES(?,?,?,?,?,?)";
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, b.bookingId);
                ps.setString(2, b.guestName);
                ps.setInt(3, b.roomNumber);
                ps.setString(4, b.roomType);
                ps.setInt(5, b.days);
                ps.setDouble(6, b.totalBill);
                return ps.executeUpdate() == 1;
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Booking with this ID already exists.");
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        boolean updateBooking(Booking b) {
            String sql = "UPDATE bookings SET guestName=?, roomNumber=?, roomType=?, days=?, totalBill=? WHERE bookingId=?";
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, b.guestName);
                ps.setInt(2, b.roomNumber);
                ps.setString(3, b.roomType);
                ps.setInt(4, b.days);
                ps.setDouble(5, b.totalBill);
                ps.setInt(6, b.bookingId);
                return ps.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        boolean removeBooking(int bookingId) {
            String sql = "DELETE FROM bookings WHERE bookingId = ?";
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, bookingId);
                return ps.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        Booking getBookingById(int bookingId) {
            String sql = "SELECT bookingId, guestName, roomNumber, roomType, days, totalBill FROM bookings WHERE bookingId = ?";
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, bookingId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Booking(
                                rs.getInt("bookingId"),
                                rs.getString("guestName"),
                                rs.getInt("roomNumber"),
                                rs.getString("roomType"),
                                rs.getInt("days"),
                                rs.getDouble("totalBill")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        List<Booking> getAllBookings() {
            String sql = "SELECT bookingId, guestName, roomNumber, roomType, days, totalBill FROM bookings ORDER BY created_at DESC";
            List<Booking> list = new ArrayList<>();
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking(
                            rs.getInt("bookingId"),
                            rs.getString("guestName"),
                            rs.getInt("roomNumber"),
                            rs.getString("roomType"),
                            rs.getInt("days"),
                            rs.getDouble("totalBill")
                    );
                    list.add(b);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
