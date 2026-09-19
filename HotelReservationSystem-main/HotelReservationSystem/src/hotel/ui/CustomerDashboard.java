package hotel.ui;

import hotel.models.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {
    private Customer customer;
    private JButton btnSearchRooms, btnViewReservations, btnLogout;

    public CustomerDashboard(Customer c) {
        this.customer = c;
        setTitle("Customer Dashboard");
        setSize(900, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Gradient Background Panel
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Deep Blue to Rich Brown Gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 65), 0, getHeight(), new Color(102, 72, 40));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Welcome, " + c.getFirstName() + "!");
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(235, 200, 80)); // Gold
        
        // Add title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(title, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(25,15,15,15);

        btnSearchRooms = new JButton("Search & Book Rooms");
        btnViewReservations = new JButton("My Reservations");
        btnLogout = new JButton("Logout");

        // Style buttons
        for (JButton btn : new JButton[]{btnSearchRooms, btnViewReservations, btnLogout}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(220, 40));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // Layout
        gbc.gridx = 0; gbc.gridy = 1; panel.add(btnSearchRooms, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(btnViewReservations, gbc);
        
        gbc.insets = new Insets(30,15,15,15);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(btnLogout, gbc);

        btnSearchRooms.addActionListener(e -> new RoomSearchForm(customer));
        btnViewReservations.addActionListener(e -> new ReservationViewForm(customer));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        add(panel);
        setVisible(true);
    }
}