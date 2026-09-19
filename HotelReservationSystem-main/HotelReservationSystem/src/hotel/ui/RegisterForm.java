package hotel.ui;

import hotel.dao.CustomerDAO;
import hotel.models.Customer;
import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {
    JTextField txtFirstName, txtLastName, txtEmail;
    JButton btnRegister, btnBack;

    public RegisterForm() {
        setTitle("Register");
        setSize(900, 540); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); 
        setLocationRelativeTo(null);

        // Gradient Background Panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Deep Blue to Rich Brown Gradient
                Color color1 = new Color(20, 30, 65);
                Color color2 = new Color(102, 72, 40);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        // Use GridBagLayout on mainPanel to ensure the form is centered
        mainPanel.setLayout(new GridBagLayout());
        
        // Inner panel to hold the registration form content
        JPanel formContentPanel = new JPanel(new GridBagLayout());
        formContentPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        
        // --- Components ---
        JLabel title = new JLabel("Customer Registration");
        title.setFont(new Font("Georgia", Font.BOLD, 24));
        title.setForeground(new Color(235, 200, 80)); // Gold
        
        JLabel lblFirst = new JLabel("First Name:");
        JLabel lblLast = new JLabel("Last Name:");
        JLabel lblEmail = new JLabel("Email:");

        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        txtEmail = new JTextField(15);
        
        // Style labels and text fields
        for (JLabel lbl : new JLabel[]{lblFirst, lblLast, lblEmail}) {
            lbl.setForeground(new Color(255, 230, 160)); // Light Gold
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }
        for (JTextField txt : new JTextField[]{txtFirstName, txtLastName, txtEmail}) {
             txt.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }

        btnRegister = new JButton("Register");
        btnBack = new JButton("← Back to Login");
        
        // Style action buttons (Gold/Brown scheme)
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegister.setBackground(new Color(235, 200, 80));
        btnRegister.setForeground(new Color(60, 40, 20));
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Style the "Back to Login" button as a subtle, professional link
        btnBack.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnBack.setForeground(new Color(255, 230, 160)); // Light Gold Text
        btnBack.setBackground(new Color(0, 0, 0, 0)); // Transparent Background
        btnBack.setBorderPainted(false); // No Border
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false); // Ensure full transparency
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));


        // --- Layout ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; formContentPanel.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; formContentPanel.add(lblFirst, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formContentPanel.add(txtFirstName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formContentPanel.add(lblLast, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formContentPanel.add(txtLastName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; formContentPanel.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formContentPanel.add(txtEmail, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; formContentPanel.add(btnRegister, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; formContentPanel.add(btnBack, gbc);

        // Add the content panel to the main panel, which centers it
        mainPanel.add(formContentPanel);


        // --- Actions ---
        btnRegister.addActionListener(e -> register());
        btnBack.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void register() {
        String f = txtFirstName.getText().trim();
        String l = txtLastName.getText().trim();
        String e1 = txtEmail.getText().trim();
        if (f.isEmpty() || l.isEmpty() || e1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (!e1.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }
        Customer c = new Customer(0, f, l, e1);
        new CustomerDAO().addCustomer(c);
        JOptionPane.showMessageDialog(this, "Registered successfully!");
        dispose();
        new LoginForm();
    }
}