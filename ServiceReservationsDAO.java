import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DB_Conn.DB;
import interfaces.ServiceReservationsInterface;
import interfaces.Allclass.ServiceReservation;

public class ServiceReservationsDAO implements ServiceReservationsInterface {
    private final DB db; // Assuming a DB class to handle connection

    public ServiceReservationsDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addServiceReservation(ServiceReservation serviceReservation) {
        String query = "INSERT INTO service_reservations(service_id, reservation_id, price, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceReservation.getServiceId());
            stmt.setInt(2, serviceReservation.getReservationId());
            stmt.setDouble(3, serviceReservation.getPrice());
            stmt.setTimestamp(4, serviceReservation.getCreatedAt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceReservation getServiceReservationById(int serviceReservationId) {
        String query = "SELECT * FROM service_reservations WHERE service_reservation_id = ?";
        try (Connection conn = db.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceReservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ServiceReservation(
                    rs.getInt("service_reservation_id"),
                    rs.getInt("service_id"),
                    rs.getInt("reservation_id"),
                    rs.getDouble("price"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ServiceReservation> getAllServiceReservations() {
        List<ServiceReservation> serviceReservations = new ArrayList<>();
        String query = "SELECT * FROM service_reservations";
        try (Connection conn = db.connect(); Statement stmt = conn.createStatement(); ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query)) {
            while (rs.next()) {
                ServiceReservation serviceReservation = new ServiceReservation(
                    rs.getInt("service_reservation_id"),
                    rs.getInt("service_id"),
                    rs.getInt("reservation_id"),
                    rs.getDouble("price"),
                    rs.getTimestamp("created_at")
                );
                serviceReservations.add(serviceReservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceReservations;
    }

    @Override
    public void updateServiceReservation(ServiceReservation serviceReservation) {
        String query = "UPDATE service_reservations SET service_id = ?, reservation_id = ?, price = ?, created_at = ? WHERE service_reservation_id = ?";
        try (Connection conn = db.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceReservation.getServiceId());
            stmt.setInt(2, serviceReservation.getReservationId());
            stmt.setDouble(3, serviceReservation.getPrice());
            stmt.setTimestamp(4, serviceReservation.getCreatedAt());
            stmt.setInt(5, serviceReservation.getServiceReservationId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteServiceReservation(int serviceReservationId) {
        String query = "DELETE FROM service_reservations WHERE service_reservation_id = ?";
        try (Connection conn = db.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceReservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
