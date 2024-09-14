package interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Reservation;

public interface ReservationInterface {
    
    // Add a new Reservation
    void addReservation(Reservation reservation) throws SQLException;

    Optional<Reservation> getReservationById(int reservationId) throws SQLException;

    List<Reservation> getAllReservationsByMembre(int AuthID) throws SQLException;

    void updateReservation(Reservation reservation) throws SQLException;

    void deleteReservation(int reservationId) throws SQLException;
}
