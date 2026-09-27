# Garage Management System — Complete Explanation

**Student:** Md Habibullah · **Subject:** OOP in Java · **Lab Exam:** September 28, 2026

This document explains every file in the project, how the pieces work together at
runtime, and exactly where each of the four OOP pillars is used. It is the deep
companion to `README.md` (quick pillar summary) and `garage-callmap.html`
(visual call-flow diagram).

---

## 1. What the project does (30-second version)

A parking garage has **30 slots** of three sizes (SMALL 1–10, MEDIUM 11–20, LARGE 21–30).
People park **Cars** (50 BDT/hour), **Bikes** (20 BDT/hour) and **Trucks**
(150 BDT/hour). The system lets you:

1. **Park** a vehicle — it is assigned to a free slot of the right size
2. **Remove** it — the garage computes the fee from parking duration and issues a **Bill**
3. **Search** for a parked vehicle by its ID
4. **View** parked vehicles, all slots, and the bill history with total revenue

You can use it through a **menu-driven CLI** (`GarageManagementApp`) or a **Swing GUI**
(`GarageGUI`). Both drive the exact same engine: `GarageManagementSystem`.

```
        GarageManagementApp ──(launches)──> GarageGUI
                 │                            │
                 └────────────┬───────────────┘
                              ▼
                GarageManagementSystem  (controller: slots + vehicles + bills)
                     │           │            │
                     ▼           ▼            ▼
                GarageSlot    Vehicle      Bill
                              (abstract)
                          ┌─────┼─────┐
                          ▼     ▼     ▼
                         Car   Bike  Truck
```

---

## 2. The files at a glance

| File | Kind | Role in one line |
|---|---|---|
| `Payable.java` | interface | The payment contract: any payable thing must know `calculateFee()` |
| `VehicleInfo.java` | interface | The display contract: any vehicle info must expose `displayDetails()` and `getVehicleType()` |
| `Vehicle.java` | abstract class | The shared vehicle template: common data + common behavior, cannot be instantiated |
| `Car.java` | concrete class | A car: 2 extra fields (doors, AC), 50 BDT/hour |
| `Bike.java` | concrete class | A bike: 1 extra field (sports bike?), 20 BDT/hour |
| `Truck.java` | concrete class | A truck: 1 extra field (capacity in tons), 150 BDT/hour |
| `GarageSlot.java` | state class | One parking slot: knows its number, size type, and which vehicle occupies it |
| `Bill.java` | record class | One receipt: bill number, the vehicle, the amount, the issue time |
| `GarageManagementSystem.java` | controller | The engine: owns all slots, the parked-vehicle map, and bill history |
| `GarageManagementApp.java` | CLI entry | Text menu: reads user input, calls the engine, prints results |
| `GarageGUI.java` | GUI entry | Swing window: same operations as the CLI, plus live tables and stats |

There is **one controller** (`GarageManagementSystem`). Both front doors — CLI and GUI —
are thin shells over it. That is why the GUI and CLI always agree on the garage state.

---

## 3. The four OOP pillars — where they actually live in this code

### 3.1 Abstraction — *hide the how, expose the what*

Three constructs:

**`Vehicle` is `abstract`** — you can never write `new Vehicle(...)`:

```java
public abstract class Vehicle implements VehicleInfo, Payable {
    public abstract String getVehicleType();   // subclasses MUST decide this
    ...
}
```

The rest of the system only ever holds a `Vehicle` reference. When the bill is
printed, nobody asks "is this a Car or a Truck?" — they just call `calculateFee()`
and the *right* calculation happens. That is abstraction: the caller knows **what**,
not **how**.

**Two interfaces define the contracts:**

```java
// Payable.java — "I can be billed"
public interface Payable {
    double calculateFee();
}

// VehicleInfo.java — "I can describe myself"
public interface VehicleInfo {
    void displayDetails();
    String getVehicleType();
}
```

