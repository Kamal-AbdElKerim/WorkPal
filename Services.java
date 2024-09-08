public class Services {

    private int service_id ;
    private String name ;
    private String description ;
    private float price ;
    private String available ;

    public Services(int service_id, String name, String description, float price, String available) {
        this.service_id = service_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Services{" +
                "service_id=" + service_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", available='" + available + '\'' +
                '}';
    }
}
