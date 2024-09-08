public class Abonnements {

    private int abonnement_id;
    private String name;
    private String description;
    private String start_date;
    private String end_date ;
    private float price;
    private int user_id ;

    public Abonnements(int abonnement_id, String name, String description, String start_date, String end_date, float price, int user_id) {
        this.abonnement_id = abonnement_id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.user_id = user_id;
    }

    public int getAbonnement_id() {
        return abonnement_id;
    }

    public void setAbonnement_id(int abonnement_id) {
        this.abonnement_id = abonnement_id;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "abonnements{" +
                "abonnement_id=" + abonnement_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", price=" + price +
                ", user_id=" + user_id +
                '}';
    }
}