`Vehicle` signs both contracts, so every concrete vehicle (Car, Bike, Truck) is
guaranteed to have `calculateFee()`, `displayDetails()` and `getVehicleType()`.

**The controller hides slot mechanics.** `GarageManagementApp` calls
`garage.parkVehicle(vehicle)` and gets `true`/`false`. It never sees the
size-matching, the fallback search, or the slot bookkeeping inside.

### 3.2 Encapsulation — *private data, controlled access*

Every field in every class is `private`:

```java
// Vehicle.java
private String vehicleId;
private Date entryTime;
private Date exitTime;
```

Access goes through public **getters only** — the data is set once in the
constructor and never rewritten from outside (except `setExitTime(Date)`, which
only the garage's `unParkVehicle()` calls, because only the garage knows when a
car leaves):

```java
// GarageSlot.java — the clearest example
public boolean parkVehicle(Vehicle vehicle) {
    if (isOccupied()) return false;   // rule enforced INSIDE the class
    occupiedBy = vehicle;
    return true;
}
```

Outside code **cannot** write `slot.occupiedBy = someVehicle;` — it doesn't even
compile. The invariant "a slot can hold at most one vehicle" is protected by the
class itself, not by the discipline of its callers. `GarageSlot`, `Bill`, and the
lists inside `GarageManagementSystem` all encapsulate the same way.

### 3.3 Inheritance — *write the common part once*

All vehicle-specific data and behavior that the three types share lives **once**
in `Vehicle`: id, owner, contact, brand, model, year, color, entry/exit times,
parking-duration math, and the generic `displayDetails()`.

```java
public class Car extends Vehicle {          // "is-a Vehicle"
    private int numberOfDoors;              // adds only what is Car-specific
    private boolean hasAC;
    ...
}
```

`Car`, `Bike`, `Truck` each add just 1–2 fields and a constructor calling
`super(...)`. The three of them inherit ~60 lines of shared logic they never
had to write. Change "how duration is computed" in `Vehicle` and all three
vehicle types get the fix.

### 3.4 Polymorphism — *one call, three behaviors*

Three overridden methods per subclass:

| Method | Car | Bike | Truck |
|---|---|---|---|
| `getVehicleType()` | `"Car"` | `"Bike"` | `"Truck"` |
| `calculateFee()` | hours × **50** | hours × **20** | hours × **150** |
| `displayDetails()` | + doors, AC | + sports bike | + capacity |

The key moment is billing. The controller's loop and the bill code treat every
vehicle identically:

```java
// GarageManagementSystem.unParkVehicle()
double fee = vehicle.calculateFee();   // variable type: Vehicle
Bill bill = new Bill(vehicle, fee, exitTime);
```

`vehicle` is declared as `Vehicle`, but at runtime the JVM dispatches to
`Car.calculateFee()`, `Bike.calculateFee()` or `Truck.calculateFee()` depending
on the real object. Same source line, different behavior — that is **runtime
polymorphism (dynamic dispatch)**.

Two more flavors are in the project:

- **Overloading-free reuse:** `super.displayDetails()` — each subclass's override
  first runs the parent's version, then appends its own lines. Inheritance and
  overriding cooperate.
- **Interface polymorphism:** anything `Payable` can be billed without knowing
  it is a vehicle at all.

---

## 4. File-by-file walkthrough

### 4.1 `Payable.java` — the billing contract

```java
public interface Payable {
    double calculateFee();
}
```

One method. It exists so the garage can demand "give me your fee" from any object
that implements it, without caring about anything else. `Vehicle implements
Payable`, so all vehicles are billable.

### 4.2 `VehicleInfo.java` — the display contract

```java
public interface VehicleInfo {
    void displayDetails();
    String getVehicleType();
}
```

Guarantees every vehicle can print its own details and name its type. The CLI's
search option and the GUI's table columns both lean on this contract.

### 4.3 `Vehicle.java` — the abstract base class

**Fields (all private):** `vehicleId`, `ownerName`, `ownerContact`, `brand`,
`model`, `year`, `color`, `entryTime`, `exitTime`.

