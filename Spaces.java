public class Spaces {

    private int space_id;
    private String name;
    private String description;
    private int capacity;
    private String features;
    private String availability ;
    private  int price_per_hour ;
    private int user_id ;

    public Spaces(int space_id, String name, String description, int capacity, String features, String availability, int price_per_hour, int user_id) {
        this.space_id = space_id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.features = features;
        this.availability = availability;
        this.price_per_hour = price_per_hour;
        this.user_id = user_id;
    }

    public int getSpace_id() {
        return space_id;
    }

    public void setSpace_id(int space_id) {
        this.space_id = space_id;
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(int price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "spaces{" +
                "space_id=" + space_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", features='" + features + '\'' +
                ", availability='" + availability + '\'' +
                ", price_per_hour=" + price_per_hour +
                ", user_id=" + user_id +
                '}';
    }
}
