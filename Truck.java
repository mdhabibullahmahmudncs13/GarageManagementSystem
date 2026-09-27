/*
 * ============================================================================
 * TRUCK CLASS
 * Pillar 3: INHERITANCE - Extends Vehicle
 * Pillar 4: POLYMORPHISM - Overrides methods for Truck-specific behavior
 * ============================================================================
 */
public class Truck extends Vehicle {
    private int capacityInTons;
    private double parkingRatePerHour = 150.0; // BDT

    public Truck(String vehicleId, String ownerName, String ownerContact,
                 String brand, String model, int year, String color,
                 int capacityInTons) {
        super(vehicleId, ownerName, ownerContact, brand, model, year, color);
        this.capacityInTons = capacityInTons;
    }

    public int getCapacityInTons() { return capacityInTons; }
    public void setCapacityInTons(int capacityInTons) { this.capacityInTons = capacityInTons; }

    // POLYMORPHISM: Returns "Truck"
    @Override
    public String getVehicleType() {
        return "Truck";
    }

    // POLYMORPHISM: Truck-specific display
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Type          : Truck");
        System.out.println("Capacity      : " + capacityInTons + " tons");
        System.out.println("Rate          : BDT " + parkingRatePerHour + "/hour");
    }

    // POLYMORPHISM: Truck calculates fee at 150 BDT/hour
    @Override
    public double calculateFee() {
        long hours = getParkingHours();
        if (hours < 1) hours = 1;
        return hours * parkingRatePerHour;
    }

    @Override
    public String getPaymentMethod() {
        return "Cash / Bank Transfer";
    }
}