**Constructor:** stores the seven identity fields and stamps `entryTime = new Date()` —
the parking clock starts the moment the object exists.

**Methods:**

- Getters for everything (read-only from outside).
- `setExitTime(Date)` — the one controlled mutation, called only at removal.
- `getParkingHours()` — `(exit − entry) / 3,600,000 ms`; if the vehicle is still
  parked, it measures up to *now*.
- `getParkingDuration()` — a human-readable `"X hours Y minutes"` string.
- `displayDetails()` — prints the common block (id, owner, contact, brand, model,
  year, color, entry time, status). Subclasses override it and extend it.

Note what `Vehicle` deliberately does **not** know: hourly rates, fee rules, slot
types. Those belong to the subclasses and the garage.

### 4.4 `Car.java` / `Bike.java` / `Truck.java` — the concrete vehicles

All three follow the same pattern:

1. **Extra private fields** — Car: `numberOfDoors`, `hasAC`; Bike: `isSportsBike`;
   Truck: `capacityInTons`.
2. **A private `parkingRatePerHour`** — 50 / 20 / 150 BDT.
3. **Constructor** calls `super(vehicleId, ownerName, ownerContact, brand, model,
   year, color)` then stores its own extras. The shared setup is inherited, not copied.
4. **Overrides:**
   - `getVehicleType()` — returns the type name (used in tables, slot display, bills).
   - `displayDetails()` — calls `super.displayDetails()` for the common block,
     then prints its own extras and its rate.
   - `calculateFee()` — the money method:

```java
// identical shape in all three, different rate constant
@Override
public double calculateFee() {
    long hours = getParkingHours();
    if (hours < 1) hours = 1;          // minimum one hour
    return hours * parkingRatePerHour;
}
```

Even a 2-minute park costs one hour — a business rule, deliberately simple.

### 4.5 `GarageSlot.java` — one parking space

**Fields:** `slotNumber` (1–30), `slotType` (`"SMALL"` / `"MEDIUM"` / `"LARGE"`),
`occupiedBy` (a `Vehicle` reference; `null` means empty).

**Why it exists:** it turns "the garage" from an abstract count into 30 *physical,
inspectable* objects. The GUI's slot table and the CLI's "View All Slots" screen
walk this list.

**Key methods:**

- `isOccupied()` — `occupiedBy != null`. Encapsulated state check.
- `parkVehicle(Vehicle)` — returns `false` if occupied; this is where the
  "one vehicle per slot" rule physically lives.
- `removeVehicle()` — returns the vehicle that was there and clears the slot.
- `displaySlot()` — one text line: `[Slot 07] OCCUPIED | Car - Habib`.

### 4.6 `GarageManagementSystem.java` — the controller (the engine)

**Fields:**

```java
private List<GarageSlot> slots;             // all 30 slots, in order
private Map<String, Vehicle> parkedVehicles; // vehicleId -> vehicle (fast lookup)
private List<Bill> billHistory;              // every issued bill, in order
```

**Why a Map for vehicles and a List for slots?** The two questions the system is
asked are different. "Is vehicle C-101 parked?" must be O(1) — that is the
`HashMap`, keyed by ID. "Show me every slot in order" must preserve slot
numbering — that is the `ArrayList`. The `slots` list also defines capacity:
`getTotalSpaces()` is `slots.size()`, and `isFull()` compares the map size
against it.

**Constructor** builds slots 1–30 and labels 1–10 SMALL, 11–20 MEDIUM, 21–30 LARGE.

**`parkVehicle(Vehicle)` — the placement algorithm:**

1. If `isFull()`, return `false` immediately.
2. Compute the preferred slot size: Car → MEDIUM, Bike → SMALL, Truck → LARGE.
3. **First pass:** find the first free slot whose type matches exactly.
4. **Fallback pass:** if none match (e.g. garage full of medium cars but a bike
   arrives), take *any* free slot — a bike can physically park in a large slot.
