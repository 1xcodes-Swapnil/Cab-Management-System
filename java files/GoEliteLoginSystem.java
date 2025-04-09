import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GoEliteLoginSystem extends JFrame {
    private JPanel mainPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel logoLabel;
    private JLabel welcomeLabel;
    private JToggleButton userToggle;
    private JToggleButton driverToggle;
    private JToggleButton adminToggle;
    private ButtonGroup toggleGroup;
    
    private JPanel userPanel;
    private JPanel driverPanel;
    private JPanel adminPanel;
    
    private final String USER_PANEL = "USER";
    private final String DRIVER_PANEL = "DRIVER";
    private final String ADMIN_PANEL = "ADMIN";
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    
    // Database simulation
    public static Map<String, User> users = new HashMap<>();
    public static Map<String, Driver> drivers = new HashMap<>();
    public static Map<String, Booking> bookings = new HashMap<>();
    
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin1234";
    
    static {
        // Add some sample data
        users.put("user1", new User("user1", "password1", "John Doe", "john@example.com", "1234567890", "123 Main St"));
        drivers.put("driver1", new Driver("driver1", "password1", "Mike Driver", "mike@example.com", "9876543210", "456 Driver St", 
                                         "DL12345", "5", "Sedan", "Male"));
    }

    public GoEliteLoginSystem() {
        setTitle("GoElite Cab Booking");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SECONDARY_COLOR);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(SECONDARY_COLOR);
        
        welcomeLabel = new JLabel("Welcome to GoElite");
        welcomeLabel.setFont(HEADER_FONT);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoLabel = createLogo();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel togglePanel = new JPanel();
        togglePanel.setBackground(SECONDARY_COLOR);
        togglePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        toggleGroup = new ButtonGroup();
        userToggle = createToggleButton("User");
        driverToggle = createToggleButton("Driver");
        adminToggle = createToggleButton("Admin");
        userToggle.setSelected(true);
        
        togglePanel.add(userToggle);
        togglePanel.add(driverToggle);
        togglePanel.add(adminToggle);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        userPanel = createUserPanel();
        driverPanel = createDriverPanel();
        adminPanel = createAdminPanel();
        
        cardPanel.add(userPanel, USER_PANEL);
        cardPanel.add(driverPanel, DRIVER_PANEL);
        cardPanel.add(adminPanel, ADMIN_PANEL);
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(togglePanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(cardPanel);
        centerPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        registerListeners();
    }
    
    private JLabel createLogo() {
        JLabel logo = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillOval(10, 10, 130, 130);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(30, 65, 90, 30, 10, 10);
                g2d.fillRoundRect(45, 40, 50, 25, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(40, 85, 20, 20);
                g2d.fillOval(90, 85, 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.fillOval(45, 90, 10, 10);
                g2d.fillOval(95, 90, 10, 10);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                g2d.drawString("GoElite", 45, 130);
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(150, 150);
            }
        };
        return logo;
    }
    
    private JToggleButton createToggleButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(PRIMARY_COLOR);
        toggleGroup.add(button);
        return button;
    }
    
    private void registerListeners() {
        userToggle.addActionListener(e -> cardLayout.show(cardPanel, USER_PANEL));
        driverToggle.addActionListener(e -> cardLayout.show(cardPanel, DRIVER_PANEL));
        adminToggle.addActionListener(e -> cardLayout.show(cardPanel, ADMIN_PANEL));
    }
    
    private JPanel createUserPanel() {
        return createAuthPanel("User");
    }
    
    private JPanel createDriverPanel() {
        return createAuthPanel("Driver");
    }
    
    private JPanel createAdminPanel() {
        return createAuthPanel("Admin");
    }
    
    private JPanel createAuthPanel(String userType) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JTabbedPane authTabs = new JTabbedPane();
        authTabs.setFont(REGULAR_FONT);
        
        JPanel loginPanel = createLoginPanel(userType);
        authTabs.addTab("Sign In", loginPanel);
        
        if (!userType.equals("Admin")) {
            JPanel registerPanel = createRegisterPanel(userType);
            authTabs.addTab("Sign Up", registerPanel);
        }
        
        panel.add(authTabs);
        return panel;
    }
    
    private JPanel createLoginPanel(String userType) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titleLabel = new JLabel(userType + " Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameLabel, gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);
        
        JCheckBox rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(REGULAR_FONT);
        rememberMeCheckbox.setBackground(SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(rememberMeCheckbox, gbc);
        
        JButton loginButton = new JButton("Sign In");
        loginButton.setFont(REGULAR_FONT);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);
        
        JLabel forgotPasswordLabel = new JLabel("Forgot password?");
        forgotPasswordLabel.setFont(REGULAR_FONT);
        forgotPasswordLabel.setForeground(PRIMARY_COLOR);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(forgotPasswordLabel, gbc);
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (validateLogin(userType, username, password)) {
                handleSuccessfulLogin(username, userType);
            }
        });
        
        return panel;
    }
    
    private boolean validateLogin(String userType, String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username and password cannot be empty!",
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (userType.equals("Admin")) {
            boolean valid = username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
            if (!valid) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid admin credentials!",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            }
            return valid;
        } else if (userType.equals("User")) {
            User user = users.get(username);
            if (user == null || !user.password.equals(password)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password!",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } else if (userType.equals("Driver")) {
            Driver driver = drivers.get(username);
            if (driver == null || !driver.password.equals(password)) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password!",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    private void handleSuccessfulLogin(String username, String userType) {
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            switch(userType) {
                case "Admin":
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                    break;
                case "Driver":
                    DriverDashboard driverDashboard = new DriverDashboard(username);
                    driverDashboard.setVisible(true);
                    break;
                default:
                    GoEliteDashboard userDashboard = new GoEliteDashboard(username);
                    userDashboard.setVisible(true);
            }
        });
    }
    
    private JPanel createRegisterPanel(String userType) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titleLabel = new JLabel(userType + " Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);
        
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(emailField, gbc);
        
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(phoneLabel, gbc);
        
        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(phoneField, gbc);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(usernameLabel, gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(passwordField, gbc);
        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(confirmPasswordLabel, gbc);
        
        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(confirmPasswordField, gbc);
        
        // Declare as final arrays to work around lambda restriction
        final JTextField[] licenseFieldHolder = new JTextField[1];
        final JComboBox<String>[] vehicleTypeComboBoxHolder = new JComboBox[1];
        
        if (userType.equals("Driver")) {
            JLabel licenseLabel = new JLabel("License Number:");
            licenseLabel.setFont(REGULAR_FONT);
            gbc.gridx = 0;
            gbc.gridy = 7;
            panel.add(licenseLabel, gbc);
            
            licenseFieldHolder[0] = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = 7;
            panel.add(licenseFieldHolder[0], gbc);
            
            JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
            vehicleTypeLabel.setFont(REGULAR_FONT);
            gbc.gridx = 0;
            gbc.gridy = 8;
            panel.add(vehicleTypeLabel, gbc);
            
            String[] vehicleTypes = {"Sedan", "SUV", "Hatchback", "Luxury"};
            vehicleTypeComboBoxHolder[0] = new JComboBox<>(vehicleTypes);
            gbc.gridx = 1;
            gbc.gridy = 8;
            panel.add(vehicleTypeComboBoxHolder[0], gbc);
        }
        
        JCheckBox termsCheckbox = new JCheckBox("I agree to the Terms and Conditions");
        termsCheckbox.setFont(REGULAR_FONT);
        termsCheckbox.setBackground(SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = userType.equals("Driver") ? 9 : 7;
        gbc.gridwidth = 2;
        panel.add(termsCheckbox, gbc);
        
        JButton registerButton = new JButton("Sign Up");
        registerButton.setFont(REGULAR_FONT);
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = userType.equals("Driver") ? 10 : 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);
        
        registerButton.addActionListener(e -> {
            if (!termsCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(this, 
                    "Please agree to the Terms and Conditions",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Passwords do not match",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String username = usernameField.getText();
            
            if (userType.equals("User")) {
                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(this, 
                        "Username already exists",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User newUser = new User(username, password, nameField.getText(), 
                                      emailField.getText(), phoneField.getText(), "");
                users.put(username, newUser);
            } else if (userType.equals("Driver")) {
                if (drivers.containsKey(username)) {
                    JOptionPane.showMessageDialog(this, 
                        "Username already exists",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Driver newDriver = new Driver(username, password, nameField.getText(), 
                                            emailField.getText(), phoneField.getText(), "",
                                            licenseFieldHolder[0].getText(), "0", 
                                            vehicleTypeComboBoxHolder[0].getSelectedItem().toString(), "Male");
                drivers.put(username, newDriver);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Registration successful! Please login with your credentials.",
                "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        return panel;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GoEliteLoginSystem());
    }
    
    // Data classes
    public static class User {
        String username;
        String password;
        String name;
        String email;
        String phone;
        String address;
        
        public User(String username, String password, String name, String email, String phone, String address) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.address = address;
        }
    }
    
    public static class Driver {
        String username;
        String password;
        String name;
        String email;
        String phone;
        String address;
        String licenseNumber;
        String experience;
        String vehicleType;
        String gender;
        
        public Driver(String username, String password, String name, String email, String phone, 
                     String address, String licenseNumber, String experience, 
                     String vehicleType, String gender) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.licenseNumber = licenseNumber;
            this.experience = experience;
            this.vehicleType = vehicleType;
            this.gender = gender;
        }
    }
    
    public static class Booking {
        String id;
        String userId;
        String driverId;
        String pickup;
        String destination;
        String dateTime;
        String status;
        String fare;
        
        public Booking(String id, String userId, String driverId, String pickup, 
                      String destination, String dateTime, String status, String fare) {
            this.id = id;
            this.userId = userId;
            this.driverId = driverId;
            this.pickup = pickup;
            this.destination = destination;
            this.dateTime = dateTime;
            this.status = status;
            this.fare = fare;
        }
    }
}