package Validation;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Validation {

    public static String getValidInput(String validName) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your " + validName + ": ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println(validName + " cannot be empty. Please try again.");
        }
    }

    public static String getValidEmail() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();
            if (isValidEmail(email)) {
                return email;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }

    public static String getValidPassword() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();
            if (password.length() >= 6) {
                return password;
            }
            System.out.println("Password must be at least 6 characters long. Please try again.");
        }
    }

    public static String getValidAddress() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your address: ");
            String address = scanner.nextLine().trim();
            if (!address.isEmpty()) {
                return address;
            }
            System.out.println("Address cannot be empty. Please try again.");
        }
    }

    public static String getValidPhoneNumber() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your phone number: ");
            String phoneNumber = scanner.nextLine().trim();
            if (isValidPhoneNumber(phoneNumber)) {
                return phoneNumber;
            }
            System.out.println("Invalid phone number format. Please try again.");
        }
    }

    public static int getValidCapacity() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter space capacity: ");
            if (scanner.hasNextInt()) {
                int capacity = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (capacity > 0) {
                    return capacity;
                }
            }
            System.out.println("Capacity must be a positive integer. Please try again.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    public static float getValidPricePerHour() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter space price per hour: ");
            if (scanner.hasNextFloat()) {
                float price = scanner.nextFloat();
                scanner.nextLine(); // Consume the newline
                if (price > 0) {
                    return price;
                }
            }
            System.out.println("Price per hour must be a positive number. Please try again.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    public static boolean getValidAvailability() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Is the space available (true/false): ");
            if (scanner.hasNextBoolean()) {
                boolean availability = scanner.nextBoolean();
                scanner.nextLine(); // Consume the newline
                return availability;
            }
            System.out.println("Please enter 'true' or 'false'.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }
}
