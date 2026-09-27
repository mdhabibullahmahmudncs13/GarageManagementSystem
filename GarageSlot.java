/*
 * ============================================================================
 * GARAGE SLOT CLASS
 * Pillar 2: ENCAPSULATION - Internal state private, controlled access
 * ============================================================================
 */
public class GarageSlot {
    private int slotNumber;
    private Vehicle occupiedBy;   // null means empty
    private String slotType;      // SMALL, MEDIUM, LARGE

    public GarageSlot(int slotNumber, String slotType) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.occupiedBy = null;
    }

    public int getSlotNumber() { return slotNumber; }
    public Vehicle getOccupiedBy() { return occupiedBy; }
    public String getSlotType() { return slotType; }

    // Encapsulated state check
    public boolean isOccupied() {
        return occupiedBy != null;
    }

    // Controlled mutation — external code cannot directly set occupiedBy
    public boolean parkVehicle(Vehicle vehicle) {
        if (isOccupied()) {
            return false;
        }
        occupiedBy = vehicle;
        return true;
    }

    // Controlled removal — returns the vehicle and clears slot
    public Vehicle removeVehicle() {
        if (!isOccupied()) {
            return null;
        }
        Vehicle v = occupiedBy;
        occupiedBy = null;
        return v;
    }

    public void displaySlot() {
        System.out.printf("[Slot %02d] %s | %s%n",
            slotNumber,
            isOccupied() ? "OCCUPIED" : "EMPTY",
            isOccupied() ? occupiedBy.getVehicleType() + " - " + occupiedBy.getOwnerName() : "Available");
    }
}
