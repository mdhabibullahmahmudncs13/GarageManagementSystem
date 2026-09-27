/*
 * ============================================================================
 * BILL CLASS
 * Pillar 2: ENCAPSULATION - All billing data private
 * ============================================================================
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill {
    private String billNumber;
    private Vehicle vehicle;
    private double amount;
    private Date issueDate;

    public Bill(Vehicle vehicle, double amount, Date issueDate) {
        this.billNumber = "BILL-" + System.currentTimeMillis();
        this.vehicle = vehicle;
        this.amount = amount;
        this.issueDate = issueDate;
    }

    public String getBillNumber() { return billNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public double getAmount() { return amount; }
    public Date getIssueDate() { return issueDate; }

    @Override
    public String toString() {
        return String.format(
            "Bill: %s | Vehicle: %s | Owner: %s | Amount: BDT %.2f | Date: %s",
            billNumber,
            vehicle.getVehicleId(),
            vehicle.getOwnerName(),
            amount,
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(issueDate)
        );
    }
}
