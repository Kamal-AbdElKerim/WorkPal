import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

import DB_Conn.DB;
import interfaces.Allclass.Service;
import interfaces.Allclass.Space;
import interfaces.Allclass.User;
import Validation.Validation;

public class ConsoleInterface {
    private Scanner scanner;
    private UserDAOImpl userDAOImpl;
    private SpaceDAO spaceDAO;
    private ServiceDAO serviceDAO; // Added ServiceDAO
    private DB db;
    private int IDAuth;
    private  Validation validation ;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
        this.db = new DB();
        this.userDAOImpl = new UserDAOImpl(db);
        this.spaceDAO = new SpaceDAO(db);
        this.serviceDAO = new ServiceDAO(db);
        this.validation = new Validation();

    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to WorkPal");
            System.out.println("1. Login");
            System.out.println("2. Register as a New Member");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    UserLogin();
                    break;
                case 2:
                    memberRegistration("membre");
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void UserLogin() {


        String email = Validation.getValidEmail();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        int userId = userDAOImpl.login(email, password);

        if (userId > 0) {
            this.IDAuth = userId;
            HashMap<String, Object> userData = userDAOImpl.getUserById(userId);

            String role = (String) userData.get("role");

            if ("membre".equals(role)) {
                memberMenu();
            } else if ("admin".equals(role)) {
                adminMenu();
            } else {
                managerMenu();
            }
        } else {
            System.out.println("Login failed. Email or password is incorrect.");
        }
    }

    private void memberRegistration(String Role) {

        String name = Validation.getValidInput("name");


        String email = Validation.getValidEmail();
        String password = Validation.getValidPassword();
        String address = Validation.getValidAddress();
        String phoneNumber = Validation.getValidPhoneNumber();
        String role = Role;

        Optional<User> existingUser = userDAOImpl.getUserByEmail(email);
        if (existingUser.isPresent()) {
            System.out.println("This email is already registered. Please use a different email.");
        } else {
            User user = new User(name, role, address, phoneNumber, password, email);
            userDAOImpl.addUser(user);
            System.out.println("Registration successful!");
            // memberMenu();
        }
    }

    private void memberMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nMember Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Reserve Space");
            System.out.println("4. View Reservations");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Manage Subscriptions");
            System.out.println("7. View Events");
            System.out.println("8. Log Out");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateProfile(IDAuth);
                    break;
                case 3:
                    reserveSpace();
                    break;
                case 4:
                    viewReservations();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    manageSubscriptions();
                    break;
                case 7:
                    viewEvents();
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void managerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nManager Menu");
            System.out.println("1. Manage Spaces");
            System.out.println("2. View Reservations");
            System.out.println("3. Manage Members");
            System.out.println("4. Manage Subscriptions");
            System.out.println("5. View Reports");
            System.out.println("6. Manage Payments");
            System.out.println("7. Log Out");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    manageSpaces();
                    break;
                case 2:
                    viewReservations();
                    break;
                case 3:
                    manageMembers();
                    break;
                case 4:
                    manageSubscriptions();
                    break;
                case 5:
                    viewReports();
                    break;
                case 6:
                    managePayments();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void adminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nAdministrator Menu");
            System.out.println("1. Manage Users");
            System.out.println("2. Configure Application");
            System.out.println("3. Manage Access Roles");
            System.out.println("4. Log Out");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    configureApplication();
                    break;
                case 3:
                    manageAccessRoles();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewProfile() {
        System.out.println("\nView Profile");

        // Retrieve user data using the DAO
        HashMap<String, Object> userData = userDAOImpl.getUserById(IDAuth);
        if (!userData.get("email").equals("admin@gmail.com")) {
            if (!userData.isEmpty()) {
                System.out.println("Name: " + userData.get("name"));
                System.out.println("Email: " + userData.get("email"));
                System.out.println("Phone Number: " + userData.get("phone_number"));
                System.out.println("Address: " + userData.get("address"));
            } else {
                System.out.println("User not found.");
            }
        }
    }

