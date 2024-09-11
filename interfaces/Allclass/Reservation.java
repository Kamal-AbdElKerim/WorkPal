package interfaces.Allclass;

import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private int userId;
    private int spaceId;
    private Timestamp startTime;
    private Integer countJour; // Using Integer instead of int to allow null values
    private String status;
    private Timestamp createdAt;

    // Default constructor
    public Reservation() {}

    // Parameterized constructor
    public Reservation(int reservationId, int userId, int spaceId, Timestamp startTime, Integer countJour, String status, Timestamp createdAt) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.spaceId = spaceId;
        this.startTime = startTime;
        this.countJour = countJour;
        this.status = status;
        this.createdAt = createdAt;
    }
    public Reservation( int userId, int spaceId, Timestamp startTime,  Integer countJour, String status) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.startTime = startTime;
        this.countJour = countJour;
        this.status = status;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Integer getCountJour() {
        return countJour;
    }

    public void setCountJour(Integer countJour) {
        this.countJour = countJour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", spaceId=" + spaceId +
                ", startTime=" + startTime +
                ", countJour=" + countJour +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
