/*
 * ============================================================================
 * CAR CLASS
 * Pillar 3: INHERITANCE - Extends Vehicle
 * Pillar 4: POLYMORPHISM - Overrides methods for Car-specific behavior
 * ============================================================================
 */
public class Car extends Vehicle {

    // Car-specific fields (INHERITANCE: extends Vehicle's fields)
    private int numberOfDoors;
    private boolean hasAC;
    private double parkingRatePerHour = 50.0; // BDT

    public Car(String vehicleId, String ownerName, String ownerContact,
               String brand, String model, int year, String color,
               int numberOfDoors, boolean hasAC) {
        super(vehicleId, ownerName, ownerContact, brand, model, year, color);
        this.numberOfDoors = numberOfDoors;
        this.hasAC = hasAC;
    }

    public int getNumberOfDoors() { return numberOfDoors; }
    public boolean isHasAC() { return hasAC; }

    // ========== POLYMORPHISM (method overriding) ==========
    @Override
    public String getVehicleType() {
        return "Car";
    }

    // ========== POLYMORPHISM (method overriding) ==========
    @Override
    public void displayDetails() {
        super.displayDetails(); // reuse parent's display
        System.out.println("Type          : Car");
        System.out.println("Doors         : " + numberOfDoors);
        System.out.println("AC            : " + (hasAC ? "Yes" : "No"));
        System.out.println("Rate          : BDT " + parkingRatePerHour + "/hour");
    }

    // ========== POLYMORPHISM (method overriding) ==========
    @Override
    public double calculateFee() {
        long hours = getParkingHours();
        if (hours < 1) hours = 1;
        return hours * parkingRatePerHour;
    }
}
