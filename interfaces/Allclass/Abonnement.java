package interfaces.Allclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Abonnement {
    private int abonnementId;
    private String name;
    private String description;
    private int countJour ;
    private double price;
    private int userId;

    // Constructors
    public Abonnement(int abonnementId, String name, String description, int countJour , double price, int userId) {
        this.abonnementId = abonnementId;
        this.name = name;
        this.description = description;
        this.countJour = countJour;
        this.price = price;
        this.userId = userId;
    }

    public Abonnement(String name, String description, int countJour , double price, int userId) {
        this.name = name;
        this.description = description;
        this.countJour = countJour;
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

    public int getCountJour() {
        return countJour;
    }

    public void setCountJour(int countJour) {
        this.countJour = countJour;
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
                ", countJour=" + countJour +
                ", price=" + price +
                ", userId=" + userId +
                '}';
    }
}