    private void updateProfile(int IDAuth) {
        // Display current profile
        viewProfile();

        // Retrieve current user data
        HashMap<String, Object> userData = userDAOImpl.getUserById(IDAuth);

        // Ensure userData is not empty before proceeding
        if (!userData.isEmpty()) {
            String currentName = (String) userData.get("name");
            String currentPhoneNumber = (String) userData.get("phone_number");
            String currentAddress = (String) userData.get("address");

            // Prompt user for new values
            System.out.print("Enter new name (leave blank to keep current: " + currentName + "): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                currentName = newName;
            }

            System.out.print("Enter new phone number (leave blank to keep current: " + currentPhoneNumber + "): ");
            String newPhoneNumber = scanner.nextLine();
            if (!newPhoneNumber.isEmpty()) {
                currentPhoneNumber = newPhoneNumber;
            }

            System.out.print("Enter new address (leave blank to keep current: " + currentAddress + "): ");
            String newAddress = scanner.nextLine();
            if (!newAddress.isEmpty()) {
                currentAddress = newAddress;
            }

            // Create a new User object with updated details
            User updateuser = new User(currentName, currentAddress, currentPhoneNumber, IDAuth);

            // Update user profile in the database
            boolean updateSuccessful = userDAOImpl.updateUser(updateuser);

            if (updateSuccessful) {
                System.out.println("Profile updated successfully.");
            } else {
                System.out.println("Failed to update profile.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void reserveSpace() {
        // Implementation for reserving a space
    }

    private void viewReservations() {
        // Implementation for viewing member reservations
    }

    private void cancelReservation() {
        // Implementation for canceling a reservation
    }

    // private void manageSubscriptions() {
    // // Implementation for managing subscriptions
    // }

    private void viewEvents() {
        // Implementation for viewing events
    }

    private void manageSpaces() {
        boolean running = true;
        while (running) {
            System.out.println("\nManager Spaces");
            System.out.println("1. Add Space");
            System.out.println("2. ubdate Space");
            System.out.println("3. delete Space");
            System.out.println("4. List Space");
            System.out.println("5. Add services to Space");
            System.out.println("6. Log Out");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    AddSpace();
                    break;
                case 2:
                    updateSpace();
                    break;
                case 3:
                    deleteSpace();
                    break;
                case 4:

                    try {
                        spaceDAO.displayAllSpacesWithServices(IDAuth);
                    } catch (SQLException e) {
                        System.out.println("Error displaying spaces with services: " + e.getMessage());
                    }
                    break;
                case 5:
                    AddServicesToSpace();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void AddSpace() {

        String name = Validation.getValidInput("space name");
        String description = Validation.getValidInput("space description");


        int capacity = Validation.getValidCapacity();
        boolean availability = Validation.getValidAvailability();
        float pricePerHour = Validation.getValidPricePerHour();

        // Create Space object
        Space space = new Space();
        space.setName(name);
        space.setDescription(description);
        space.setCapacity(capacity);
        space.setAvailability(availability);
        space.setPricePerHour(pricePerHour);
        space.setUserId(IDAuth);

        // Add space to database
        spaceDAO.addSpace(space);
        System.out.println("Space added successfully!");
    }

    private void updateSpace() {
        listSpaces();
        System.out.print("Enter the ID of the space you want to update: ");
        int spaceId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Retrieve the space to update
        Space space = spaceDAO.findSpaceById(spaceId);
        if (space == null) {
            System.out.println("Space not found.");
            return;
        }

        // Get new details from the user
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            space.setName(name);
        }

        System.out.print("Enter new description (leave blank to keep current): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            space.setDescription(description);
        }

        System.out.print("Enter new capacity (enter 0 to keep current): ");
        String capacityInput = scanner.nextLine();
        if (!capacityInput.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityInput);
                if (capacity > 0) {
                    space.setCapacity(capacity);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for capacity. Keeping current value.");
            }
        }

        System.out.print("Is the space available (true/false): ");
        String availabilityInput = scanner.nextLine();
        try {
            boolean availability = Boolean.parseBoolean(availabilityInput);
            space.setAvailability(availability);
        } catch (Exception e) {
            System.out.println("Invalid input for availability. Keeping current value.");
        }

        System.out.print("Enter new price per hour: ");
        String priceInput = scanner.nextLine();
        if (!priceInput.isEmpty()) {
            try {
                float pricePerHour = Float.parseFloat(priceInput);
                space.setPricePerHour(pricePerHour);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for price per hour. Keeping current value.");
            }
        }

        // Update space in the database
        spaceDAO.updateSpace(space);
        System.out.println("Space updated successfully.");
    }

    private void listSpaces() {
        // Retrieve all spaces from the database
        List<Space> spaces = spaceDAO.findAllSpacesByGestionnaire(IDAuth);

        // Check if the list is empty
        if (spaces.isEmpty()) {
            System.out.println("No spaces found for the given gestionnaire.");
            return;
        }

        // Print headers for readability
        System.out.printf("%-5s %-20s %-20s %-10s %-10s %-15s%n",
                "ID", "Name", "Description", "Capacity", "Available", "Price/Hour");

        // Print each space's details
        for (Space space : spaces) {
            System.out.printf("%-5d %-20s %-20s %-10d %-10b %-15.2f%n",
                    space.getSpaceId(),
                    space.getName(),
                    space.getDescription(),
                    space.getCapacity(),
                    space.isAvailability(),
                    space.getPricePerHour()); // Ensure conversion to float
        }
    }

    private void deleteSpace() {
        listSpaces();
        System.out.print("Enter the ID of the space you want to delete: ");
        int spaceId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Confirm deletion
        System.out.print("Are you sure you want to delete this space? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }

        // Delete the space
        spaceDAO.deleteSpace(spaceId);
        System.out.println("Space deleted successfully.");
    }

    private void listAllServices() {
        Map<Integer, Service> services = serviceDAO.findAllServices();
        System.out.printf("%-10s %-20s %-30s %-10s%n", "ID", "Name", "Description", "Price");
        for (Service service : services.values()) {
            System.out.printf("%-10d %-20s %-30s %-10.2f%n",
                    service.getServiceId(),
                    service.getName(),
                    service.getDescription(),
                    service.getPrice());
        }
    }

    private void AddServicesToSpace() {
        listSpaces();
        System.out.print("Enter the ID of the space to which you want to add a service: ");
        int spaceId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean addingServices = true;

        while (addingServices) {
            listAllServices();
            System.out.print("Enter the ID of the service you want to add (or type 0 to finish): ");
            int serviceId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (serviceId == 0) {
                addingServices = false;
                System.out.println("Service added to space successfully.");
            } else {
                try {
                    // Add service to space
                    serviceDAO.addServiceToSpace(spaceId, serviceId);
                    System.out.println("Service added to space successfully.");
                } catch (SQLException e) {
                    System.out.println("Error adding service to space: " + e.getMessage());
                }
            }
        }
    }


    private void manageMembers() {
        // Implementation for managing members (add, update, delete)
    }

    private void manageSubscriptions() {
        // Implementation for managing subscriptions (create, update, delete)
    }

    private void viewReports() {
        // Implementation for viewing reports and statistics
    }

    private void managePayments() {
        // Implementation for managing payments and invoices
    }

    private void configureApplication() {
        // Implementation for configuring application settings
    }

    private void manageAccessRoles() {
        // Implementation for managing user roles and permissions
    }

    private void manageUsers() {
        boolean running = true;
        while (running) {
            System.out.println("\nManage Users");
            System.out.println("1. Manage members");
            System.out.println("2. Manage managers");
            System.out.println("3. Reset user passwords");
            System.out.println("4. Back");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    manageMembre(); // Method to manage members
                    break;
                case 2:
                    manageGestionnaire(); // Method to manage managers
                    break;
                case 3:
                    resetUserPasswords(); // Method to reset passwords
                    break;
                case 4:
                    running = false; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void allUsersByRole(String role) {
        HashMap<Integer, User> users = userDAOImpl.getAllUsersByRole(IDAuth, role);

        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (Map.Entry<Integer, User> entry : users.entrySet()) {
                Integer userId = entry.getKey();
                User user = entry.getValue();

                System.out.println("\nUser ID: " + userId);
                System.out.println("Name: " + user.getName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Password: " + user.getPassword());
                System.out.println("Phone Number: " + user.getPhone_number());
                System.out.println("Address: " + user.getAddress());
                System.out.println("Role: " + user.getRole());
            }
        }
    }

    // Method to manage members
    private void manageMembre() {

        boolean running = true;
        while (running) {
            System.out.println("\nManage members");
            System.out.println("1. List members");
            System.out.println("2. Add members");
            System.out.println("3. Ubdate members");
            System.out.println("4. Delete members");
            System.out.println("5. Back");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    allUsersByRole("membre");
                    break;
                case 2:
                    memberRegistration("membre");
                    break;
                case 3:
                    updateuser("membre"); // Method to reset passwords
                    break;
                case 4:
                    deleteuser("membre");
                    break;
                case 5:
                    running = false; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void updateuser(String Role) {
        allUsersByRole(Role); // This method should display all users
        System.out.print("Enter the user ID of the " + Role + " you want to update: ");
        int userId = Integer.parseInt(scanner.nextLine());
        updateProfile(userId);

    }

    private void deleteuser(String Role) {
        allUsersByRole(Role); // This method should display all users
        System.out.print("Enter the user ID of the " + Role + " you want to delete: ");
        int userId = Integer.parseInt(scanner.nextLine());

        boolean deleteSuccessful = userDAOImpl.deleteUser(userId);
        if (deleteSuccessful) {
            System.out.println("Member deleted successfully.");
        } else {
            System.out.println("Failed to delete member.");
        }
    }

    private void manageGestionnaire() {
        boolean running = true;
        while (running) {
            System.out.println("\nManage Gestionnaire");
            System.out.println("1. List Gestionnaire");
            System.out.println("2. Add Gestionnaire");
            System.out.println("3. Ubdate Gestionnaire");
            System.out.println("4. Delete Gestionnaire");
            System.out.println("5. Back");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    allUsersByRole("gestionnaire");
                    break;
                case 2:
                    memberRegistration("gestionnaire");
                    break;
                case 3:
                    updateuser("gestionnaire"); // Method to reset passwords
                    break;
                case 4:
                    deleteuser("gestionnaire");
                    break;
                case 5:
                    running = false; // Exit the loop
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void resetUserPasswords() {
        System.out.println("List gestionnaire");
        allUsersByRole("gestionnaire");
        System.out.println("List membre");
        allUsersByRole("membre");

        System.out.print("Enter the user ID of the user you want to reset the password for: ");
        int userId = Integer.parseInt(scanner.nextLine());

        // Prompt for the new password
        System.out.print("Enter the new password: ");
        String newPassword = Validation.getValidPassword();

        // Reset the password using the DAO method
        userDAOImpl.resetPassword(userId, newPassword);

        System.out.println("Password has been reset successfully.");
    }

}
