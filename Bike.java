/*
 * ============================================================================
 * BIKE CLASS
 * Pillar 3: INHERITANCE - Extends Vehicle
 * Pillar 4: POLYMORPHISM - Overrides methods for Bike-specific behavior
 * ============================================================================
 */
public class Bike extends Vehicle {
    private boolean isSportsBike;
    private double parkingRatePerHour = 20.0; // BDT

    public Bike(String vehicleId, String ownerName, String ownerContact,
                String brand, String model, int year, String color,
                boolean isSportsBike) {
        super(vehicleId, ownerName, ownerContact, brand, model, year, color);
        this.isSportsBike = isSportsBike;
    }

    public boolean isSportsBike() { return isSportsBike; }

    // POLYMORPHISM: Returns "Bike"
    @Override
    public String getVehicleType() {
        return "Bike";
    }

    // POLYMORPHISM: Bike-specific display
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Type          : Bike");
        System.out.println("Sports Bike   : " + (isSportsBike ? "Yes" : "No"));
        System.out.println("Rate          : BDT " + parkingRatePerHour + "/hour");
    }

    // POLYMORPHISM: Bike calculates fee at 20 BDT/hour
    @Override
    public double calculateFee() {
        long hours = getParkingHours();
        if (hours < 1) hours = 1;
        return hours * parkingRatePerHour;
    }
}
