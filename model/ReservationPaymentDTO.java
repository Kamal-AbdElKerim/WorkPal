package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationPaymentDTO {
    private int reservationId;
    private int userId;
    private int spaceId;
    private LocalDateTime startTime;
    private int countJour;
    private String reservationStatus;
    private int paymentId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String paymentStatus;

    public ReservationPaymentDTO(int reservationId, int userId, int spaceId, LocalDateTime startTime, int countJour, 
                                 String reservationStatus, int paymentId, BigDecimal amount, 
                                 LocalDateTime paymentDate, String paymentMethod, String paymentStatus) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.spaceId = spaceId;
        this.startTime = startTime;
        this.countJour = countJour;
        this.reservationStatus = reservationStatus;
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // Getters
    public int getReservationId() {
        return reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getCountJour() {
        return countJour;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
