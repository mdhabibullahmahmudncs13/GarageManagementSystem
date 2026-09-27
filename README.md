# Garage Management System
## Object-Oriented Programming (OOP) Project in Java

**Student:** Md Habibullah  
**Subject:** Object Oriented Programming (OOP) in Java  
**Lab Exam:** September 28, 2026  

---

## The Four Pillars of OOP — Demonstrated

This project explicitly implements all four pillars of Object-Oriented Programming:

### 1. ABSTRACTION
**What it is:** Hiding complex implementation details and showing only essential features.

**How it's implemented:**
- `abstract class Vehicle` — cannot be instantiated directly; defines a template
- `interface Payable` — defines contract for payment calculation without implementation
- `interface VehicleInfo` — defines contract for displaying vehicle info
- Subclasses MUST implement abstract methods (`getVehicleType()`)

**Code Example:**
```java
abstract class Vehicle implements VehicleInfo, Payable {
    // Users don't need to know HOW parking fee is calculated
    // They just call calculateFee() — details are hidden in subclasses
    abstract String getVehicleType();
}
```

---

### 2. ENCAPSULATION
**What it is:** Bundling data (fields) and methods that operate on that data, restricting direct access to some components.

**How it's implemented:**
- All fields in `Vehicle` are `private`
- Access only through public getters (data is set once in the constructor)
- `GarageSlot` encapsulates slot state — external code cannot directly modify `occupiedBy`
- `Bill` class encapsulates billing data

**Code Example:**
```java
class Vehicle {
    private String vehicleId;      // Hidden from outside
    private Date entryTime;        // Hidden from outside
    private Date exitTime;         // Hidden from outside
    
    // Controlled access
    public String getVehicleId() { return vehicleId; }
    public Date getEntryTime() { return entryTime; }
    public void setExitTime(Date exitTime) { this.exitTime = exitTime; }
}
```

---

### 3. INHERITANCE
**What it is:** Mechanism where a new class derives properties and behaviors from an existing class.

**How it's implemented:**
- `Vehicle` (parent/superclass) → `Car`, `Bike`, `Truck` (child/subclasses)
- Subclasses inherit: `vehicleId`, `ownerName`, `entryTime`, `getParkingDuration()`, etc.
- Subclasses extend: add their own properties (`numberOfDoors`, `isSportsBike`, `capacityInTons`)

**Code Example:**
```java
class Car extends Vehicle {
    private int numberOfDoors;  // Car-specific field
    private boolean hasAC;      // Car-specific field
    
    // Inherits from Vehicle: vehicleId, ownerName, entryTime, etc.
    // Inherits methods: getParkingDuration(), displayDetails(), etc.
}
```

---

### 4. POLYMORPHISM
**What it is:** Ability of an object to take many forms. Same method call behaves differently based on the actual object type.

**How it's implemented:**
- **Method Overriding (Runtime Polymorphism):**
  - `calculateFee()` — each vehicle type calculates differently
  - `displayDetails()` — each vehicle type displays its specific info
  - `getVehicleType()` — returns different strings per type
  
- **Interface Polymorphism:**
  - `Payable` interface allows treating all vehicles uniformly for billing

**Code Example:**
```java
// Same method call, different behavior:
Vehicle v1 = new Car(...);
Vehicle v2 = new Bike(...);
Vehicle v3 = new Truck(...);

v1.calculateFee();  // Returns hours * 50.0 (Car rate)
v2.calculateFee();  // Returns hours * 20.0 (Bike rate)
v3.calculateFee();  // Returns hours * 150.0 (Truck rate)
```

---

## Class Hierarchy Diagram

```
┌─────────────────────────────────────────────────────┐
│                    Interfaces                        │
│  ┌─────────────┐    ┌────────────────┐             │
│  │  Payable    │    │  VehicleInfo   │             │
│  │ calculateFee│    │ displayDetails │             │
│  └─────────────┘    │getVehicleType  │             │
│  └─────────────┘    └────────────────┘             │
└─────────────────────────────────────────────────────┘
                         ▲
                         │ implements
                         │
              ┌─────────────────────┐
              │   abstract Vehicle  │  ← ABSTRACTION
              │─────────────────────│     (cannot instantiate)
              │ - vehicleId        │     (forces subclasses)
              │ - ownerName        │
              │ - entryTime        │
              │ - exitTime         │
              │                     │
              │ + getParkingDuration│    ENCAPSULATION
              │ + displayDetails   │     (private fields +
              │                     │      controlled access)
              └──────────┬──────────┘
                         │ extends
           ┌─────────────┼─────────────┐
           │             │             │
     ┌─────────┐  ┌─────────┐  ┌──────────┐
     │  Car    │  │  Bike   │  │  Truck   │  ← INHERITANCE
     │         │  │         │  │          │     (subclasses)
     │-doors   │  │-sports  │  │-capacity │
     │-hasAC   │  │         │  │          │
     └────┬────┘  └────┬────┘  └────┬─────┘
          │             │             │
          └─────────────┴─────────────┘
                    POLYMORPHISM
              (same method, different behavior
               via overriding calculateFee(),
               displayDetails(), etc.)
```

---

## Project Features

1. **Vehicle Parking** — Park Car, Bike, or Truck with owner details
2. **Vehicle Removal** — Exit vehicle and generate bill
3. **Slot Management** — 30 slots (SMALL/MEDIUM/LARGE)
4. **Search** — Find vehicle by ID
5. **Bill History** — Track all payments and total revenue

---

## Parking Rates (BDT/hour)

| Vehicle Type | Rate |
|--------------|------|
| Bike | 20 |
| Car | 50 |
| Truck | 150 |

---

## Compilation & Execution

```bash
# Compile
javac GarageManagementApp.java

# Run
java GarageManagementApp
```

---

## Key OOP Takeaways for Exam

1. **Abstraction** reduces complexity — users interact with simple interfaces
2. **Encapsulation** protects data integrity — fields can't be modified directly
3. **Inheritance** promotes code reuse — common vehicle logic in one place
4. **Polymorphism** enables flexibility — one interface, many implementations
