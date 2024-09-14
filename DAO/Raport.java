package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB_Conn.DB;

public class Raport {

    private DB db;
    private Connection conn;

    public Raport() {
        this.db = new DB();
    }

    // Method to establish connection
    private void connect() throws SQLException {
        this.conn = db.connect();
    }

    // Method to close connection
    private void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public void generateSpaceUsageReport() throws SQLException {
        connect(); // Establish the connection
        String sql = "SELECT s.space_id, s.name AS space_name, " +
                     "COUNT(r.reservation_id) AS total_reservations, " +
                     "SUM(r.count_jour) AS total_days_reserved " +
                     "FROM spaces s " +
                     "LEFT JOIN reservations r ON s.space_id = r.space_id " +
                     "GROUP BY s.space_id, s.name";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            System.out.println("Space Usage Report:");
            System.out.printf("%-10s %-30s %-20s %-20s%n", "Space ID", "Space Name", "Total Reservations", "Total Days Reserved");
            System.out.println("-------------------------------------------------------------");
    
            while (rs.next()) {
                int spaceId = rs.getInt("space_id");
                String spaceName = rs.getString("space_name");
                int totalReservations = rs.getInt("total_reservations");
                int totalDaysReserved = rs.getInt("total_days_reserved");
    
                System.out.printf("%-10d %-30s %-20d %-20d%n", spaceId, spaceName, totalReservations, totalDaysReserved);
            }
        } finally {
            closeConnection(); // Close the connection
        }
    }

    public void generateReservationsReport() throws SQLException {
        connect(); // Establish the connection
        String sql = "SELECT COUNT(r.reservation_id) AS total_reservations, " +
                     "SUM(p.amount) AS total_revenue " +
                     "FROM reservations r " +
                     "JOIN payments p ON r.reservation_id = p.reservation_id " +
                     "WHERE p.status = 'Completed'";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            System.out.println("Reservations Report:");
            System.out.printf("%-20s %-20s%n", "Total Reservations", "Total Revenue");
            System.out.println("------------------------------");
    
            if (rs.next()) {
                int totalReservations = rs.getInt("total_reservations");
                double totalRevenue = rs.getDouble("total_revenue");
    
                System.out.printf("%-20d %-20.2f%n", totalReservations, totalRevenue);
            }
        } finally {
            closeConnection(); // Close the connection
        }
    }

    public void generateRevenueBySpaceReport() throws SQLException {
        connect(); // Establish the connection
        String sql = "SELECT s.space_id, s.name AS space_name, " +
                     "SUM(p.amount) AS total_revenue " +
                     "FROM spaces s " +
                     "JOIN reservations r ON s.space_id = r.space_id " +
                     "JOIN payments p ON r.reservation_id = p.reservation_id " +
                     "WHERE p.status = 'Completed' " +
                     "GROUP BY s.space_id, s.name";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            System.out.println("Revenue by Space Report:");
            System.out.printf("%-10s %-30s %-20s%n", "Space ID", "Space Name", "Total Revenue");
            System.out.println("----------------------------------------");
    
            while (rs.next()) {
                int spaceId = rs.getInt("space_id");
                String spaceName = rs.getString("space_name");
                double totalRevenue = rs.getDouble("total_revenue");
    
                System.out.printf("%-10d %-30s %-20.2f%n", spaceId, spaceName, totalRevenue);
            }
        } finally {
            closeConnection(); // Close the connection
        }
    }
}
