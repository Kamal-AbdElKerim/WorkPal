import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import email.Gmail;

import DB_Conn.DB;
import interfaces.Allclass.*;
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
    private SubscriptionsDAO subscriptionsDAO;
    private ServiceReservationsDAO serviceReservationsDAO;
    private FavoriteSpaceDAO favoriteSpaceDAO;

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
        this.subscriptionsDAO = new SubscriptionsDAO(db);
        this.serviceReservationsDAO = new ServiceReservationsDAO(db);
        this.favoriteSpaceDAO = new FavoriteSpaceDAO(db);

    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to WorkPal");
            System.out.println("1. Login");
            System.out.println("2. forgot password");
            System.out.println("3. Register as a New Member");
            System.out.println("4. Exit");
            System.out.print("Please choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    UserLogin();
                    break;
                case 2:
                    ForgotPassword();
                    break;
                case 3:
                    memberRegistration("membre");
                    break;
                case 4:
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

    private void ForgotPassword() {
        try {
            String email = Validation.getValidEmail();
            Optional<User> user = userDAOImpl.getUserByEmail(email);

            if (user.isPresent()) {
                // Generate random numeric password
                String randomPassword = generateRandomNumericPassword(10); // e.g., 10-digit password
                user.get().setPassword(randomPassword); // Set the new password

                // Save the updated password in the database
                boolean updateSuccess = userDAOImpl.updateUserPassword(user.get());

                if (updateSuccess) {
                    // Send email with the new password
                    Gmail.sendEmail(email, " Forgot Password ", " Hello, your new password is:  " + randomPassword);
                    System.out
                            .println("An email has been sent to your registered email address with the new password.");
                } else {
                    System.out.println("Failed to update the password in the database.");
                }
            } else {
                System.out.println("No user found with the given email.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Generate random numeric password
    private String generateRandomNumericPassword(int length) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10); // Generates a random digit between 0 and 9
            password.append(digit);
        }
        return password.toString();
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

    private void sendEmail(String subject, String message) {
        try {
            // Fetch user details by ID
            HashMap<String, Object> userAuth = userDAOImpl.getUserById(IDAuth);
            String email = (String) userAuth.get("email"); // Cast Object to String
            String name = (String) userAuth.get("name");

            // Use the Email class to send the email
            Gmail.sendEmail(email, subject,
                    "Hello " + name + message);
        } catch (Exception e) {
            // Print the error message if something goes wrong
            System.out.println(e.getMessage());
        }
    }

    private void memberMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nMember Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Reserve Space");
            System.out.println("4. All Space Reserved");
            System.out.println("5. View Reservations");
            System.out.println("6. Cancel Reservation");
            System.out.println("7. Add services to My Space");
            System.out.println("8. favorite space");
            System.out.println("9. List favorite");
            System.out.println("10. View Events");
            System.out.println("11. Log Out");
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
                    AllSpaceReserved();
                    break;
                case 5:
                    viewReservations();
                    break;
                case 6:
                    cancelReservation();
                    break;
                case 7:
                    AddServicesToReservations();
                    break;
                case 8:
                    FavoriteSpace();
                    break;
                case 9:
                    ListFavorite();
                    break;
                case 10:
                    viewEvents();
                    break;
                case 11:
                    running = false;
                    System.out.println("Logging out...");
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
                sendEmail(" Profile updated successfully.", "Profile updated");

            } else {
                System.out.println("Failed to update profile.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void reserveSpace() {

        System.out.println("1 - Display all spaces available");
        System.out.println("2 - Search for available spaces");
        System.out.print("Enter your choice: ");
        int choice1 = Integer.parseInt(scanner.nextLine());

        try {
            if (choice1 == 1) {
                // Display all spaces with services
                spaceDAO.displayAllSpacesWithServices();
            } else if (choice1 == 2) {
                // Get search criteria from the user
                System.out.print("Enter space name (or leave empty for no search): ");
                String name = scanner.nextLine();
                name = name.isEmpty() ? null : name;

                System.out.print("Enter minimum capacity (or leave empty for no search): ");
                String capacityInput = scanner.nextLine();
                Integer capacity = capacityInput.isEmpty() ? null : Integer.parseInt(capacityInput);

                System.out.print("Enter maximum price per day (or leave empty for no search): ");
                String priceInput = scanner.nextLine();
                Double pricePerJour = priceInput.isEmpty() ? null : Double.parseDouble(priceInput);

                // Perform the search
                List<Space> spaces = spaceDAO.searchSpaces(name, capacity, pricePerJour);

                // Display results
                if (spaces.isEmpty()) {
                    System.out.println("No spaces found matching the criteria.");
                } else {
                    System.out.println("Found " + spaces.size() + " spaces:");
                    for (Space space : spaces) {
                        System.out.println("Space ID: " + space.getSpaceId() +
                                ", Name: " + space.getName() +
                                ", Description: " + space.getDescription() +
                                ", Capacity: " + space.getCapacity() +
                                ", Availability: " + space.getAvailability() +
                                ", Price per day: " + space.getPricePerJour() +
                                ", User ID: " + space.getUserId());
                    }
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                return;
            }

            // Get Space ID to reserve
            System.out.print("Enter Space ID to reserve: ");
            int spaceId = Integer.parseInt(scanner.nextLine());

            boolean running = true;
            while (running) {
                // Provide options for the user to choose the reservation method
                System.out.println("\nChoose a method to reserve:");
                System.out.println("1 - Reserve Space");
                System.out.println("2 - Reserve Space with Abonnement");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        // Reserve space without abonnement
                        try {
                            reserveSpaceWithoutAbonnement(spaceId);
                        } catch (Exception e) {
                            System.out.println("Failed to reserve space: " + e.getMessage());
                            e.printStackTrace(); // Log the exception for debugging
                        }
                        break;

                    case 2:
                        // Reserve space with abonnement
                        try {
                            reserveSpaceWithAbonnement(spaceId);
                        } catch (Exception e) {
                            System.out.println("Failed to reserve space with abonnement: " + e.getMessage());
                            e.printStackTrace(); // Log the exception for debugging
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    private void AllSpaceReserved() {

        try {
            List<Reservation> reservations = reservationsDAO.getAllReservationsByMembre(IDAuth);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
                return;
            }

            // Print table header (including Space and Service details)
            System.out.printf("%-15s %-20s %-30s %-20s %-15s %-15s %-30s\n",
                    "Reservation ID", "Space Name", "Space Description", "Start Time", "Count Jour", "Status",
                    "Services");
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");

            // Print table rows
            for (Reservation reservation : reservations) {
                // Fetch the space details using spaceId
                Space space = spaceDAO.findSpaceById(reservation.getSpaceId());

                if (space != null) {
                    // Fetch services associated with the space
                    List<Service> spaceServices = serviceDAO.getServicesBySpaceId(space.getSpaceId());

                    // Fetch services associated with the reservation
                    List<Service> reservationServices = serviceDAO
                            .getServicesByReservationId(reservation.getReservationId());

                    // Combine both space services and reservation services
                    List<Service> allServices = new ArrayList<>();
                    allServices.addAll(spaceServices);
                    allServices.addAll(reservationServices);

                    // Collect service names to display
                    String serviceNames = allServices.stream()
                            .map(Service::getName)
                            .collect(Collectors.joining(", "));

                    // Print reservation, space, and service details
                    System.out.printf("%-15d %-20s %-30s %-20s %-15s %-15s %-30s\n",
                            reservation.getReservationId(),
                            space.getName(), // Space name
                            space.getDescription(), // Space description
                            reservation.getStartTime().toString(), // Start time
                            reservation.getCountJour() != null ? reservation.getCountJour().toString() : "N/A", // Count
                                                                                                                // Jour
                            reservation.getStatus(), // Status
                            serviceNames.isEmpty() ? "No services" : serviceNames); // List of services
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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

                sendEmail(" You have reserved the space for " + days + " days.", "Reservation successful");

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
        Space space = spaceDAO.findSpaceById(spaceId);

        if (space == null) {
            System.out.println("Space not found.");
            memberMenu();
            return;
        }

        try {
            System.out.println("ID: " + space.getUserId());
            List<Abonnement> abonnements = abonnementsDAO.getAllAbonnements(space.getUserId());

            // Print table header (User ID removed)
            System.out.printf("%-15s %-15s %-20s %-15s %-15s\n",
                    "ID", "Name", "Description", "Count Jour", "Price");
            System.out.println(
                    "-------------------------------------------------------------------------------");

            // Print table rows (User ID removed)
            for (Abonnement abonnement : abonnements) {
                System.out.printf("%-15d %-15s %-20s %-15d %-15.2f\n",
                        abonnement.getAbonnementId(),
                        abonnement.getName(),
                        abonnement.getDescription(),
                        abonnement.getCountJour(), // int
                        abonnement.getPrice()); // double
            }

            System.out.println("\nChoose an abonnement to reserve:");
            int abonnementId = Integer.parseInt(scanner.nextLine());
            Abonnement selectedAbonnement = abonnementsDAO.getAbonnementById(abonnementId).orElse(null);
            System.out.println("Total price: " + selectedAbonnement.getPrice() + "DH");
            System.out.println("Do you want to buy " + selectedAbonnement.getName() + " for "
                    + selectedAbonnement.getPrice() + "DH? (true or false):");

            boolean userResponse;
            try {
                userResponse = scanner.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
                scanner.next(); // Clear the invalid input
                return;
            }
            LocalDateTime localDateTime = LocalDateTime.now();
            Timestamp startTime = Timestamp.valueOf(localDateTime);

            if (userResponse) {
                Subscription subscription = new Subscription(IDAuth, abonnementId, spaceId, "active", startTime);
                subscriptionsDAO.addSubscription(subscription);
                Reservation reservation = new Reservation(IDAuth, spaceId, startTime, selectedAbonnement.getCountJour(),
                        "active");
                reservationsDAO.addReservation(reservation);
                int reservationId = reservation.getReservationId();
                space.setAvailability(false);
                spaceDAO.updateSpace(space);
                Payments pay = new Payments(reservationId, selectedAbonnement.getPrice(), startTime, "Credit Card");
                paymentsDAO.addPayment(pay);

                System.out.println("You have reserved the space for " + selectedAbonnement.getCountJour() + " days.");
                sendEmail(" Reservation successful",
                        " You have reserved the space for " + selectedAbonnement.getCountJour() + " days.");

                memberMenu();
            } else {
                System.out.println("Reservation cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewReservations() {
        try {
            List<Reservation> reservations = reservationsDAO.getAllReservationsByMembre(IDAuth);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
                return;
            }

            // Print table header
            System.out.printf("%-15s  %-15s %-25s %-15s %-15s\n",
                    "Reservation ID", "Space ID", "Start Time", "Count Jour", "Status");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------");

            // Print table rows
            for (Reservation reservation : reservations) {
                System.out.printf("%-15d  %-15d %-25s %-15s %-15s\n",
                        reservation.getReservationId(),

                        reservation.getSpaceId(),
                        reservation.getStartTime().toString(), // Format timestamp as a string
                        reservation.getCountJour() != null ? reservation.getCountJour().toString() : "N/A", // Handle
                                                                                                            // null
                                                                                                            // values
                        reservation.getStatus()); // Format timestamp as a string
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        viewReservations();
        System.out.print("Enter the reservation ID to cancel: ");
        int reservationId = Integer.parseInt(scanner.nextLine());

        try {
            // Fetch reservation details
            Reservation reservation = reservationsDAO.getReservationById(reservationId).orElse(null);

            if (reservation == null) {
                System.out.println("Reservation not found.");
                return;
            }

            // Update reservation status to cancelled
            reservation.setStatus("cancelled");
            reservationsDAO.updateReservation(reservation);

            // Fetch space associated with the reservation
            Space space = spaceDAO.findSpaceById(reservation.getSpaceId());

            if (space != null) {
                // Update space availability to true
                space.setAvailability(true);
                spaceDAO.updateSpace(space); // Assuming you have an updateSpace method
            }

            System.out.println("Reservation has been successfully cancelled.");

            // Send confirmation email to user
            HashMap<String, Object> userAuth = userDAOImpl.getUserById(IDAuth);
            sendEmail("Reservation Cancelled", " Your reservation has been successfully cancelled.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void FavoriteSpace() {
        try {
            spaceDAO.displayAllSpacesWithServices();
            System.out.print("Enter space ID to add to favorite: ");
            int spaceId = Integer.parseInt(scanner.nextLine());

            FavoriteSpace favoriteSpace = new FavoriteSpace(IDAuth, spaceId, LocalDateTime.now());

            favoriteSpaceDAO.addFavoriteSpace(favoriteSpace);
            System.out.println("Space added to favorites successfully!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ListFavorite(){
       try {
         favoriteSpaceDAO.displayFavoritesWithSpaces(IDAuth);
       } catch (Exception e) {
        // TODO: handle exception
       }
            
    }

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

    private void AddServicesToReservations() {
        AllSpaceReserved();
        System.out.print("Enter the ID of the Reservation to which you want to add a service: ");
        int reservationID = scanner.nextInt();
        scanner.nextLine();

        boolean addingServices = true;
        double totalPrice = 0.0; // Initialize total price

        while (addingServices) {
            listAllServices();
            System.out.print("Enter the ID of the service you want to add (or type 0 to finish): ");
            int serviceId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (serviceId == 0) {
                addingServices = false;
                System.out.println("Finished adding services.");
                System.out.println("Total price for all services added: " + totalPrice + " MAD");
            } else {
                try {
                    // Add service to space
                    serviceDAO.addServiceToSpace(reservationID, serviceId);

                    // Retrieve service details and accumulate the price
                    List<Service> services = serviceDAO.getServicesBySpaceId(reservationID);
                    for (Service service : services) {
                        totalPrice += service.getPrice();
                    }
                    LocalDateTime localDateTime = LocalDateTime.now();
                    Timestamp startTime = Timestamp.valueOf(localDateTime);
                    ServiceReservation serviceReservation = new ServiceReservation(serviceId, reservationID, totalPrice,
                            startTime);
                    serviceReservationsDAO.addServiceReservation(serviceReservation);
                    List<Payments> payment = paymentsDAO.getPaymentsByReservationId(reservationID);
                    System.out.println("Service added to reservation successfully. Current total price: " + totalPrice);
                    double currentAmount = payment.get(0).getAmount();

                    paymentsDAO.updatePaymentPrice(reservationID, totalPrice + currentAmount);
                } catch (SQLException e) {
                    System.out.println("Error adding service to reservation: " + e.getMessage());
                }
            }
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

        int countJour = Validation.getValidJour();

        double price = Validation.getValidPrice();
        System.out.println(IDAuth);
        Abonnement abonnement = new Abonnement(name, description, countJour, price, IDAuth);

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

            System.out.print(
                    "Enter new countJour (or press Enter to keep '" + existingAbonnement.getCountJour() + "'): ");
            String countJourInput = scanner.nextLine().trim();
            int countJour;
            if (countJourInput.isEmpty()) {
                countJour = existingAbonnement.getCountJour();
            } else {
                countJour = Integer.parseInt(countJourInput);
            }

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
            Abonnement updatedAbonnement = new Abonnement(abonnementId, name, description, countJour, price, IDAuth);

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
