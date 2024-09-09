import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import DB_Conn.DB;
import interfaces.Allclass.Service;
import interfaces.ServicesInterface;

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
        System.out.println(servicesMap);
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


}
