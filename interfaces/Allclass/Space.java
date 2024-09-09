package interfaces.Allclass;

public class Space {
    private int spaceId;
    private String name;
    private String description;
    private int capacity;
    private boolean availability;
    private float pricePerHour;
    private int userId;


        // Constructor (optional)

        public Space(int spaceId, String name, String description, int capacity, boolean availability, float pricePerHour, int userId) {
            this.spaceId = spaceId;
            this.name = name;
            this.description = description;
            this.capacity = capacity;
            this.availability = availability;
            this.pricePerHour = pricePerHour;
            this.userId = userId;
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


    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }





    // Additional methods can be added as needed
}
