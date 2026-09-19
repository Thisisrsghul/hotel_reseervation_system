package hotel.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ExplorePage extends JFrame {
    public ExplorePage() {
        // --- FIX: Set a consistent Look and Feel (L&F) for reliable styling ---
        try {
            // Using the Nimbus L&F as it handles UIManager properties better
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fallback to the default system L&F if Nimbus fails
            // System.out.println("Could not set Nimbus L&F.");
        }
        
        setTitle("Explore Etihad Elite Rooms");
        setSize(900, 540);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // --- Customizing JTabbedPane Look using UIManager (Robust Styling) ---
        // Setting content area and general background
        UIManager.put("TabbedPane.contentAreaColor", new Color(35, 50, 90));
        UIManager.put("TabbedPane.background", new Color(20, 30, 65)); 
        
        // --- Setting Tab Foreground (Text) Colors ---
        UIManager.put("TabbedPane.foreground", new Color(255, 230, 160)); // UNSELECTED text: Bright Gold
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);      // SELECTED text: White

        // --- Setting Tab Background Colors (The problem area) ---
        // We set these explicitly and rely on the Nimbus L&F to respect them.
        UIManager.put("TabbedPane.selectedBackground", new Color(102, 72, 40)); // SELECTED background: Rich Brown/Bronze
        UIManager.put("TabbedPane.unselectedBackground", new Color(20, 30, 65)); // UNSELECTED background: Deep Blue
        
        UIManager.put("TabbedPane.font", new Font("SansSerif", Font.BOLD, 16));

        // --- Main Content Panel with Gradient Background ---
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 65);
                Color color2 = new Color(102, 72, 40);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Standard JTabbedPane Creation ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Georgia", Font.BOLD, 18));
        tabbedPane.setBackground(new Color(20, 30, 65));

        // --- Tabs ---
        tabbedPane.addTab("Fine Dining", createFeaturePanel(
            "Gourmet Delights at The Golden Spoon",
            "Experience world-class cuisine at 'The Golden Spoon'. Our menu features locally sourced ingredients and international flavors prepared by Michelin-star chefs. Private dining available.",
            "dining.png"
        ));
        tabbedPane.addTab("Luxury Spa & Wellness", createFeaturePanel(
            "Etihad Serenity Oasis",
            "Unwind at the 'Etihad Serenity Spa'. We offer a full range of services including Swedish massage, hot stone therapy, and hydrotherapy pools. Book your escape today.",
            "spa.jpg"
        ));
        tabbedPane.addTab("Our Rooms & Suites", createFeaturePanel(
            "Unmatched Comfort and Elite Service",
            "Every room offers panoramic city views, plush king-sized beds, marble bathrooms, and smart home technology. Our Elite Suites include dedicated butler service.",
            "rooms.jpg"
        ));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // --- Footer with Continue Button ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        
        JButton btnContinue = new JButton("Continue to Login →");
        btnContinue.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnContinue.setBackground(new Color(235, 200, 80));
        btnContinue.setForeground(new Color(60, 40, 20));
        btnContinue.setFocusPainted(false);
        btnContinue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinue.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        btnContinue.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(LoginForm::new);
        });
        
        footer.add(btnContinue);
        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private JPanel createFeaturePanel(String titleText, String descriptionText, String imagePath) {
        
        // Use BorderLayout for the text and image panels to get better horizontal spacing
        JPanel outerPanel = new JPanel(new BorderLayout(30, 0)); // Increased horizontal gap to 30
        outerPanel.setBackground(new Color(35, 50, 90));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 

        // --- 1. Image Placeholder/Loader (WEST) ---
        JLabel imageLabel = new JLabel();
        final int IMG_SIZE = 300;
        imageLabel.setPreferredSize(new Dimension(IMG_SIZE, IMG_SIZE));
        imageLabel.setMinimumSize(new Dimension(IMG_SIZE, IMG_SIZE));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(235, 200, 80), 2));
        
        // Image Loading Logic (Standardized size)
        try {
            URL imgURL = getClass().getResource("/hotel/ui/" + imagePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image image = icon.getImage().getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText(""); 
            } else {
                imageLabel.setText(imagePath + " (Placeholder)");
            }
        } catch (Exception e) {
            imageLabel.setText("Error loading: " + imagePath);
        }
        imageLabel.setForeground(new Color(255, 230, 160)); 
        
        outerPanel.add(imageLabel, BorderLayout.WEST);

        // --- 2. Text Content (CENTER) ---
        // Use BorderLayout for the text panel to cleanly place the title (NORTH) and description (CENTER)
        JPanel textContainer = new JPanel(new BorderLayout(0, 10)); // Gap between title and description
        textContainer.setBackground(new Color(35, 50, 90));
        textContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5)); 

        // Title (NORTH)
        JLabel title = new JLabel(titleText, SwingConstants.LEFT);
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(new Color(235, 200, 80)); 
        title.setMinimumSize(new Dimension(100, 35)); 
        title.setPreferredSize(new Dimension(100, 35));
        
        // Description (CENTER)
        JTextArea description = new JTextArea(descriptionText);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description.setForeground(Color.WHITE);
        description.setBackground(new Color(35, 50, 90));
        description.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        textContainer.add(title, BorderLayout.NORTH);
        textContainer.add(description, BorderLayout.CENTER);
        
        outerPanel.add(textContainer, BorderLayout.CENTER);
        
        return outerPanel;
    }
}