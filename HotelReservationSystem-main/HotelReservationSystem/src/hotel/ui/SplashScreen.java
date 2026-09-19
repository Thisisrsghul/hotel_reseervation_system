package hotel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class SplashScreen extends JFrame {
    public SplashScreen() {
        setUndecorated(true);
        setSize(950, 540);
        setLocationRelativeTo(null);

        // Gradient background panel for a luxurious, deep color scheme
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 65);      // Deep blue/Indigo
                Color color2 = new Color(102, 72, 40);     // Rich brown/Bronze
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(true);
        panel.setLayout(new BorderLayout());

        // Top bar with custom close button for a modern, borderless look
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(900, 40));

        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeButton.setForeground(new Color(255, 255, 255)); // Brown
        closeButton.setBackground(new Color(255, 255, 255, 0)); // Transparent
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(new Color(235, 100, 80)); // Highlight on hover
            }
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(new Color(120, 60, 40));
            }
        });
        closeButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        // Right-align the X button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeButton);
        topBar.add(rightPanel, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Center content panel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(40));

        // Load and center the hotel.jpeg image
        ImageIcon logoIcon = null;
        URL iconUrl = getClass().getResource("/hotel/ui/hotel.jpeg");
        if (iconUrl != null) {
            logoIcon = new ImageIcon(iconUrl);
        }
        JLabel logo = logoIcon != null ? new JLabel(logoIcon) : new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(logo);
        centerPanel.add(Box.createVerticalStrut(26));

        // Title with elegant gold color and bold font
        JLabel title = new JLabel("Etihad Elite Rooms", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 32));
        title.setForeground(new Color(235, 200, 80)); // Elegant gold
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));
        centerPanel.add(title);

        // Subtitle with italic font for a touch of class
        JLabel subtitle = new JLabel("Luxury stays, unforgettable experiences", SwingConstants.CENTER);
        subtitle.setFont(new Font("Serif", Font.ITALIC, 18));
        subtitle.setForeground(new Color(255, 230, 160));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(subtitle);

        // Multi-line description for marketing copy
        JLabel description = new JLabel("<html><div style='text-align: center;'>"
            + "Relax in spacious rooms, enjoy gourmet dining, and unwind in our world-class spa.<br>"
            + "Every stay is designed for your comfort, privacy, and memorable moments.<br>"
            + "Welcome to a new level of hospitality—make yourself at home."
            + "</div></html>", SwingConstants.CENTER);
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(Color.WHITE);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setBorder(BorderFactory.createEmptyBorder(8, 0, 18, 0));
        centerPanel.add(description);

        centerPanel.add(Box.createVerticalStrut(16));

        // Main action button - "Explore" - with custom color and hover effects
        JButton exploreButton = new JButton("Explore");
        exploreButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        exploreButton.setBackground(new Color(235, 200, 80));
        exploreButton.setForeground(new Color(60, 40, 20));
        exploreButton.setFocusPainted(false);
        exploreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exploreButton.setMaximumSize(new Dimension(180, 44));
        exploreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exploreButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(235, 200, 80), 2, true),
            BorderFactory.createEmptyBorder(8, 24, 8, 24)
        ));
        exploreButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exploreButton.setBackground(new Color(255, 220, 120));
                exploreButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 220, 120), 2, true),
                    BorderFactory.createEmptyBorder(8, 24, 8, 24)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exploreButton.setBackground(new Color(235, 200, 80));
                exploreButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(235, 200, 80), 2, true),
                    BorderFactory.createEmptyBorder(8, 24, 8, 24)
                ));
            }
        });
        exploreButton.addActionListener(e -> {
            setVisible(false);
            dispose();
            // *** UPDATED: Transition to the new ExplorePage ***
            SwingUtilities.invokeLater(ExplorePage::new);
        });
        centerPanel.add(exploreButton);

        panel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
        });
    }
}