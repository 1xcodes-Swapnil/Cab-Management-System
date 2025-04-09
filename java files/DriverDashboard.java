import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class DriverDashboard extends JFrame {
    private JPanel contentPane, headerPanel, mainPanel;
    private JLabel welcomeLabel, driverNameLabel, logoLabel;
    private JButton homeBtn, customersBtn, editProfileBtn, logoutBtn;
    private String driverUsername;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultTableModel bookingsTableModel;
    private JComboBox<String> filterCombo;
    
    public DriverDashboard(String driverUsername) {
        this.driverUsername = driverUsername;       setTitle("GoElite - Driver Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        createLayout();
        addActionListeners();
    }
    
    private void initComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header panel
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(new Color(51, 153, 255));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        logoLabel = new JLabel("GoElite");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(51, 153, 255));
        homeBtn = createHeaderButton("Home");
        customersBtn = createHeaderButton("Customers");
        editProfileBtn = createHeaderButton("Edit Profile");
        navPanel.add(homeBtn);
        navPanel.add(customersBtn);
        navPanel.add(editProfileBtn);
        
        JPanel driverInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        driverInfoPanel.setBackground(new Color(51, 153, 255));
        GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(driverUsername);
        driverNameLabel = new JLabel("Hello, " + (driver != null ? driver.name : "Driver"));
        driverNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        driverNameLabel.setForeground(Color.WHITE);
        logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 102, 102));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        driverInfoPanel.add(driverNameLabel);
        driverInfoPanel.add(logoutBtn);
        
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.CENTER);
        headerPanel.add(driverInfoPanel, BorderLayout.EAST);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createHomePanel(), "home");
        cardPanel.add(createCustomersPanel(), "customers");
        cardPanel.add(createEditProfilePanel(), "editProfile");
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(cardPanel, BorderLayout.CENTER);
    }
    
    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(51, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }
    
    private void createLayout() {
        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        setContentPane(contentPane);
    }
    
    private void addActionListeners() {
        homeBtn.addActionListener(e -> cardLayout.show(cardPanel, "home"));
        customersBtn.addActionListener(e -> {
            refreshBookingsTable();
            cardLayout.show(cardPanel, "customers");
        });
        editProfileBtn.addActionListener(e -> cardLayout.show(cardPanel, "editProfile"));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    new GoEliteLoginSystem().setVisible(true);
                });
            }
        });
    }
    
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon originalIcon = new ImageIcon("m.png");
            if (originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("GoElite Logo");
                imageLabel.setFont(new Font("Arial", Font.BOLD, 18));
            }
        } catch (Exception e) {
            imageLabel.setText("GoElite Logo");
            imageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(driverUsername);
        String driverName = driver != null ? driver.name : "Driver";
        
        JLabel welcomeMsg = new JLabel("Welcome, " + driverName);
        welcomeMsg.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea infoText = new JTextArea(
                "GoElite helps drivers connect with customers easily and efficiently.\n\n" +
                "• Get bookings directly from customers\n" +
                "• Manage your schedule easily\n" +
                "• Track your earnings\n" +
                "• Build your customer base\n\n");
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setFont(new Font("Arial", Font.PLAIN, 16));
        infoText.setBackground(panel.getBackground());
        infoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(welcomeMsg);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(infoText);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewBookingsBtn = new JButton("View Customer Bookings");
        viewBookingsBtn.setPreferredSize(new Dimension(300, 50));
        viewBookingsBtn.setFont(new Font("Arial", Font.BOLD, 16));
        viewBookingsBtn.addActionListener(e -> {
            refreshBookingsTable();
            cardLayout.show(cardPanel, "customers");
        });
        buttonPanel.add(viewBookingsBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshBookingsTable() {
        bookingsTableModel.setRowCount(0);
        
        for (GoEliteLoginSystem.Booking booking : GoEliteLoginSystem.bookings.values()) {
            if (booking.driverId.equals(driverUsername)) {
                GoEliteLoginSystem.User user = GoEliteLoginSystem.users.get(booking.userId);
                String userName = user != null ? user.name : "Unknown";
                
                bookingsTableModel.addRow(new Object[] {
                    booking.id,
                    userName,
                    booking.pickup,
                    booking.destination,
                    booking.dateTime,
                    booking.status,
                    getActionForStatus(booking.status)
                });
            }
        }
    }
    
    private String getActionForStatus(String status) {
        switch(status) {
            case "Pending": return "Accept/Reject";
            case "Accepted": return "Start Trip";
            case "In Progress": return "End Trip";
            case "Completed": return "View Details";
            case "Cancelled": return "View Details";
            default: return "N/A";
        }
    }
    
    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Customer Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Booking ID", "Customer Name", "Pickup Location", "Destination", "Date/Time", "Status", "Action"};
        bookingsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        refreshBookingsTable();
        
        JTable table = new JTable(bookingsTableModel);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh Bookings");
        refreshBtn.addActionListener(e -> refreshBookingsTable());
        controlPanel.add(refreshBtn);
        
        filterCombo = new JComboBox<>(new String[]{"All Bookings", "Pending", "Accepted", "In Progress", "Completed", "Cancelled"});
        filterCombo.addActionListener(e -> filterBookings());
        controlPanel.add(new JLabel("Filter: "));
        controlPanel.add(filterCombo);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void filterBookings() {
        String filterOption = (String) filterCombo.getSelectedItem();
        
        if (filterOption.equals("All Bookings")) {
            refreshBookingsTable();
            return;
        }
        
        int rowCount = bookingsTableModel.getRowCount();
        
        for (int i = rowCount - 1; i >= 0; i--) {
            String status = (String) bookingsTableModel.getValueAt(i, 5);
            if (!status.equals(filterOption)) {
                bookingsTableModel.removeRow(i);
            }
        }
        
        if (bookingsTableModel.getRowCount() == 0) {
            refreshBookingsTable();
            JOptionPane.showMessageDialog(this, "No bookings found with status: " + filterOption);
        }
    }
    
    private JPanel createEditProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create final reference to driver object
        final GoEliteLoginSystem.Driver driver = GoEliteLoginSystem.drivers.get(driverUsername);
        
        JPanel picPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(120, 120));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        profilePicLabel.setBackground(Color.LIGHT_GRAY);
        profilePicLabel.setOpaque(true);
        
        JButton uploadBtn = new JButton("Upload Photo");
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ImageIcon icon = new ImageIcon(selectedFile.getPath());
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    profilePicLabel.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error loading image");
                }
            }
        });
        
        picPanel.add(profilePicLabel);
        picPanel.add(uploadBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(picPanel, gbc);
        
        String[] labels = {"Full Name:", "Email:", "Phone:", "Address:", "Gender:", "Years of Experience:", "Vehicle Type:", "License Number:"};
        final JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            fields[i] = new JTextField(20);
            
            // Set initial values
            if (driver != null) {
                switch(i) {
                    case 0: fields[i].setText(driver.name); break;
                    case 1: fields[i].setText(driver.email); break;
                    case 2: fields[i].setText(driver.phone); break;
                    case 3: fields[i].setText(driver.address); break;
                    case 5: fields[i].setText(driver.experience); break;
                    case 6: fields[i].setText(driver.vehicleType); break;
                    case 7: fields[i].setText(driver.licenseNumber); break;
                }
            }
            
            if (i == 4) {
                JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                JRadioButton maleRadio = new JRadioButton("Male");
                JRadioButton femaleRadio = new JRadioButton("Female");
                JRadioButton otherRadio = new JRadioButton("Other");
                
                ButtonGroup genderGroup = new ButtonGroup();
                genderGroup.add(maleRadio);
                genderGroup.add(femaleRadio);
                genderGroup.add(otherRadio);
                
                if (driver != null) {
                    if (driver.gender.equals("Male")) maleRadio.setSelected(true);
                    else if (driver.gender.equals("Female")) femaleRadio.setSelected(true);
                    else otherRadio.setSelected(true);
                }
                
                genderPanel.add(maleRadio);
                genderPanel.add(femaleRadio);
                genderPanel.add(otherRadio);
                
                gbc.gridx = 0;
                gbc.gridy = i + 1;
                gbc.gridwidth = 1;
                formPanel.add(label, gbc);
                
                gbc.gridx = 1;
                formPanel.add(genderPanel, gbc);
                continue;
            }
            
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            formPanel.add(label, gbc);
            
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            // Create final references for lambda
            final GoEliteLoginSystem.Driver finalDriver = driver;
            final JLabel finalDriverNameLabel = driverNameLabel;
            final CardLayout finalCardLayout = cardLayout;
            final JPanel finalCardPanel = cardPanel;
            
            if (finalDriver != null) {
                finalDriver.name = fields[0].getText();
                finalDriver.email = fields[1].getText();
                finalDriver.phone = fields[2].getText();
                finalDriver.address = fields[3].getText();
                finalDriver.experience = fields[5].getText();
                finalDriver.vehicleType = fields[6].getText();
                finalDriver.licenseNumber = fields[7].getText();
                
                JOptionPane.showMessageDialog(panel, "Profile updated successfully!");
                finalDriverNameLabel.setText("Hello, " + finalDriver.name);
                finalCardLayout.show(finalCardPanel, "home");
            }
        });
        
        cancelBtn.addActionListener(e -> cardLayout.show(cardPanel, "home"));
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private DriverDashboard dashboard;
        private String bookingId, customerName;
        
        public ButtonEditor(JCheckBox checkBox, DriverDashboard dashboard) {
            super(checkBox);
            this.dashboard = dashboard;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                performAction();
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            
            bookingId = table.getValueAt(row, 0).toString();
            customerName = table.getValueAt(row, 1).toString();
            
            return button;
        }
        
        private void performAction() {
            GoEliteLoginSystem.Booking booking = GoEliteLoginSystem.bookings.get(bookingId);
            if (booking == null) return;
            
            if ("Accept/Reject".equals(label)) {
                Object[] options = {"Accept", "Reject", "Cancel"};
                int choice = JOptionPane.showOptionDialog(dashboard,
                        "Booking ID: " + bookingId + "\nCustomer: " + customerName + "\n\nWhat would you like to do?",
                        "Booking Action",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                
                if (choice == 0) {
                    booking.status = "Accepted";
                    updateBookingStatus(bookingId, "Accepted", "Start Trip");
                    JOptionPane.showMessageDialog(dashboard, "Booking accepted successfully!");
                } else if (choice == 1) {
                    booking.status = "Rejected";
                    updateBookingStatus(bookingId, "Rejected", "N/A");
                    JOptionPane.showMessageDialog(dashboard, "Booking rejected.");
                }
            } else if ("Start Trip".equals(label)) {
                int confirm = JOptionPane.showConfirmDialog(dashboard,
                        "Start trip for booking " + bookingId + " with " + customerName + "?",
                        "Start Trip", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    booking.status = "In Progress";
                    updateBookingStatus(bookingId, "In Progress", "End Trip");
                    JOptionPane.showMessageDialog(dashboard, "Trip started successfully!");
                }
            } else if ("End Trip".equals(label)) {
                int confirm = JOptionPane.showConfirmDialog(dashboard,
                        "End trip for booking " + bookingId + " with " + customerName + "?",
                        "End Trip", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    booking.status = "Completed";
                    updateBookingStatus(bookingId, "Completed", "View Details");
                    JOptionPane.showMessageDialog(dashboard, "Trip completed successfully!");
                }
            } else if ("View Details".equals(label)) {
                GoEliteLoginSystem.User user = GoEliteLoginSystem.users.get(booking.userId);
                String userName = user != null ? user.name : "Unknown";
                
                JOptionPane.showMessageDialog(dashboard, 
                        "Booking Details\n\n" +
                        "Booking ID: " + booking.id + "\n" +
                        "Customer: " + userName + "\n" +
                        "Pickup: " + booking.pickup + "\n" +
                        "Destination: " + booking.destination + "\n" +
                        "Date/Time: " + booking.dateTime + "\n" +
                        "Status: " + booking.status + "\n" +
                        "Fare: $" + booking.fare,
                        "Booking Details", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        private void updateBookingStatus(String bookingId, String newStatus, String newAction) {
            for (int i = 0; i < bookingsTableModel.getRowCount(); i++) {
                if (bookingId.equals(bookingsTableModel.getValueAt(i, 0))) {
                    bookingsTableModel.setValueAt(newStatus, i, 5);
                    bookingsTableModel.setValueAt(newAction, i, 6);
                    break;
                }
            }
        }
        
        public Object getCellEditorValue() {
            return label;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DriverDashboard dashboard = new DriverDashboard("driver1");
            dashboard.setVisible(true);
        });
    }
}