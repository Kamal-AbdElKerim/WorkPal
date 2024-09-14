package interfaces;


import java.sql.SQLException;
import java.util.List;

import model.Payments;

public interface PaymentsInterface {
    void addPayment(Payments payment) throws SQLException;
    Payments getPaymentById(int paymentId);
    List<Payments> getPaymentsByReservationId(int reservationId);
    void updatePaymentStatus(int paymentId, String status);
    void deletePayment(int paymentId);
}