/*
 * ============================================================================
 * GARAGE MANAGEMENT GUI
 * Swing-based graphical interface for the Garage Management System
 * ============================================================================
 */
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GarageGUI extends JFrame {

    private GarageManagementSystem garage;
    private JTextField txtVehicleId, txtOwnerName, txtContact, txtBrand, txtModel, txtColor;
    private JTextField txtYear, txtDoors, txtCapacity;
    private JComboBox<String> cbVehicleType, cbSportsBike, cbHasAC;
    private JLabel lblDoors, lblHasAC, lblSportsBike, lblCapacity;
    private JPanel formPanel;
    private JTextArea txtOutput;

    // Stats labels stored as fields — no fragile component-tree lookups
    private JLabel lblTotal, lblAvailable, lblParked, lblRevenue;

    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel slotTableModel;
    private DefaultTableModel billTableModel;

    public GarageGUI() {
        this.garage = new GarageManagementSystem(30);
        initUI();
    }

    private void initUI() {
        setTitle("Garage Management System - OOP Project");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL - Title =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("🚗 GARAGE MANAGEMENT SYSTEM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 100, 200));
        topPanel.add(lblTitle);

        JLabel lblSubtitle = new JLabel("OOP Project | Four Pillars: Abstraction, Encapsulation, Inheritance, Polymorphism");
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitle.setForeground(Color.GRAY);
        topPanel.add(lblSubtitle);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER - Left: Park Vehicle Form | Right: Status =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // LEFT: Park Vehicle Form
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Park a Vehicle"));

        // Form
        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        cbVehicleType = new JComboBox<>(new String[]{"Car", "Bike", "Truck"});
        cbVehicleType.addActionListener(e -> updateFormFields());
        formPanel.add(cbVehicleType, gbc);

        // Vehicle ID
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1;
        txtVehicleId = new JTextField(15);
        formPanel.add(txtVehicleId, gbc);

        // Owner Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Owner Name:"), gbc);
        gbc.gridx = 1;
        txtOwnerName = new JTextField(15);
        formPanel.add(txtOwnerName, gbc);

        // Contact
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        txtContact = new JTextField(15);
        formPanel.add(txtContact, gbc);

        // Brand
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1;
        txtBrand = new JTextField(15);
        formPanel.add(txtBrand, gbc);

        // Model
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        txtModel = new JTextField(15);
        formPanel.add(txtModel, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        txtYear = new JTextField(15);
        formPanel.add(txtYear, gbc);

        // Color
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        txtColor = new JTextField(15);
        formPanel.add(txtColor, gbc);

        // Car-specific: Doors (own gridy row — no cell collisions)
        gbc.gridx = 0; gbc.gridy = 8;
        lblDoors = new JLabel("Doors:");
        formPanel.add(lblDoors, gbc);
        gbc.gridx = 1;
        txtDoors = new JTextField(15);
        txtDoors.setVisible(false);
        formPanel.add(txtDoors, gbc);

        // Car-specific: AC
        gbc.gridx = 0; gbc.gridy = 9;
        lblHasAC = new JLabel("Has AC:");
        formPanel.add(lblHasAC, gbc);
        gbc.gridx = 1;
        cbHasAC = new JComboBox<>(new String[]{"Yes", "No"});
        cbHasAC.setVisible(false);
        formPanel.add(cbHasAC, gbc);

        // Bike-specific: Sports Bike
        gbc.gridx = 0; gbc.gridy = 10;
        lblSportsBike = new JLabel("Sports Bike:");
        formPanel.add(lblSportsBike, gbc);
        gbc.gridx = 1;
        cbSportsBike = new JComboBox<>(new String[]{"Yes", "No"});
        cbSportsBike.setVisible(false);
        formPanel.add(cbSportsBike, gbc);

        // Truck-specific: Capacity
        gbc.gridx = 0; gbc.gridy = 11;
        lblCapacity = new JLabel("Capacity (tons):");
        formPanel.add(lblCapacity, gbc);
        gbc.gridx = 1;
        txtCapacity = new JTextField(15);
        txtCapacity.setVisible(false);
        formPanel.add(txtCapacity, gbc);

        leftPanel.add(formPanel, BorderLayout.NORTH);

        // Park Button
        JButton btnPark = new JButton("🚗 Park Vehicle");
        btnPark.setBackground(new Color(0, 150, 0));
        btnPark.setForeground(Color.WHITE);
        btnPark.setFont(new Font("Arial", Font.BOLD, 14));
        btnPark.addActionListener(e -> parkVehicle());
        leftPanel.add(btnPark, BorderLayout.CENTER);

        // Remove Button
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        removePanel.add(new JLabel("Remove Vehicle ID:"));
        JTextField txtRemoveId = new JTextField(10);
        removePanel.add(txtRemoveId);
        JButton btnRemove = new JButton("🚗 Remove & Pay");
        btnRemove.setBackground(new Color(200, 0, 0));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.addActionListener(e -> removeVehicle(txtRemoveId.getText()));
        removePanel.add(btnRemove);
        leftPanel.add(removePanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);

        // RIGHT: Status Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Garage Status"));

        // Stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTotal = new JLabel("Total Slots: 30");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(lblTotal);

        lblAvailable = new JLabel("Available: 30");
        lblAvailable.setFont(new Font("Arial", Font.BOLD, 14));
        lblAvailable.setForeground(new Color(0, 150, 0));
        statsPanel.add(lblAvailable);

        lblParked = new JLabel("Parked: 0");
        lblParked.setFont(new Font("Arial", Font.BOLD, 14));
        lblParked.setForeground(new Color(200, 150, 0));
        statsPanel.add(lblParked);

        lblRevenue = new JLabel("Revenue: BDT 0");
        lblRevenue.setFont(new Font("Arial", Font.BOLD, 14));
        lblRevenue.setForeground(new Color(0, 100, 200));
        statsPanel.add(lblRevenue);

        rightPanel.add(statsPanel, BorderLayout.NORTH);

        // Slots Table
        String[] slotColumns = {"Slot #", "Type", "Status", "Vehicle"};
        slotTableModel = new DefaultTableModel(slotColumns, 0);
        JTable slotTable = new JTable(slotTableModel);
        slotTable.setPreferredScrollableViewportSize(new Dimension(300, 300));
        slotTable.setFillsViewportHeight(true);
        JScrollPane slotScroll = new JScrollPane(slotTable);
        rightPanel.add(slotScroll, BorderLayout.CENTER);

        // Update stats
        Timer statsTimer = new Timer(1000, e -> updateStats());
        statsTimer.start();

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM - Parked Vehicles & Bill History =====
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Parked Vehicles Table
        JPanel parkedPanel = new JPanel(new BorderLayout());
        parkedPanel.setBorder(BorderFactory.createTitledBorder("Parked Vehicles"));
        String[] vehicleColumns = {"ID", "Owner", "Type", "Brand", "Entry Time"};
        vehicleTableModel = new DefaultTableModel(vehicleColumns, 0);
        JTable vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setPreferredScrollableViewportSize(new Dimension(400, 150));
        vehicleTable.setFillsViewportHeight(true);
        parkedPanel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        // Bill History Table
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBorder(BorderFactory.createTitledBorder("Bill History"));
        String[] billColumns = {"Bill #", "Vehicle", "Owner", "Amount", "Date"};
        billTableModel = new DefaultTableModel(billColumns, 0);
        JTable billTable = new JTable(billTableModel);
        billTable.setPreferredScrollableViewportSize(new Dimension(400, 150));
        billTable.setFillsViewportHeight(true);
        billPanel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        bottomPanel.add(parkedPanel);
        bottomPanel.add(billPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== OUTPUT CONSOLE =====
        JPanel mainCenter = new JPanel(new BorderLayout());
        mainCenter.add(centerPanel, BorderLayout.CENTER);
        
        // We'll add output to the east
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Console Output"));
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtOutput.setBackground(new Color(30, 30, 30));
        txtOutput.setForeground(new Color(0, 255, 0));
        JScrollPane outputScrollPane = new JScrollPane(txtOutput);
        eastPanel.add(outputScrollPane, BorderLayout.CENTER);
        eastPanel.setPreferredSize(new Dimension(300, 400));

        add(mainCenter, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        // Initialize
        updateFormFields();
        refreshAllTables();
        log("Garage Management System Started");
        log("Total Slots: 30 | Ready to park vehicles");
    }

    private void updateFormFields() {
        String type = (String) cbVehicleType.getSelectedItem();
        boolean isCar = type.equals("Car");
        boolean isBike = type.equals("Bike");
        boolean isTruck = type.equals("Truck");

        // Show only the fields (and their labels) relevant to the selected type
        lblDoors.setVisible(isCar);
        txtDoors.setVisible(isCar);
        lblHasAC.setVisible(isCar);
        cbHasAC.setVisible(isCar);
        lblSportsBike.setVisible(isBike);
        cbSportsBike.setVisible(isBike);
        lblCapacity.setVisible(isTruck);
        txtCapacity.setVisible(isTruck);

        // Collapse the now-empty rows immediately
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void parkVehicle() {
        try {
            String type = (String) cbVehicleType.getSelectedItem();
            String vehicleId = txtVehicleId.getText().trim();
            String ownerName = txtOwnerName.getText().trim();
            String contact = txtContact.getText().trim();
            String brand = txtBrand.getText().trim();
            String model = txtModel.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());
            String color = txtColor.getText().trim();

            if (vehicleId.isEmpty() || ownerName.isEmpty()) {
                log("❌ Error: Vehicle ID and Owner Name are required!");
                return;
            }

            Vehicle vehicle = null;

            switch (type) {
                case "Car":
                    int doors = Integer.parseInt(txtDoors.getText().trim());
                    boolean hasAC = cbHasAC.getSelectedItem().equals("Yes");
                    vehicle = new Car(vehicleId, ownerName, contact, brand, model, year, color, doors, hasAC);
                    break;
                case "Bike":
                    boolean sportsBike = cbSportsBike.getSelectedItem().equals("Yes");
                    vehicle = new Bike(vehicleId, ownerName, contact, brand, model, year, color, sportsBike);
                    break;
                case "Truck":
                    int capacity = Integer.parseInt(txtCapacity.getText().trim());
                    vehicle = new Truck(vehicleId, ownerName, contact, brand, model, year, color, capacity);
                    break;
            }

            if (garage.parkVehicle(vehicle)) {
                log("✅ Vehicle Parked Successfully!");
                log("   Vehicle ID: " + vehicleId);
                log("   Type: " + type);
                log("   Owner: " + ownerName);
                log("   Entry Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                refreshAllTables();
            } else {
                log("❌ Failed to park vehicle. Garage may be full!");
            }

            // Clear form
            txtVehicleId.setText("");
            txtOwnerName.setText("");
            txtContact.setText("");
            txtBrand.setText("");
            txtModel.setText("");
            txtYear.setText("");
            txtColor.setText("");
            txtDoors.setText("");
            txtCapacity.setText("");

        } catch (NumberFormatException e) {
            log("❌ Error: Please enter valid numbers for Year/Doors/Capacity");
        }
    }

    private void removeVehicle(String vehicleId) {
        if (vehicleId.trim().isEmpty()) {
            log("❌ Error: Please enter a Vehicle ID");
            return;
        }

        Bill bill = garage.unParkVehicle(vehicleId.trim());
        if (bill != null) {
            log("✅ Vehicle Removed Successfully!");
            log("   Vehicle ID: " + vehicleId);
            log("   Bill #: " + bill.getBillNumber());
            log("   Total Bill: BDT " + bill.getAmount());
            refreshAllTables();
        } else {
            log("❌ Vehicle not found in garage!");
        }
    }

    private void updateStats() {
        int total = garage.getTotalSpaces();
        int avail = garage.getAvailableSlots();
        int parkedCount = total - avail;
        double rev = garage.getTotalRevenue();

        lblTotal.setText("Total Slots: " + total);
        lblAvailable.setText("Available: " + avail);
        lblParked.setText("Parked: " + parkedCount);
        lblRevenue.setText(String.format("Revenue: BDT %.2f", rev));

        if (avail == 0) {
            lblAvailable.setForeground(new Color(200, 0, 0));
        } else {
            lblAvailable.setForeground(new Color(0, 150, 0));
        }
    }

    private void refreshAllTables() {
        // Refresh vehicle table
        vehicleTableModel.setRowCount(0);
        for (Vehicle v : garage.getAllParkedVehicles()) {
            vehicleTableModel.addRow(new Object[]{
                v.getVehicleId(),
                v.getOwnerName(),
                v.getVehicleType(),
                v.getBrand(),
                new SimpleDateFormat("HH:mm:ss").format(v.getEntryTime())
            });
        }

        // Refresh slot table
        slotTableModel.setRowCount(0);
        for (GarageSlot slot : garage.getAllSlots()) {
            String status = slot.isOccupied() ? "OCCUPIED" : "EMPTY";
            String vehicle = slot.isOccupied() ?
                slot.getOccupiedBy().getVehicleType() + " - " + slot.getOccupiedBy().getOwnerName() : "-";
            slotTableModel.addRow(new Object[]{
                slot.getSlotNumber(),
                slot.getSlotType(),
                status,
                vehicle
            });
        }

        // Refresh bill table
        billTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        for (Bill bill : garage.getBillHistory()) {
            billTableModel.addRow(new Object[]{
                bill.getBillNumber(),
                bill.getVehicle().getVehicleId(),
                bill.getVehicle().getOwnerName(),
                String.format("BDT %.2f", bill.getAmount()),
                sdf.format(bill.getIssueDate())
            });
        }

        // Refresh stats
        updateStats();
    }

    private void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        txtOutput.append("[" + timestamp + "] " + message + "\n");
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GarageGUI().setVisible(true);
        });
    }
}