5. Book it: `slot.parkVehicle(vehicle)` and `parkedVehicles.put(id, vehicle)`.

The two-pass structure is the whole placement policy in six lines: *prefer the
right size, never refuse over a technicality.*

**`unParkVehicle(String vehicleId)` — the billing flow:**

1. Look up the vehicle in the map. Unknown ID → return `null` (caller prints an error).
2. Find which slot holds it (`slot.getOccupiedBy().getVehicleId().equals(vehicleId)`)
   and call `slot.removeVehicle()` — freeing the physical space.
3. Stamp `vehicle.setExitTime(new Date())` — stop the clock.
4. `double fee = vehicle.calculateFee();` — **the polymorphic dispatch moment.**
5. `new Bill(vehicle, fee, exitTime)` → add to `billHistory`, remove from the map.
6. Return the bill so the caller can show it.

**Read-only services:** `findVehicle(id)` (map get), `getAllParkedVehicles()`,
`getAllSlots()`, `getBillHistory()` (all return defensive copies so callers can't
corrupt internal state), `getTotalRevenue()` (sums the bills), plus the three
`display...()` methods that render text screens for the CLI.

### 4.7 `Bill.java` — one receipt

**Fields:** `billNumber` (`"BILL-"` + `System.currentTimeMillis()` — unique enough,
no counter to manage), `vehicle` (a *reference*, not a copy), `amount`, `issueDate`.

`toString()` pulls owner and vehicle ID **through the vehicle reference**:

```java
return String.format("Bill: %s | Vehicle: %s | Owner: %s | Amount: BDT %.2f | Date: %s",
    billNumber, vehicle.getVehicleId(), vehicle.getOwnerName(), amount,
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(issueDate));
```

One line, and it demonstrates why objects hold *references*: the bill doesn't
duplicate vehicle data, it points at it.

### 4.8 `GarageManagementApp.java` — the CLI front door

**`main`:** prints a one-line banner, asks **GUI or CLI**, and either launches
`new GarageGUI()` or calls `runCLI(...)`.

**`runCLI(Scanner, garage)` — the menu loop:**

```
1. Park a Vehicle      -> parkVehicleMenu()
2. Remove (Pay Bill)   -> removeVehicleMenu()
3. Search Vehicle      -> searchVehicleMenu()
4. View Parked         -> garage.displayAllParkedVehicles()
5. View Slots          -> garage.displayAllSlots()
6. View Bills          -> garage.displayBillHistory()
7. Exit
```

A `while (running)` loop with a `switch` — reads, dispatches, repeats. All input
parsing (and the `sc.nextLine()` calls that swallow leftover newlines after
`nextInt()`) lives here; the engine never touches the keyboard.

**`parkVehicleMenu(...)`** is the biggest CLI method: it asks the seven common
questions (ID, owner, contact, brand, model, year, color), then the type-specific
ones (doors+AC / sports / tons), constructs the right subclass, and hands it to
`garage.parkVehicle(vehicle)`. This is where the *user* meets polymorphism: one
prompt flow, three possible concrete objects.

### 4.9 `GarageGUI.java` — the Swing front door

Same operations, richer skin. The moving parts:

- **Form panel** — fields for the seven common attributes plus type-specific
  fields that show/hide as the vehicle-type combo changes (`updateFormFields()`).
  Same Q&A as the CLI, just point-and-click.
- **Park / Remove buttons** — read the fields, call `garage.parkVehicle(...)` /
  `garage.unParkVehicle(...)`, log the outcome to the console pane. Identical
  engine calls to the CLI.
- **Three tables** (`DefaultTableModel`): parked vehicles, all 30 slots, bill
  history. `refreshAllTables()` wipes and refills all three from the engine's
  `getAll*()` accessors after every action.
- **Stats labels** — total / available / parked / revenue, updated by
  `updateStats()`. A `javax.swing.Timer` ticks once per second so the numbers
  stay live even without user actions.
- **`log(String)`** — appends a timestamped line to the dark console pane, so
  every action leaves a visible trail.
