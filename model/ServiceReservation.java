package model;

import java.sql.Timestamp;

public class ServiceReservation {
    private int serviceReservationId;
    private int serviceId;
    private int reservationId;
    private double price;
    private Timestamp createdAt;

    public ServiceReservation(int serviceReservationId, int serviceId, int reservationId, double price, Timestamp createdAt) {
        this.serviceReservationId = serviceReservationId;
        this.serviceId = serviceId;
        this.reservationId = reservationId;
        this.price = price;
        this.createdAt = createdAt;
    }

    public ServiceReservation( int serviceId, int reservationId, double price, Timestamp createdAt) {
        this.serviceId = serviceId;
        this.reservationId = reservationId;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getServiceReservationId() {
        return serviceReservationId;
    }

    public void setServiceReservationId(int serviceReservationId) {
        this.serviceReservationId = serviceReservationId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ServiceReservation{" +
                "serviceReservationId=" + serviceReservationId +
                ", serviceId=" + serviceId +
                ", reservationId=" + reservationId +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
