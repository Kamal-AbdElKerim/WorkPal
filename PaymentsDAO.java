import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB_Conn.DB;
import interfaces.PaymentsInterface;
import interfaces.Allclass.Payments;

    public class PaymentsDAO implements PaymentsInterface {
    private DB db;

    public PaymentsDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addPayment(Payments payment) throws SQLException {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date, payment_method) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, payment.getReservationId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setTimestamp(3, payment.getPaymentDate());
            stmt.setString(4, payment.getPaymentMethod());
            
            stmt.executeUpdate();
        }
    }
    

    @Override
    public Payments getPaymentById(int paymentId) {
        String query = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Payments(
                    rs.getInt("payment_id"),
                    rs.getInt("reservation_id"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("payment_date"),
                    rs.getString("payment_method"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payments> getPaymentsByReservationId(int reservationId) {
        List<Payments> paymentsList = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE reservation_id = ?";
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payments payment = new Payments(
                    rs.getInt("payment_id"),
                    rs.getInt("reservation_id"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("payment_date"),
                    rs.getString("payment_method"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                );
                paymentsList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentsList;
    }

    @Override
    public void updatePaymentStatus(int paymentId, String status) {
        String query = "UPDATE payments SET status = ? WHERE payment_id = ?";
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePayment(int paymentId) {
        String query = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
