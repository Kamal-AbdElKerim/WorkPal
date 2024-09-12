package interfaces.Allclass;

import java.time.LocalDateTime;

public class Space {
    private int spaceId;
    private String name;
    private String description;
    private int capacity;
    private boolean availability;
    private float pricePerJour;
    private int userId;
    private LocalDateTime createdAt;



        // Constructor (optional)

        public Space(int spaceId, String name, String description, int capacity, boolean availability, float pricePerJour, int userId) {
            this.spaceId = spaceId;
            this.name = name;
            this.description = description;
            this.capacity = capacity;
            this.availability = availability;
            this.pricePerJour = pricePerJour;
            this.userId = userId;
        }

           public Space(int spaceId, String name, String description, int capacity, boolean availability, float pricePerJour, int userId, LocalDateTime createdAt) {
        this.spaceId = spaceId;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.availability = availability;
        this.pricePerJour = pricePerJour;
        this.userId = userId;
        this.createdAt = createdAt;
    }
    
        public Space() {
            // Default constructor
        }

    // Getters and Setters

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public float getPricePerJour() {
        return pricePerJour;
    }

    public void setPricePerJour(float pricePerJour) {
        this.pricePerJour = pricePerJour;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }





    // Additional methods can be added as needed
}
