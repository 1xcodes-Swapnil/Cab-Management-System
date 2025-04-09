import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GoEliteDashboard extends JFrame {
    private final String username;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JTextField pickupField, destinationField;
    private JComboBox<String> cabTypeCombo;
    private JLabel fareLabel;
    
    public GoEliteDashboard(String username) {
        this.username = username;
        setTitle("GoElite - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createMainPanel();
        setVisible(true);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        mainContentPanel.add(createHomePage(), "home");
        mainContentPanel.add(createAboutUsPage(), "aboutUs");
        mainContentPanel.add(createContactUsPage(), "contactUs");
        mainContentPanel.add(createBookRidePage(), "bookRide");
        
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        add(mainPanel);
        cardLayout.show(mainContentPanel, "home");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setBackground(new Color(40, 116, 166));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(new Color(40, 116, 166));
        
        JLabel logoLabel = new JLabel("GoElite");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        navPanel.setBackground(new Color(40, 116, 166));
        
        String[] navItems = {"Home", "About Us", "Contact Us", "Book a Ride"};
        Dimension buttonSize = new Dimension(120, 35);
        
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setPreferredSize(buttonSize);
            navButton.setFocusPainted(false);
            navButton.setFont(new Font("Arial", Font.PLAIN, 14));
            navButton.setBackground(new Color(52, 152, 219));
            navButton.setForeground(Color.WHITE);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            navButton.addActionListener(e -> {
                String page = switch(item) {
                    case "Home" -> "home";
                    case "About Us" -> "aboutUs";
                    case "Contact Us" -> "contactUs";
                    case "Book a Ride" -> "bookRide";
                    default -> "home";
                };
                cardLayout.show(mainContentPanel, page);
            });
            
            navPanel.add(navButton);
        }
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(40, 116, 166));
        
        JLabel welcomeLabel = new JLabel("Hello, " + GoEliteLoginSystem.users.get(username).name);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        userPanel.add(welcomeLabel);
        
        JButton userDropdown = new JButton("▼");
        userDropdown.setFocusPainted(false);
        userDropdown.setFont(new Font("Arial", Font.BOLD, 12));
        userDropdown.setBackground(new Color(52, 152, 219));
        userDropdown.setForeground(Color.WHITE);
        
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editDetails = new JMenuItem("Edit Details");
        JMenuItem logout = new JMenuItem("Logout");
        
        editDetails.addActionListener(e -> showEditDetailsDialog());
        logout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new GoEliteLoginSystem().setVisible(true));
            }
        });
        
        popupMenu.add(editDetails);
        popupMenu.add(logout);
        userDropdown.addActionListener(e -> popupMenu.show(userDropdown, 0, userDropdown.getHeight()));
        userPanel.add(userDropdown);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.CENTER);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void showEditDetailsDialog() {
        GoEliteLoginSystem.User user = GoEliteLoginSystem.users.get(username);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(user.name);
        JTextField emailField = new JTextField(user.email);
        JTextField phoneField = new JTextField(user.phone);
        JTextField addressField = new JTextField(user.address);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Profile", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            user.name = nameField.getText();
            user.email = emailField.getText();
            user.phone = phoneField.getText();
            user.address = addressField.getText();
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        }
    }
    
    private JComponent createHomePage() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel bannerImage = new JLabel(loadImage("/bb.png", 900, 300));
        bannerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(bannerImage);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel welcomeText = new JLabel("Welcome to GoElite");
        welcomeText.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(welcomeText);
        homePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JTextArea aboutText = new JTextArea("GoElite Cabs provides premium taxi service with professional drivers, " +
                                            "comfortable vehicles, and competitive rates. We promise punctuality, " +
                                            "safety, and a smooth ride every time. Experience the difference with GoElite!");
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);
        aboutText.setBackground(homePanel.getBackground());
        aboutText.setFont(new Font("Arial", Font.PLAIN, 16));
        aboutText.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(aboutText);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JButton bookRideButton = new JButton("Book a Ride");
        bookRideButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookRideButton.setBackground(new Color(52, 152, 219));
        bookRideButton.setForeground(Color.WHITE);
        bookRideButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookRideButton.setPreferredSize(new Dimension(200, 40));
        bookRideButton.setMaximumSize(new Dimension(200, 40));
        bookRideButton.setFocusPainted(false);
        bookRideButton.addActionListener(e -> cardLayout.show(mainContentPanel, "bookRide"));
        homePanel.add(bookRideButton);
        homePanel.add(Box.createVerticalGlue());
        
        return new JScrollPane(homePanel);
    }
    
    private JComponent createAboutUsPage() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel bannerImage = new JLabel(loadImage("/aa.png", 900, 300));
        bannerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutPanel.add(bannerImage);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel aboutUsTitle = new JLabel("About GoElite");
        aboutUsTitle.setFont(new Font("Arial", Font.BOLD, 28));
        aboutUsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutPanel.add(aboutUsTitle);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JTextArea aboutUsText = new JTextArea("GoElite Taxi Service was founded in 2025 with a vision to revolutionize " +
                                              "the way people travel in cities. Our fleet consists of modern, comfortable " +
                                              "vehicles driven by experienced professionals who are committed to providing " +
                                              "exceptional service.\n\n" +
                                              "We prioritize safety, comfort, and convenience for our customers. All our drivers " +
                                              "undergo rigorous background checks and training to ensure the highest standards " +
                                              "of service and security.\n\n" +
                                              "At GoElite, we leverage technology to make booking and tracking your ride seamless. " +
                                              "Our mission is to provide reliable transportation that gets you where you need to go, " +
                                              "when you need to be there.");
        aboutUsText.setWrapStyleWord(true);
        aboutUsText.setLineWrap(true);
        aboutUsText.setEditable(false);
        aboutUsText.setBackground(aboutPanel.getBackground());
        aboutUsText.setFont(new Font("Arial", Font.PLAIN, 16));
        aboutUsText.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutPanel.add(aboutUsText);
        aboutPanel.add(Box.createVerticalGlue());
        
        return new JScrollPane(aboutPanel);
    }
    
    private JComponent createContactUsPage() {
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel bannerImage = new JLabel(loadImage("/cc.png", 900, 300));
        bannerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactPanel.add(bannerImage);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel contactTitle = new JLabel("Contact Us");
        contactTitle.setFont(new Font("Arial", Font.BOLD, 28));
        contactTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactPanel.add(contactTitle);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.setBackground(new Color(240, 240, 240));
        detailsPanel.setMaximumSize(new Dimension(500, 300));
        
        String[][] contactDetails = {
            {"Phone:", "1-800-GO-ELITE (1-800-465-3483)"},
            {"Email:", "support@goelite.com"},
            {"Address:", "123 Transport Avenue, Suite 400, New York, NY 10001"},
            {"Business Hours:", "24/7 - We're always here for you!"}
        };
        
        for (String[] detail : contactDetails) {
            JPanel detailRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            detailRow.setBackground(new Color(240, 240, 240));
            
            JLabel label = new JLabel(detail[0]);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            detailRow.add(label);
            
            JLabel value = new JLabel(detail[1]);
            value.setFont(new Font("Arial", Font.PLAIN, 16));
            detailRow.add(value);
            
            detailsPanel.add(detailRow);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        contactPanel.add(detailsPanel);
        contactPanel.add(Box.createVerticalGlue());
        
        return new JScrollPane(contactPanel);
    }
    
    private JComponent createBookRidePage() {
        JPanel bookRidePanel = new JPanel();
        bookRidePanel.setLayout(new BoxLayout(bookRidePanel, BoxLayout.Y_AXIS));
        bookRidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel bookRideTitle = new JLabel("Book a Ride");
        bookRideTitle.setFont(new Font("Arial", Font.BOLD, 28));
        bookRideTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookRidePanel.add(bookRideTitle);
        bookRidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Ride Details"));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(600, 300));
        
        JPanel pickupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pickupLabel = new JLabel("Pickup Location:");
        pickupLabel.setPreferredSize(new Dimension(120, 25));
        pickupField = new JTextField(30);
        pickupPanel.add(pickupLabel);
        pickupPanel.add(pickupField);
        formPanel.add(pickupPanel);
        
        JPanel destPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel destLabel = new JLabel("Destination:");
        destLabel.setPreferredSize(new Dimension(120, 25));
        destinationField = new JTextField(30);
        destPanel.add(destLabel);
        destPanel.add(destinationField);
        formPanel.add(destPanel);
        
        JPanel cabTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cabTypeLabel = new JLabel("Cab Type:");
        cabTypeLabel.setPreferredSize(new Dimension(120, 25));
        String[] cabTypes = {"Sedan", "SUV", "Hatchback", "Luxury"};
        cabTypeCombo = new JComboBox<>(cabTypes);
        cabTypePanel.add(cabTypeLabel);
        cabTypePanel.add(cabTypeCombo);
        formPanel.add(cabTypePanel);
        
        JPanel farePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fareTextLabel = new JLabel("Estimated Fare:");
        fareTextLabel.setPreferredSize(new Dimension(120, 25));
        fareLabel = new JLabel("$0.00");
        farePanel.add(fareTextLabel);
        farePanel.add(fareLabel);
        formPanel.add(farePanel);
        
        cabTypeCombo.addActionListener(e -> updateFare());
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton searchButton = new JButton("Search Available Cabs");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchPanel.add(searchButton);
        formPanel.add(searchPanel);
        
        bookRidePanel.add(formPanel);
        bookRidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel cabsPanel = new JPanel();
        cabsPanel.setLayout(new BoxLayout(cabsPanel, BoxLayout.Y_AXIS));
        cabsPanel.setBorder(BorderFactory.createTitledBorder("Available Cabs"));
        cabsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel noCabsLabel = new JLabel("Enter pickup and destination to see available cabs");
        noCabsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabsPanel.add(noCabsLabel);
        
        searchButton.addActionListener(e -> {
            if (pickupField.getText().isEmpty() || destinationField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both pickup location and destination");
                return;
            }
            
            cabsPanel.removeAll();
            
            JLabel resultsHeader = new JLabel("Cabs from " + pickupField.getText() + " to " + destinationField.getText());
            resultsHeader.setFont(new Font("Arial", Font.BOLD, 16));
            resultsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            cabsPanel.add(resultsHeader);
            cabsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Get available drivers for the selected cab type
            String selectedCabType = (String) cabTypeCombo.getSelectedItem();
            
            for (GoEliteLoginSystem.Driver driver : GoEliteLoginSystem.drivers.values()) {
                if (driver.vehicleType.equals(selectedCabType)) {
                    JPanel cabCard = createDriverCard(driver);
                    cabsPanel.add(cabCard);
                    cabsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            if (cabsPanel.getComponentCount() == 1) { // Only header was added
                cabsPanel.add(new JLabel("No available drivers for " + selectedCabType));
            }
            
            cabsPanel.revalidate();
            cabsPanel.repaint();
        });
        
        bookRidePanel.add(cabsPanel);
        bookRidePanel.add(Box.createVerticalGlue());
        
        return new JScrollPane(bookRidePanel);
    }
    
    private void updateFare() {
        String cabType = (String) cabTypeCombo.getSelectedItem();
        double fare = 0;
        
        switch(cabType) {
            case "Sedan": fare = 45.00; break;
            case "SUV": fare = 65.00; break;
            case "Hatchback": fare = 35.00; break;
            case "Luxury": fare = 85.00; break;
        }
        
        fareLabel.setText(String.format("$%.2f", fare));
    }
    
    private JPanel createDriverCard(GoEliteLoginSystem.Driver driver) {
        JPanel cabCard = new JPanel();
        cabCard.setLayout(new BoxLayout(cabCard, BoxLayout.Y_AXIS));
        cabCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cabCard.setMaximumSize(new Dimension(500, 120));
        cabCard.setBackground(new Color(248, 248, 248));
        
        JLabel driverName = new JLabel("Driver: " + driver.name);
        driverName.setFont(new Font("Arial", Font.BOLD, 16));
        driverName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2));
        detailsPanel.setBackground(new Color(248, 248, 248));
        detailsPanel.add(new JLabel("Vehicle Type:"));
        detailsPanel.add(new JLabel(driver.vehicleType));
        detailsPanel.add(new JLabel("Experience:"));
        detailsPanel.add(new JLabel(driver.experience + " years"));
        detailsPanel.add(new JLabel("License:"));
        detailsPanel.add(new JLabel(driver.licenseNumber));
        
        JButton bookButton = new JButton("Book Now");
        bookButton.setBackground(new Color(46, 204, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bookButton.addActionListener(ev -> {
            String cabType = (String) cabTypeCombo.getSelectedItem();
            double fare = 0;
            
            switch(cabType) {
                case "Sedan": fare = 45.00; break;
                case "SUV": fare = 65.00; break;
                case "Hatchback": fare = 35.00; break;
                case "Luxury": fare = 85.00; break;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Confirm booking with " + driver.name + "?\n" +
                    "Pickup: " + pickupField.getText() + "\n" +
                    "Destination: " + destinationField.getText() + "\n" +
                    "Fare: $" + String.format("%.2f", fare),
                    "Confirm Booking", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Create a booking
                String bookingId = "BK" + System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateTime = sdf.format(new Date());
                
                GoEliteLoginSystem.Booking booking = new GoEliteLoginSystem.Booking(
                    bookingId, 
                    username, 
                    driver.username, 
                    pickupField.getText(), 
                    destinationField.getText(), 
                    dateTime, 
                    "Pending", 
                    String.format("%.2f", fare)
                );
                
                GoEliteLoginSystem.bookings.put(bookingId, booking);
                
                JOptionPane.showMessageDialog(this, 
                    "Booking confirmed!\nDriver: " + driver.name + 
                    "\nPickup: " + pickupField.getText() + 
                    "\nDestination: " + destinationField.getText() + 
                    "\nFare: $" + String.format("%.2f", fare) + 
                    "\nStatus: Pending driver confirmation",
                    "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        cabCard.add(Box.createRigidArea(new Dimension(0, 5)));
        cabCard.add(driverName);
        cabCard.add(Box.createRigidArea(new Dimension(0, 5)));
        cabCard.add(detailsPanel);
        cabCard.add(Box.createRigidArea(new Dimension(0, 5)));
        cabCard.add(bookButton);
        cabCard.add(Box.createRigidArea(new Dimension(0, 5)));
        
        return cabCard;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 2));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        footerPanel.setBackground(new Color(52, 73, 94));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(52, 73, 94));
        
        JLabel privacyTitle = new JLabel("Privacy Policy");
        privacyTitle.setFont(new Font("Arial", Font.BOLD, 14));
        privacyTitle.setForeground(Color.WHITE);
        
        JLabel privacyText = new JLabel("Terms and Conditions | Privacy Policy | FAQ");
        privacyText.setFont(new Font("Arial", Font.PLAIN, 12));
        privacyText.setForeground(Color.WHITE);
        
        leftPanel.add(privacyTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(privacyText);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(52, 73, 94));
        
        JLabel contactTitle = new JLabel("Get in Touch");
        contactTitle.setFont(new Font("Arial", Font.BOLD, 14));
        contactTitle.setForeground(Color.WHITE);
        
        JLabel contactText = new JLabel("Phone: 1-800-GO-ELITE | Email: support@goelite.com");
        contactText.setFont(new Font("Arial", Font.PLAIN, 12));
        contactText.setForeground(Color.WHITE);
        
        rightPanel.add(contactTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(contactText);
        
        footerPanel.add(leftPanel);
        footerPanel.add(rightPanel);
        
        return footerPanel;
    }
    
    private ImageIcon loadImage(String filename, int width, int height) {
        try {
            InputStream is = getClass().getResourceAsStream(filename);
            if (is == null) {
                System.err.println("Image not found: " + filename);
                return createPlaceholderImage(filename, width, height);
            }
            
            BufferedImage originalImage = ImageIO.read(is);
            if (originalImage == null) {
                System.err.println("Could not read image: " + filename);
                return createPlaceholderImage(filename, width, height);
            }
            
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.err.println("Error loading image " + filename + ": " + e.getMessage());
            return createPlaceholderImage(filename, width, height);
        }
    }
    
    private ImageIcon createPlaceholderImage(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width-1, height-1);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();
        return new ImageIcon(image);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GoEliteDashboard("user1"));
    }
}