- **`main`** — sets the system look-and-feel, then shows the window on the EDT
  (`SwingUtilities.invokeLater`) — the correct way to start Swing.

The GUI holds its own `new GarageManagementSystem(30)`. State is still centralized
because *every* mutation goes through the controller — the GUI never edits slots
or bills directly.

---

## 5. How everything works together — the runtime flows

### Flow A — Startup (GUI path)

```
main() -> user picks 1
       -> new GarageGUI()
            -> new GarageManagementSystem(30)   // 30 GarageSlot objects created
            -> initUI()                          // form, tables, stats, timer
```

The engine exists before any UI element that needs it. Every UI read is a method
call into that one object.

### Flow B — Parking a vehicle

```
User fills form / answers prompts
  -> new Car("C-1","Habib","0171","Toyota","Axio",2022,"White",4,true)
       -> super(...) sets 7 fields + entryTime = now
  -> garage.parkVehicle(vehicle)
       -> isFull()?  no
       -> prefer MEDIUM slot: slot 11 free? -> slot.parkVehicle(vehicle)  // true
       -> parkedVehicles["C-1"] = vehicle
       -> return true
  -> UI: "Parked successfully", tables refresh, slot 11 shows the car
```

### Flow C — Removing a vehicle (billing)

```
User enters "C-1"
  -> garage.unParkVehicle("C-1")
       -> map lookup: found
       -> slot found -> slot.removeVehicle()          // physical space freed
       -> vehicle.setExitTime(now)                     // clock stops
       -> fee = vehicle.calculateFee()                 // POLYMORPHIC: 50/20/150 * hours
       -> new Bill(vehicle, fee, exitTime)             // BILL-<timestamp>
       -> billHistory.add(bill); map.remove("C-1")
       -> return bill
  -> UI/CLI prints the bill; revenue label goes up
```

### Flow D — Querying

Search = one `map.get(id)` plus `displayDetails()` (an overridden method doing
per-type printing). Slot view = walk the `slots` list. Revenue = sum `billHistory`.
No cache to invalidate, no state to sync — there is exactly one copy of the truth,
inside the controller.

---

## 6. Design decisions worth defending in the exam

| Decision | Why |
|---|---|
| Two thin front doors, one engine | CLI and GUI can never disagree; adding a third front door (web?) needs no engine change |
| `HashMap` for vehicles, `ArrayList` for slots | O(1) ID lookup vs ordered physical layout — pick the structure for the question being asked |
| Two-pass slot matching | Right size first, any size as fallback — real garages work this way |
| Fee logic in the vehicle, not the controller | Rates are a property of the vehicle type; adding a `Van` class = one new file, zero controller edits (Open/Closed principle) |
| `GarageSlot` guards its own state | The "no double parking" rule is enforced where the data lives, not scattered across callers |
| Minimum 1-hour charge | Simple, visible business rule; one `if` line |
| Defensive copies from `getAll*()` | Callers can read but never mutate the engine's lists |

---

## 7. Running it

```bash
javac GarageManagementApp.java GarageGUI.java

java GarageManagementApp      # choose 1 = GUI, 2 = CLI
java GarageGUI                # GUI directly
```

Rates: Bike 20 · Car 50 · Truck 150 BDT/hour, minimum one hour.

## 8. One-line exam answers

- **Abstraction:** `abstract class Vehicle` + `Payable`/`VehicleInfo` interfaces — callers use `calculateFee()`/`displayDetails()` without knowing the concrete type.
- **Encapsulation:** every field `private`; `GarageSlot.parkVehicle()` enforces the one-vehicle rule inside the class.
- **Inheritance:** `Car/Bike/Truck extends Vehicle` — common fields and duration math written once in the parent.
- **Polymorphism:** `vehicle.calculateFee()` on a `Vehicle` reference dispatches to Car (50) / Bike (20) / Truck (150) at runtime; `displayDetails()` overrides layer onto `super`.
