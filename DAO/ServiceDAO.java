package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB_Conn.DB;
import interfaces.ServicesInterface;
import model.Service;

public class ServiceDAO implements ServicesInterface {
    private DB db;

    public ServiceDAO(DB db) {
        this.db = db;
    }

    @Override
    public Map<Integer, Service> findAllServices() {
        Map<Integer, Service> servicesMap = new HashMap<>();
        String sql = "SELECT * FROM services";
        try (Connection connection = db.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Service service = new Service();
                // Retrieve serviceId from ResultSet
                int serviceId = rs.getInt("service_id");
                service.setServiceId(serviceId); // Ensure you set the serviceId

                service.setName(rs.getString("name"));
                service.setDescription(rs.getString("description"));
                service.setPrice(rs.getDouble("price"));

                // Add service to map with serviceId as key
                servicesMap.put(serviceId, service);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching services", e);
        }
        return servicesMap;
    }

    @Override
    public void addServiceToSpace(int spaceId, int serviceId) throws SQLException {
        String sql = "INSERT INTO service_spaces (service_id, spaces_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = db.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            pstmt.setInt(2, spaceId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add service to space: " + e.getMessage());
        }
    }
    public List<Service> getServicesBySpaceId(int spaceId) throws SQLException {
        String sql = "SELECT s.service_id, s.name, s.description, s.price " +
                     "FROM services s " +
                     "JOIN service_spaces ss ON s.service_id = ss.service_id " +
                     "WHERE ss.spaces_id = ?";
        
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, spaceId); // Set the spaceId parameter
            ResultSet rs = stmt.executeQuery();
            
            List<Service> services = new ArrayList<>();
            while (rs.next()) {
                services.add(mapRowToService(rs));
            }
            return services;
        }
    }
    

    public List<Service> getServicesByReservationId(int reservationId) throws SQLException {
        String sql = "SELECT s.service_id, s.name, s.description, s.price " +
                     "FROM services s " +
                     "JOIN service_reservations sr ON s.service_id = sr.service_id " +
                     "WHERE sr.reservation_id = ?";
    
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
    
            List<Service> services = new ArrayList<>();
            while (rs.next()) {
                services.add(mapRowToService(rs));
            }
            return services;
        }
    }
    

    private Service mapRowToService(ResultSet rs) throws SQLException {
        int serviceId = rs.getInt("service_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        double price = rs.getDouble("price"); // Use double for price
        
        return new Service(serviceId, name, description, price);
    }
    



}
