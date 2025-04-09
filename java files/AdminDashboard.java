import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.*;

public class AdminDashboard extends JFrame {
    private JPanel mainPanel, contentPanel;
    private CardLayout cardLayout;
    private DefaultTableModel driversTableModel;
    private DefaultTableModel bookingsTableModel;
    private DefaultTableModel usersTableModel;
    private ImageIcon driverPhoto = null;

    public AdminDashboard() {
        setTitle("Admin Dashboard - GoElite Cab Services");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main layout
        mainPanel = new JPanel(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with CardLayout to switch between views
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Add different view panels
        contentPanel.add(createViewBookingsPanel(), "viewBookings");
        contentPanel.add(createAddDriverPanel(), "addDriver");
        contentPanel.add(createRegisteredDriversPanel(), "registeredDrivers");
        contentPanel.add(createRegisteredUsersPanel(), "registeredUsers");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Set default view
        cardLayout.show(contentPanel, "viewBookings");
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(1000, 70));
        
        // Logo/Website name on left
        JLabel logoLabel = new JLabel("GoElite");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.add(logoLabel, BorderLayout.WEST);
        
        // Center navigation buttons
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        
        JButton viewBookingsBtn = createHeaderButton("View Bookings");
        JButton addDriverBtn = createHeaderButton("Add Driver");
        JButton registeredDriversBtn = createHeaderButton("Registered Drivers");
        JButton registeredUsersBtn = createHeaderButton("Registered Users");
        
        navPanel.add(viewBookingsBtn);
        navPanel.add(addDriverBtn);
        navPanel.add(registeredDriversBtn);
        navPanel.add(registeredUsersBtn);
        
        panel.add(navPanel, BorderLayout.CENTER);
        
        // Admin info and logout on right
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminPanel.setOpaque(false);
        JLabel helloAdminLabel = new JLabel("Hello, Admin");
        helloAdminLabel.setForeground(Color.WHITE);
        helloAdminLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(new Color(41, 128, 185));
        logoutBtn.addActionListener(e -> logout());
        
        adminPanel.add(helloAdminLabel);
        adminPanel.add(logoutBtn);
        adminPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panel.add(adminPanel, BorderLayout.EAST);
        
        registeredUsersBtn.addActionListener(e -> cardLayout.show(contentPanel, "registeredUsers"));
        
        return panel;
    }
    
    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 30));
        
        button.addActionListener(e -> {
            switch(text) {
                case "View Bookings":
                    refreshBookingsTable();
                    cardLayout.show(contentPanel, "viewBookings");
                    break;
                case "Add Driver":
                    cardLayout.show(contentPanel, "addDriver");
                    break;
                case "Registered Drivers":
                    refreshDriversTable();
                    cardLayout.show(contentPanel, "registeredDrivers");
                    break;
                case "Registered Users":
                    refreshUsersTable();
                    cardLayout.show(contentPanel, "registeredUsers");
                    break;
            }
        });
        return button;
    }
    
    private void refreshBookingsTable() {
        bookingsTableModel.setRowCount(0);
        
        for (GoEliteLoginSystem.Booking booking : GoEliteLoginSystem.bookings.values()) {
            GoEliteLoginSystem.User user = GoEliteLoginSystem.users.get(booking.userId);
            GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(booking.driverId);
            
            String userName = user != null ? user.name : "Unknown";
            String driverName = driver != null ? driver.name : "Not Assigned";
            
            bookingsTableModel.addRow(new Object[] {
                booking.id,
                userName,
                booking.pickup,
                booking.destination,
                booking.dateTime,
                driverName,
                booking.status
            });
        }
    }
    
    private void refreshDriversTable() {
        driversTableModel.setRowCount(0);
        
        for (GoEliteLoginSystem.Driver driver : GoEliteLoginSystem.drivers.values()) {
            driversTableModel.addRow(new Object[] {
                driver.username,
                driver.name,
                driver.phone,
                driver.experience,
                driver.licenseNumber,
                driver.vehicleType,
                "Active" // Default status
            });
        }
    }
    
    private void refreshUsersTable() {
        usersTableModel.setRowCount(0);
        
        for (GoEliteLoginSystem.User user : GoEliteLoginSystem.users.values()) {
            usersTableModel.addRow(new Object[] {
                user.username,
                user.name,
                user.email,
                user.phone,
                user.address
            });
        }
    }
    
    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Current Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Booking ID", "Customer Name", "Pickup", "Destination", "Date/Time", "Driver Assigned", "Status"};
        bookingsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        refreshBookingsTable();
        
        JTable bookingsTable = new JTable(bookingsTableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        bookingsTable.setFillsViewportHeight(true);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton assignBtn = new JButton("Assign Driver");
        JButton cancelBtn = new JButton("Cancel Booking");
        
        assignBtn.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a booking first.");
                return;
            }
            
            String bookingId = bookingsTableModel.getValueAt(selectedRow, 0).toString();
            GoEliteLoginSystem.Booking booking = GoEliteLoginSystem.bookings.get(bookingId);
            
            if (booking == null) return;
            
            String currentDriver = bookingsTableModel.getValueAt(selectedRow, 5).toString();
            String status = bookingsTableModel.getValueAt(selectedRow, 6).toString();
            
            if (!status.equals("Pending") && !currentDriver.equals("Not Assigned")) {
                JOptionPane.showMessageDialog(this, "Driver already assigned to this booking.");
                return;
            }
            
            // Get available drivers
            String[] drivers = new String[GoEliteLoginSystem.drivers.size()];
            int i = 0;
            for (GoEliteLoginSystem.Driver driver : GoEliteLoginSystem.drivers.values()) {
                drivers[i++] = driver.name;
            }
            
            String selectedDriver = (String) JOptionPane.showInputDialog(
                this, "Select a driver:", "Assign Driver",
                JOptionPane.QUESTION_MESSAGE, null, drivers, drivers[0]);
                
            if (selectedDriver != null) {
                // Find driver by name
                String driverUsername = "";
                for (GoEliteLoginSystem.Driver driver : GoEliteLoginSystem.drivers.values()) {
                    if (driver.name.equals(selectedDriver)) {
                        driverUsername = driver.username;
                        break;
                    }
                }
                
                if (!driverUsername.isEmpty()) {
                    booking.driverId = driverUsername;
                    booking.status = "Assigned";
                    
                    bookingsTableModel.setValueAt(selectedDriver, selectedRow, 5);
                    bookingsTableModel.setValueAt("Assigned", selectedRow, 6);
                    JOptionPane.showMessageDialog(this, "Driver assigned successfully!");
                }
            }
        });
        
        cancelBtn.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a booking first.");
                return;
            }
            
            String bookingId = bookingsTableModel.getValueAt(selectedRow, 0).toString();
            GoEliteLoginSystem.Booking booking = GoEliteLoginSystem.bookings.get(bookingId);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this booking?", 
                "Cancel Booking", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                booking.status = "Cancelled";
                bookingsTableModel.setValueAt("Cancelled", selectedRow, 6);
                JOptionPane.showMessageDialog(this, "Booking has been cancelled.");
            }
        });
        
        actionPanel.add(assignBtn);
        actionPanel.add(cancelBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAddDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Image upload section
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel imageLabel = new JLabel("No Image");
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setOpaque(true);
        
        JButton uploadBtn = new JButton("Upload Photo");
        imagePanel.add(imageLabel);
        imagePanel.add(uploadBtn);
        formPanel.add(imagePanel);
        
        // Add vertical space
        formPanel.add(Box.createVerticalStrut(20));
        
        // Form fields
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField expField = new JTextField(20);
        JTextField licenseField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        
        formPanel.add(createFormField("Full Name:", nameField));
        formPanel.add(createFormField("Email:", emailField));
        formPanel.add(createFormField("Phone:", phoneField));
        formPanel.add(createFormField("Address:", addressField));
        
        // Gender radio buttons
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(new JLabel("Gender:"));
        JRadioButton maleRadio = new JRadioButton("Male");
        JRadioButton femaleRadio = new JRadioButton("Female");
        JRadioButton otherRadio = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        formPanel.add(genderPanel);
        
        formPanel.add(createFormField("Years of Experience:", expField));
        
        // Vehicle type combo box
        JPanel vehiclePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vehiclePanel.add(new JLabel("Vehicle Type:"));
        String[] vehicleTypes = {"Sedan", "SUV", "Hatchback", "Luxury"};
        JComboBox<String> vehicleCombo = new JComboBox<>(vehicleTypes);
        vehiclePanel.add(vehicleCombo);
        formPanel.add(vehiclePanel);
        
        formPanel.add(createFormField("License Number:", licenseField));
        formPanel.add(createFormField("Username:", usernameField));
        formPanel.add(createFormField("Password:", passwordField));
        
        // Add vertical space before buttons
        formPanel.add(Box.createVerticalStrut(30));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        formPanel.add(buttonPanel);
        
        // Add form panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Event listeners
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    driverPhoto = new ImageIcon(selectedFile.getPath());
                    Image img = driverPhoto.getImage().getScaledInstance(
                        200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, 
                        "Error loading image: " + ex.getMessage(),
                        "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        saveBtn.addActionListener(e -> {
            if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || 
                licenseField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(panel, 
                    "Please fill all required fields (Name, Phone, License, Username, Password)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String gender = "Other";
            if (maleRadio.isSelected()) gender = "Male";
            else if (femaleRadio.isSelected()) gender = "Female";
            
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (GoEliteLoginSystem.drivers.containsKey(username)) {
                JOptionPane.showMessageDialog(panel, 
                    "Username already exists",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            GoEliteLoginSystem.Driver newDriver = new GoEliteLoginSystem.Driver(
                username,
                password,
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                licenseField.getText(),
                expField.getText(),
                vehicleCombo.getSelectedItem().toString(),
                gender
            );
            
            GoEliteLoginSystem.drivers.put(username, newDriver);
            refreshDriversTable();
            
            JOptionPane.showMessageDialog(panel, "Driver added successfully!");
            
            // Clear fields
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            addressField.setText("");
            expField.setText("");
            licenseField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            genderGroup.clearSelection();
            imageLabel.setIcon(null);
            imageLabel.setText("No Image");
            imageLabel.setBackground(Color.LIGHT_GRAY);
            
            cardLayout.show(contentPanel, "registeredDrivers");
        });
        
        cancelBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel? All data will be lost.",
                "Cancel Confirmation", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                addressField.setText("");
                expField.setText("");
                licenseField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                genderGroup.clearSelection();
                imageLabel.setIcon(null);
                imageLabel.setText("No Image");
                
                cardLayout.show(contentPanel, "registeredDrivers");
            }
        });
        
        return panel;
    }
    
    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(field);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }
    
    private JPanel createRegisteredDriversPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Registered Drivers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Username", "Name", "Phone", "Experience", "License No.", "Vehicle Type", "Status"};
        driversTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        refreshDriversTable();
        
        JTable driversTable = new JTable(driversTableModel);
        JScrollPane scrollPane = new JScrollPane(driversTable);
        driversTable.setFillsViewportHeight(true);
        driversTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewBtn = new JButton("View Details");
        JButton editBtn = new JButton("Edit Details");
        JButton deactivateBtn = new JButton("Deactivate Driver");
        
        viewBtn.addActionListener(e -> {
            int selectedRow = driversTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a driver first.");
                return;
            }
            
            String username = driversTableModel.getValueAt(selectedRow, 0).toString();
            GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(username);
            
            if (driver != null) {
                StringBuilder details = new StringBuilder();
                details.append("Username: ").append(driver.username).append("\n");
                details.append("Name: ").append(driver.name).append("\n");
                details.append("Phone: ").append(driver.phone).append("\n");
                details.append("Email: ").append(driver.email).append("\n");
                details.append("Address: ").append(driver.address).append("\n");
                details.append("Gender: ").append(driver.gender).append("\n");
                details.append("Experience: ").append(driver.experience).append("\n");
                details.append("Vehicle Type: ").append(driver.vehicleType).append("\n");
                details.append("License Number: ").append(driver.licenseNumber);
                
                JOptionPane.showMessageDialog(this, details.toString(), 
                    "Driver Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        editBtn.addActionListener(e -> {
            int selectedRow = driversTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a driver first.");
                return;
            }
            
            String username = driversTableModel.getValueAt(selectedRow, 0).toString();
            GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(username);
            
            if (driver != null) {
                JTextField nameField = new JTextField(driver.name);
                JTextField phoneField = new JTextField(driver.phone);
                JTextField emailField = new JTextField(driver.email);
                JTextField addressField = new JTextField(driver.address);
                JTextField expField = new JTextField(driver.experience);
                JTextField vehicleField = new JTextField(driver.vehicleType);
                JTextField licenseField = new JTextField(driver.licenseNumber);
                
                JPanel editPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                editPanel.add(new JLabel("Name:"));
                editPanel.add(nameField);
                editPanel.add(new JLabel("Phone:"));
                editPanel.add(phoneField);
                editPanel.add(new JLabel("Email:"));
                editPanel.add(emailField);
                editPanel.add(new JLabel("Address:"));
                editPanel.add(addressField);
                editPanel.add(new JLabel("Experience:"));
                editPanel.add(expField);
                editPanel.add(new JLabel("Vehicle Type:"));
                editPanel.add(vehicleField);
                editPanel.add(new JLabel("License Number:"));
                editPanel.add(licenseField);
                
                int result = JOptionPane.showConfirmDialog(this, editPanel, 
                    "Edit Driver Details", JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE);
                    
                if (result == JOptionPane.OK_OPTION) {
                    driver.name = nameField.getText();
                    driver.phone = phoneField.getText();
                    driver.email = emailField.getText();
                    driver.address = addressField.getText();
                    driver.experience = expField.getText();
                    driver.vehicleType = vehicleField.getText();
                    driver.licenseNumber = licenseField.getText();
                    
                    driversTableModel.setValueAt(driver.name, selectedRow, 1);
                    driversTableModel.setValueAt(driver.phone, selectedRow, 2);
                    driversTableModel.setValueAt(driver.experience, selectedRow, 3);
                    driversTableModel.setValueAt(driver.licenseNumber, selectedRow, 4);
                    driversTableModel.setValueAt(driver.vehicleType, selectedRow, 5);
                    
                    JOptionPane.showMessageDialog(this, "Driver details updated successfully!");
                }
            }
        });
        
        deactivateBtn.addActionListener(e -> {
            int selectedRow = driversTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a driver first.");
                return;
            }
            
            String username = driversTableModel.getValueAt(selectedRow, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to deactivate this driver?", 
                "Deactivate Driver", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                String currentStatus = driversTableModel.getValueAt(selectedRow, 6).toString();
                if (currentStatus.equals("Active")) {
                    driversTableModel.setValueAt("Inactive", selectedRow, 6);
                    JOptionPane.showMessageDialog(this, "Driver has been deactivated.");
                } else {
                    driversTableModel.setValueAt("Active", selectedRow, 6);
                    JOptionPane.showMessageDialog(this, "Driver has been activated.");
                }
            }
        });
        
        actionPanel.add(viewBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deactivateBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRegisteredUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Registered Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Username", "Name", "Email", "Phone", "Address"};
        usersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        refreshUsersTable();
        
        JTable usersTable = new JTable(usersTableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        usersTable.setFillsViewportHeight(true);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewBtn = new JButton("View Details");
        
        viewBtn.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user first.");
                return;
            }
            
            String username = usersTableModel.getValueAt(selectedRow, 0).toString();
            GoEliteLoginSystem.User user = GoEliteLoginSystem.users.get(username);
            
            if (user != null) {
                StringBuilder details = new StringBuilder();
                details.append("Username: ").append(user.username).append("\n");
                details.append("Name: ").append(user.name).append("\n");
                details.append("Email: ").append(user.email).append("\n");
                details.append("Phone: ").append(user.phone).append("\n");
                details.append("Address: ").append(user.address);
                
                JOptionPane.showMessageDialog(this, details.toString(), 
                    "User Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        actionPanel.add(viewBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Logout Confirmation", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new GoEliteLoginSystem().setVisible(true);
            });
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}