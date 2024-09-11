
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import DB_Conn.DB;
import interfaces.ReservationInterface;
import interfaces.Allclass.Reservation;

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
    public List<Reservation> getAllReservations() throws SQLException {
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = db.connect().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
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
}
