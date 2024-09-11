import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import DB_Conn.DB;
import interfaces.Allclass.Abonnement;
import interfaces.Allclass.Payments;
import interfaces.Allclass.Reservation;
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
    private Validation validation;
    private AbonnementsDAO abonnementsDAO;
    private ReservationsDAO reservationsDAO;
    private PaymentsDAO paymentsDAO;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
        this.db = new DB();
        this.userDAOImpl = new UserDAOImpl(db);
        this.spaceDAO = new SpaceDAO(db);
        this.serviceDAO = new ServiceDAO(db);
        this.validation = new Validation();
        this.abonnementsDAO = new AbonnementsDAO(db);
        this.reservationsDAO = new ReservationsDAO(db);
        this.paymentsDAO = new PaymentsDAO(db);

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
    
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }
    
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a valid number.");
                continue;
            }
    
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
                    System.out.println("Logging out...");
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
        try {
            // Display all available spaces with associated services
            spaceDAO.displayAllSpacesWithServices();

            System.out.println("Enter Space ID to reserve:");
            int spaceId = Integer.parseInt(scanner.nextLine());

            boolean running = true;
            while (running) {
                // Provide options for the user to choose the reservation method
                System.out.println("\nChoose a method to reserve:");
                System.out.println("1. Reserve Space");
                System.out.println("2. Reserve Space with Abonnement");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        // Reserve space without abonnement
                        reserveSpaceWithoutAbonnement(spaceId);

                        break;

                    case 2:
                        reserveSpaceWithAbonnement(spaceId);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to reserve space: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    public void reserveSpaceWithoutAbonnement(int spaceId) throws Exception {
        System.out.println("How many days do you want to reserve the space?");
        int days;
        String status = "active";
    
        // Get the number of days from user input
        try {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number of days.");
                return;
            }
            days = Integer.parseInt(input);
            if (days <= 0) {
                System.out.println("Number of days must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number of days.");
            return;
        }
    
        // Fetch space details
        Space space = spaceDAO.findSpaceById(spaceId);
    
        if (space == null) {
            System.out.println("Space not found.");
            memberMenu();
            return;
        }
    
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp startTime = Timestamp.valueOf(localDateTime);
    
        if (space.getAvailability()) {
            // Create a new reservation
            double payment = space.getPricePerJour() * days;
            System.out.println("Total price: " + payment + "DH");
            System.out.println("Do you want to reserve for " + payment + "DH? (true or false):");
    
            // Handle user input for reservation confirmation
            boolean userResponse;
            try {
                userResponse = scanner.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
                scanner.next(); // Clear the invalid input
                return;
            }
    
            if (userResponse) {
                Reservation reservation = new Reservation(IDAuth, spaceId, startTime, days, status);
                reservationsDAO.addReservation(reservation);
                int reservationId = reservation.getReservationId();
                space.setAvailability(false);
                spaceDAO.updateSpace(space);
                Payments pay = new Payments(reservationId, payment, startTime, "Credit Card");
                paymentsDAO.addPayment(pay);
    
                System.out.println("You have reserved the space for " + days + " days.");
            } else {
                System.out.println("Reservation cancelled.");
            }
    
            // Return to member menu
            memberMenu();
    
        } else {
            System.out.println("Space is not available at the moment.");
        }
    }
    

    private void reserveSpaceWithAbonnement(int spaceId) {

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
            System.out.println("2. update Space");
            System.out.println("3. delete Space");
            System.out.println("4. List Space");
            System.out.println("5. Add services to Space");
            System.out.println("6. Back");
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
                    listSpaces();

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
        float pricePerJour = Validation.getValidPricePerJour();

        // Create Space object
        Space space = new Space();
        space.setName(name);
        space.setDescription(description);
        space.setCapacity(capacity);
        space.setAvailability(availability);
        space.setPricePerJour(pricePerJour);
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
                float pricePerJour = Float.parseFloat(priceInput);
                space.setPricePerJour(pricePerJour);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for price per hour. Keeping current value.");
            }
        }

        // Update space in the database
        spaceDAO.updateSpace(space);
        System.out.println("Space updated successfully.");
    }

    private void listSpaces() {
        try {
            spaceDAO.displayAllSpacesWithServices(IDAuth);
        } catch (SQLException e) {
            System.out.println("Error displaying spaces with services: " + e.getMessage());
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
        boolean running = true;
        while (running) {
            System.out.println("\nmanage Subscriptions");
            System.out.println("1. Add Subscriptions");
            System.out.println("2. update Subscriptions");
            System.out.println("3. delete Subscriptions");
            System.out.println("4. list Subscriptions");
            System.out.println("5. back");
            System.out.print("Please choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    AddAbonnements();
                    break;
                case 2:
                    UpdateAbonnements();
                    break;
                case 3:
                    DeleteAbonnements();
                    break;
                case 4:
                    ListAbonnements();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void AddAbonnements() {

        String name = Validation.getValidInput("name");

        String description = Validation.getValidInput("description");

        LocalDate startDate = Validation.getValidDate("Enter start date (YYYY-MM-DD):");

        LocalDate endDate = Validation.getValidDate("Enter end date (YYYY-MM-DD) or press Enter to skip:");

        double price = Validation.getValidPrice();
        System.out.println(IDAuth);
        Abonnement abonnement = new Abonnement(name, description, startDate, endDate, price, IDAuth);

        try {
            abonnementsDAO.addAbonnement(abonnement);
            System.out.println("Abonnement created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating abonnement: " + e.getMessage());
        }

    }

    private void UpdateAbonnements() {
        ListAbonnements();
        System.out.println("Enter abonnement ID to update:");
        int abonnementId = Integer.parseInt(scanner.nextLine());
        try {
            Abonnement existingAbonnement = abonnementsDAO.getAbonnementById(abonnementId)
                    .orElseThrow(() -> new SQLException("Abonnement not found"));

            // Gather updated information
            System.out.print("Enter new name (or press Enter to keep '" + existingAbonnement.getName() + "'): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty())
                name = existingAbonnement.getName();

            System.out.print(
                    "Enter new description (or press Enter to keep '" + existingAbonnement.getDescription() + "'): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty())
                description = existingAbonnement.getDescription();

            System.out.print("Enter new start date (YYYY-MM-DD) (or press Enter to keep '"
                    + existingAbonnement.getStartDate() + "'): ");
            String startDateInput = scanner.nextLine().trim();
            LocalDate startDate = startDateInput.isEmpty() ? existingAbonnement.getStartDate()
                    : LocalDate.parse(startDateInput);

            System.out.print("Enter new end date (YYYY-MM-DD) (or press Enter to keep '"
                    + existingAbonnement.getEndDate() + "'): ");
            String endDateInput = scanner.nextLine().trim();
            LocalDate endDate = endDateInput.isEmpty() ? existingAbonnement.getEndDate()
                    : LocalDate.parse(endDateInput);

            double price = -1;
            while (price < 0) {
                System.out.print("Enter new price (or press Enter to keep '" + existingAbonnement.getPrice() + "'): ");
                String priceInput = scanner.nextLine().trim();
                if (priceInput.isEmpty()) {
                    price = existingAbonnement.getPrice();
                } else {
                    try {
                        price = Double.parseDouble(priceInput);
                        if (price < 0) {
                            System.out.println("Price cannot be negative. Please enter a valid price.");
                            price = -1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price. Please enter a valid number.");
                    }
                }
            }

            // Create an updated Abonnement object
            Abonnement updatedAbonnement = new Abonnement(abonnementId, name, description, startDate, endDate, price,
                    IDAuth);

            // Update the abonnement in the database
            abonnementsDAO.updateAbonnement(updatedAbonnement);

            System.out.println("Abonnement updated successfully.");

        } catch (SQLException e) {
            System.out.println("Error updating abonnement: " + e.getMessage());
        }

    }

    private void ListAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementsDAO.getAllAbonnements(IDAuth);

            for (Abonnement abonnement : abonnements) {
                System.out.println(abonnement);
            }
        } catch (SQLException e) {
            System.err.println("Error listing abonnements: " + e.getMessage());
        }
    }

    private void DeleteAbonnements() {
        ListAbonnements();
        System.out.println("Enter abonnement ID to delete:");
        int abonnementId = Integer.parseInt(scanner.nextLine());

        try {
            abonnementsDAO.deleteAbonnement(abonnementId);
            System.out.println("Abonnement deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting abonnement: " + e.getMessage());
        }
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
