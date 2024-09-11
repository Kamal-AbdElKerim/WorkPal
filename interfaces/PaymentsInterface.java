package interfaces;


import interfaces.Allclass.Payments;

import java.sql.SQLException;
import java.util.List;

public interface PaymentsInterface {
    void addPayment(Payments payment) throws SQLException;
    Payments getPaymentById(int paymentId);
    List<Payments> getPaymentsByReservationId(int reservationId);
    void updatePaymentStatus(int paymentId, String status);
    void deletePayment(int paymentId);
}