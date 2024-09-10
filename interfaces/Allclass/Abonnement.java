package interfaces.Allclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Abonnement {
    private int abonnementId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private int userId;

    // Constructors
    public Abonnement(int abonnementId, String name, String description, LocalDate startDate, LocalDate endDate, double price, int userId) {
        this.abonnementId = abonnementId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.userId = userId;
    }

    public Abonnement(String name, String description, LocalDate startDate, LocalDate endDate, double price, int userId) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.userId = userId;

    }

    // Getters and Setters
    public int getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(int abonnementId) {
        this.abonnementId = abonnementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    @Override
    public String toString() {
        return "Abonnement{" +
                "abonnementId=" + abonnementId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", userId=" + userId +
                '}';
    }
}
