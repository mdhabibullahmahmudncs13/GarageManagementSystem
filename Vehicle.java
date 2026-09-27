/*
 * ============================================================================
 * VEHICLE (ABSTRACT CLASS)
 * Pillar 1: ABSTRACTION - Cannot be instantiated, defines template
 * Pillar 2: ENCAPSULATION - All fields private, controlled access
 * ============================================================================
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Vehicle implements VehicleInfo, Payable {

    // ========== ENCAPSULATION ==========
    // All fields are PRIVATE — cannot be accessed directly from outside.
    // Data integrity is protected; changes only go through controlled methods.
    
    private String vehicleId;
    private String ownerName;
    private String ownerContact;
    private String brand;
    private String model;
    private int year;
    private String color;
    private Date entryTime;
    private Date exitTime;

    // Constructor
    public Vehicle(String vehicleId, String ownerName, String ownerContact,
                   String brand, String model, int year, String color) {
        this.vehicleId = vehicleId;
        this.ownerName = ownerName;
        this.ownerContact = ownerContact;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.entryTime = new Date();
        this.exitTime = null;
    }

    // ========== ENCAPSULATION (GETTERS & SETTERS) ==========
    // Public methods provide CONTROLLED access to private fields.
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    
    public String getOwnerContact() { return ownerContact; }
    public void setOwnerContact(String ownerContact) { this.ownerContact = ownerContact; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Date getEntryTime() { return entryTime; }
    public Date getExitTime() { return exitTime; }
    
    // Controlled mutation — only garage system should set exit time
    public void setExitTime(Date exitTime) { this.exitTime = exitTime; }

    // ========== ABSTRACTION ==========
    // Abstract method — subclasses MUST provide their own implementation.
    @Override
    public abstract String getVehicleType();

    // ========== INHERITANCE (shared behavior) ==========
    // Concrete method that ALL subclasses inherit and can use directly.
    
    public String getParkingDuration() {
        if (exitTime == null) {
            return "Still parked";
        }
        long diff = exitTime.getTime() - entryTime.getTime();
        long hours = diff / (1000 * 60 * 60);
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        return String.format("%d hours %d minutes", hours, minutes);
    }

    public long getParkingHours() {
        if (exitTime == null) {
            return (System.currentTimeMillis() - entryTime.getTime()) / (1000 * 60 * 60);
        }
        return (exitTime.getTime() - entryTime.getTime()) / (1000 * 60 * 60);
    }

    /**
     * Default display method — can be overridden by subclasses (POLYMORPHISM).
     */
    public void displayDetails() {
        System.out.println("------------------------------------------");
        System.out.println("Vehicle ID    : " + vehicleId);
        System.out.println("Owner         : " + ownerName);
        System.out.println("Contact       : " + ownerContact);
        System.out.println("Brand         : " + brand);
        System.out.println("Model         : " + model);
        System.out.println("Year          : " + year);
        System.out.println("Color         : " + color);
        System.out.println("Entry Time    : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entryTime));
        if (exitTime != null) {
            System.out.println("Exit Time     : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(exitTime));
            System.out.println("Duration      : " + getParkingDuration());
        } else {
            System.out.println("Status        : Currently Parked");
        }
    }
}
