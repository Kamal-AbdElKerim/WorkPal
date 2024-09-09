package interfaces.Allclass;
/**
 * User
 */

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone_number;
    private String address;
    private String img;
    private String Role;

    public User(String name, String role, String address, String phone_number, String password, String email) {
        this.name = name;
        this.Role = role;
        this.address = address;
        this.phone_number = phone_number;
        this.password = password;
        this.email = email;
    }

    public User(String name,String address, String phone_number , int id) {
        this.id = id ;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone_number=" + phone_number +
                ", address='" + address + '\'' +
                ", img='" + img + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}