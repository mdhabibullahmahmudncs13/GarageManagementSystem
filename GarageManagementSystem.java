/*
 * ============================================================================
 * GARAGE MANAGEMENT SYSTEM
 * Main controller class - manages slots, vehicles, and billing
 * ============================================================================
 */
import java.util.*;

public class GarageManagementSystem {
    private List<GarageSlot> slots;
    private Map<String, Vehicle> parkedVehicles;
    private List<Bill> billHistory;
    private int totalSpaces;

    public GarageManagementSystem(int totalSpaces) {
        this.totalSpaces = totalSpaces;
        this.slots = new ArrayList<>();
        this.parkedVehicles = new HashMap<>();
        this.billHistory = new ArrayList<>();

        for (int i = 1; i <= totalSpaces; i++) {
            String type = (i <= 10) ? "SMALL" : (i <= 20) ? "MEDIUM" : "LARGE";
            slots.add(new GarageSlot(i, type));
        }
    }

    public boolean isFull() {
        return parkedVehicles.size() >= totalSpaces;
    }

    public int getAvailableSlots() {
        return totalSpaces - parkedVehicles.size();
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    // POLYMORPHISM in action: treat all vehicles uniformly via Vehicle reference
    public boolean parkVehicle(Vehicle vehicle) {
        if (isFull()) {
            return false;
        }

        int slotIndex = -1;
        String type = vehicle instanceof Car ? "MEDIUM" :
                      vehicle instanceof Bike ? "SMALL" : "LARGE";

        for (int i = 0; i < slots.size(); i++) {
            if (!slots.get(i).isOccupied() && slots.get(i).getSlotType().equals(type)) {
                slotIndex = i;
                break;
            }
        }

        if (slotIndex == -1) {
            for (int i = 0; i < slots.size(); i++) {
                if (!slots.get(i).isOccupied()) {
                    slotIndex = i;
                    break;
                }
            }
        }

        if (slotIndex == -1) {
            return false;
        }

        slots.get(slotIndex).parkVehicle(vehicle);
        parkedVehicles.put(vehicle.getVehicleId(), vehicle);

        return true;
    }

    public Bill unParkVehicle(String vehicleId) {
        Vehicle vehicle = parkedVehicles.get(vehicleId);

        if (vehicle == null) {
            return null;
        }

        for (GarageSlot slot : slots) {
            if (slot.isOccupied() && slot.getOccupiedBy().getVehicleId().equals(vehicleId)) {
                slot.removeVehicle();
                break;
            }
        }

        Date exitTime = new Date();
        vehicle.setExitTime(exitTime);

        double fee = vehicle.calculateFee(); // POLYMORPHISM: correct fee for vehicle type
        Bill bill = new Bill(vehicle, fee, exitTime);
        billHistory.add(bill);
        parkedVehicles.remove(vehicleId);

        return bill;
    }

    public Vehicle findVehicle(String vehicleId) {
        return parkedVehicles.get(vehicleId);
    }

    // ========== DISPLAY METHODS (used by CLI menu) ==========
    public void displayAllParkedVehicles() {
        System.out.println("\n========== PARKED VEHICLES ==========");
        if (parkedVehicles.isEmpty()) {
            System.out.println("No vehicles currently parked.");
            return;
        }
        for (Vehicle v : parkedVehicles.values()) {
            v.displayDetails();
        }
        System.out.println("Total parked: " + parkedVehicles.size() + " | Available slots: " + getAvailableSlots());
    }

    public void displayAllSlots() {
        System.out.println("\n========== GARAGE SLOTS ==========");
        for (GarageSlot slot : slots) {
            slot.displaySlot();
        }
        System.out.println("Total: " + totalSpaces + " | Occupied: " + parkedVehicles.size() + " | Available: " + getAvailableSlots());
    }

    public void displayBillHistory() {
        System.out.println("\n========== BILL HISTORY ==========");
        if (billHistory.isEmpty()) {
            System.out.println("No bills issued yet.");
            return;
        }
        for (Bill bill : billHistory) {
            System.out.println(bill);
        }
        System.out.printf("Total Revenue: BDT %.2f%n", getTotalRevenue());
    }

    public List<Vehicle> searchByOwner(String ownerName) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : parkedVehicles.values()) {
            if (v.getOwnerName().equalsIgnoreCase(ownerName)) {
                results.add(v);
            }
        }
        return results;
    }

    public List<Vehicle> getAllParkedVehicles() {
        return new ArrayList<>(parkedVehicles.values());
    }

    public List<GarageSlot> getAllSlots() {
        return new ArrayList<>(slots);
    }

    public List<Bill> getBillHistory() {
        return new ArrayList<>(billHistory);
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Bill bill : billHistory) {
            total += bill.getAmount();
        }
        return total;
    }
}
