
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import DB_Conn.DB;
import interfaces.ReservationInterface;
import interfaces.Allclass.Reservation;
import interfaces.Allclass.ReservationPaymentDTO;

public class ReservationsDAO implements ReservationInterface {

    private DB db;

    public ReservationsDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, space_id, start_time, count_jour, status) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = db.connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getSpaceId());
            stmt.setTimestamp(3, reservation.getStartTime());
            stmt.setInt(4, reservation.getCountJour());
            stmt.setString(5, reservation.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Retrieve the generated reservationId
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int reservationId = generatedKeys.getInt(1); // Get the auto-generated reservation_id
                        reservation.setReservationId(reservationId); // Set it in the reservation object
                        System.out.println("Reservation ID: " + reservationId);
                    }
                }
            }
        }
    }
    

    @Override
    public Optional<Reservation> getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reservation reservation = mapRowToReservation(rs);
                return Optional.of(reservation);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> getAllReservationsByMembre(int AuthID) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, AuthID);  // Set the AuthID parameter in the SQL query
            ResultSet rs = stmt.executeQuery();
            
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;
        }
    }
    

    @Override
    public void updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations SET user_id = ?, space_id = ?, start_time = ?, count_jour = ?, status = ? WHERE reservation_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getSpaceId());
            stmt.setTimestamp(3, reservation.getStartTime());
            stmt.setInt(4, reservation.getCountJour());
            stmt.setString(5, reservation.getStatus());
            stmt.setInt(6, reservation.getReservationId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
    }

    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        int reservationId = rs.getInt("reservation_id");
        int userId = rs.getInt("user_id");
        int spaceId = rs.getInt("space_id");
        Timestamp startTime = rs.getTimestamp("start_time");
        int countJour = rs.getInt("count_jour");
        String status = rs.getString("status");
        Timestamp createdAt = rs.getTimestamp("created_at");

        return new Reservation(reservationId, userId, spaceId, startTime, countJour, status, createdAt);
    }

    public List<ReservationPaymentDTO> getAllReservationsWithPaymentsByMembre(int AuthID) throws SQLException {
        String sql = "SELECT r.reservation_id, r.user_id, r.space_id, r.start_time, r.count_jour, r.status, " +
                     "p.payment_id, p.amount, p.payment_date, p.payment_method, p.status AS payment_status " +
                     "FROM reservations r " +
                     "LEFT JOIN payments p ON r.reservation_id = p.reservation_id " +
                     "WHERE r.user_id = ?";
    
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, AuthID);  // Set the AuthID parameter in the SQL query
            ResultSet rs = stmt.executeQuery();
    
            List<ReservationPaymentDTO> reservationPayments = new ArrayList<>();
            while (rs.next()) {
                // Mapping each row to a DTO object
                ReservationPaymentDTO dto = new ReservationPaymentDTO(
                    rs.getInt("reservation_id"),
                    rs.getInt("user_id"),
                    rs.getInt("space_id"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getInt("count_jour"),
                    rs.getString("status"),
                    rs.getInt("payment_id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_date") != null ? rs.getTimestamp("payment_date").toLocalDateTime() : null,
                    rs.getString("payment_method"),
                    rs.getString("payment_status")
                );
                reservationPayments.add(dto);
            }
            return reservationPayments;
        }

       
}

    

public void displayAllReservations(int userID) throws SQLException {
    String sql = "SELECT r.reservation_id, r.start_time, r.count_jour, r.status, "
               + "s.name AS space_name, u.name AS user_name, u.email "
               + "FROM reservations r "
               + "JOIN spaces s ON r.space_id = s.space_id "
               + "JOIN users u ON r.user_id = u.user_id "
               + "WHERE s.user_id = ? "
               + "ORDER BY r.start_time DESC;";
    
    try (Connection conn = db.connect(); 
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Set the userID parameter
        stmt.setInt(1, userID);

        try (ResultSet rs = stmt.executeQuery()) {
            // Print table header
            System.out.printf("%-15s %-20s %-15s %-10s %-10s %-15s\n",
                              "Reservation ID", "Start Time", "Space Name", 
                              "User Name", "Duration", "Status");
            System.out.println("----------------------------------------------------------------------");
            
            // Iterate through the result set and display each row
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                String spaceName = rs.getString("space_name");
                String userName = rs.getString("user_name");
                int countJour = rs.getInt("count_jour");
                String status = rs.getString("status");

                String startDate = startTime.toLocalDate().toString();

                // Print row data
                System.out.printf("%-15d %-20s %-15s %-10s %-10d %-15s\n",
                                  reservationId, startDate, spaceName, 
                                  userName, countJour, status);
            }
        }
    }
}

    public void cancelReservation(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'Cancelled' WHERE reservation_id = ?;";
        
        try (Connection conn = db.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
            System.out.println("Reservation canceled successfully.");
        }
    }
    public void modifyReservation(int reservationId,  int newCountJour) throws SQLException {
        String sql = "UPDATE reservations SET  count_jour = ? WHERE reservation_id = ?";
        
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newCountJour);
            stmt.setInt(2, reservationId);
            stmt.executeUpdate();
        }
    }

    public void updateExpiredReservations() throws SQLException {
        String updateReservationsSql = "UPDATE reservations r " +
                                       "SET status = 'Cancelled' " +
                                       "FROM spaces s " +
                                       "WHERE r.space_id = s.space_id " +
                                       "AND r.start_time + INTERVAL '1 day' * r.count_jour < NOW() " +
                                       "AND r.status = 'active'";
    
        String updateSpacesSql = "UPDATE spaces s " +
                                 "SET availability = TRUE " +
                                 "FROM reservations r " +
                                 "WHERE s.space_id = r.space_id " +
                                 "AND r.start_time + INTERVAL '1 day' * r.count_jour < NOW() " +
                                 "AND r.status = 'Cancelled'";
    
        String updateSubscriptionsSql = "UPDATE subscriptions sub " +
                                        "SET status = 'inactive' " +
                                        "WHERE sub.subscription_id IN ( " +
                                        "    SELECT sub.subscription_id " +
                                        "    FROM reservations r " +
                                        "    JOIN abonnements a ON sub.abonnement_id = a.abonnement_id " +
                                        "    WHERE sub.space_id = r.space_id " +
                                        "    AND r.start_time + INTERVAL '1 day' * r.count_jour < NOW() " +
                                        "    AND sub.status = 'active' " +
                                        ")";
    
        try (Connection conn = db.connect(); 
             PreparedStatement updateReservationsStmt = conn.prepareStatement(updateReservationsSql);
             PreparedStatement updateSpacesStmt = conn.prepareStatement(updateSpacesSql);
             PreparedStatement updateSubscriptionsStmt = conn.prepareStatement(updateSubscriptionsSql)) {
    
            // Update reservations
            updateReservationsStmt.executeUpdate();
    
            // Update spaces
            updateSpacesStmt.executeUpdate();
    
            // Update subscriptions
            updateSubscriptionsStmt.executeUpdate();
        }
    }
    
    
}

    
    
