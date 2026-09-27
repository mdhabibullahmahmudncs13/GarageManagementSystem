/*
 * GARAGE MANAGEMENT SYSTEM - OOP Lab Exam (September 28, 2026) - Md Habibullah
 * Pillars: Abstraction (abstract Vehicle, Payable), Encapsulation (private fields),
 * Inheritance (Car/Bike/Truck extend Vehicle), Polymorphism (overridden calculateFee/displayDetails).
 */

import java.util.Scanner;

public class GarageManagementApp {
    public static void main(String[] args) {
        System.out.println("==== GARAGE MANAGEMENT SYSTEM (OOP Project) ====");
        System.out.println("Select Mode:");
        System.out.println("1. Graphical User Interface (GUI)");
        System.out.println("2. Command Line Interface (CLI)");
        System.out.print("Enter choice (1 or 2): ");

        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            // Launch GUI
            System.out.println("\nLaunching GUI...");
            sc.close();
            GarageGUI gui = new GarageGUI();
            gui.setVisible(true);
        } else {
            // Run CLI
            runCLI(sc);
        }
    }

    private static void runCLI(Scanner sc) {
        GarageManagementSystem garage = new GarageManagementSystem(30);

        boolean running = true;

        while (running) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1. Park a Vehicle");
            System.out.println("2. Remove a Vehicle (Pay Bill)");
            System.out.println("3. Search Vehicle");
            System.out.println("4. View All Parked Vehicles");
            System.out.println("5. View All Slots");
            System.out.println("6. View Bill History");
            System.out.println("7. Exit");
            System.out.println("===========================");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    parkVehicleMenu(sc, garage);
                    break;
                case 2:
                    removeVehicleMenu(sc, garage);
                    break;
                case 3:
                    searchVehicleMenu(sc, garage);
                    break;
                case 4:
                    garage.displayAllParkedVehicles();
                    break;
                case 5:
                    garage.displayAllSlots();
                    break;
                case 6:
                    garage.displayBillHistory();
                    break;
                case 7:
                    System.out.println("\nThank you for using Garage Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }

        sc.close();
    }

    private static void parkVehicleMenu(Scanner sc, GarageManagementSystem garage) {
        System.out.println("\n--- Park Vehicle ---");
        System.out.println("1. Car");
        System.out.println("2. Bike");
        System.out.println("3. Truck");
        System.out.print("Select vehicle type: ");

        int type = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = sc.nextLine();
        System.out.print("Enter Owner Name: ");
        String ownerName = sc.nextLine();
        System.out.print("Enter Owner Contact: ");
        String contact = sc.nextLine();
        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();
        System.out.print("Enter Model: ");
        String model = sc.nextLine();
        System.out.print("Enter Year: ");
        int year = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Color: ");
        String color = sc.nextLine();

        Vehicle vehicle = null;

        switch (type) {
            case 1:
                System.out.print("Enter Number of Doors: ");
                int doors = sc.nextInt();
                sc.nextLine();
                System.out.print("Has AC? (true/false): ");
                boolean ac = sc.nextBoolean();
                sc.nextLine();
                vehicle = new Car(vehicleId, ownerName, contact, brand, model, year, color, doors, ac);
                break;
            case 2:
                System.out.print("Is Sports Bike? (true/false): ");
                boolean sports = sc.nextBoolean();
                sc.nextLine();
                vehicle = new Bike(vehicleId, ownerName, contact, brand, model, year, color, sports);
                break;
            case 3:
                System.out.print("Enter Capacity (tons): ");
                int capacity = sc.nextInt();
                sc.nextLine();
                vehicle = new Truck(vehicleId, ownerName, contact, brand, model, year, color, capacity);
                break;
            default:
                System.out.println("Invalid vehicle type!");
                return;
        }

        garage.parkVehicle(vehicle);
    }

    private static void removeVehicleMenu(Scanner sc, GarageManagementSystem garage) {
        System.out.print("\nEnter Vehicle ID to remove: ");
        String vehicleId = sc.nextLine();

        Bill bill = garage.unParkVehicle(vehicleId);
        if (bill != null) {
            System.out.println("\n--- Vehicle Removed ---");
            System.out.println(bill);
        } else {
            System.out.println("Vehicle not found in garage!");
        }
    }

    private static void searchVehicleMenu(Scanner sc, GarageManagementSystem garage) {
        System.out.print("\nEnter Vehicle ID to search: ");
        String vehicleId = sc.nextLine();

        Vehicle vehicle = garage.findVehicle(vehicleId);
        if (vehicle != null) {
            System.out.println("\n--- Vehicle Found ---");
            vehicle.displayDetails();
            System.out.println("Current Fee: BDT " + vehicle.calculateFee());
        } else {
            System.out.println("Vehicle not found in garage!");
        }
    }
}
