package hotel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class LoginForm extends JFrame {
    public LoginForm() {
        setUndecorated(true);
        setSize(900, 540); 
        setLocationRelativeTo(null);

        // Gradient background
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
        panel.setLayout(new BorderLayout());

        // Top bar with cross button (same as Splash Screen)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(900, 36));
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        closeButton.setForeground(new Color(120, 60, 40));
        closeButton.setBackground(new Color(0, 0, 0, 0));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> { setVisible(false); dispose(); });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeButton);
        topBar.add(rightPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Centered form
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(Box.createVerticalStrut(48));

        // Logo (Assuming hotel.jpeg is accessible via the class path)
        ImageIcon logoIcon = null;
        URL iconUrl = getClass().getResource("/hotel/ui/hotel.jpeg");
        if (iconUrl != null) logoIcon = new ImageIcon(iconUrl);
        JLabel logo = logoIcon != null ? new JLabel(logoIcon) : new JLabel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(logo);
        formPanel.add(Box.createVerticalStrut(16));

        JLabel title = new JLabel("Welcome! Please choose login type:");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(new Color(235, 200, 80)); // Elegant Gold
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));

        JButton btnCustomer = new JButton("Customer");
        JButton btnAdmin = new JButton("Admin");
        
        // Custom Button Styling (Gold/Brown theme)
        for (JButton btn : new JButton[]{btnCustomer, btnAdmin}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 22));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(220, 48));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 200, 80), 2, true),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
            ));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(255, 220, 120));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(235, 200, 80));
                }
            });
        }
        formPanel.add(btnCustomer);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(btnAdmin);

        btnCustomer.addActionListener(e -> showCustomerPanel());
        btnAdmin.addActionListener(e -> showAdminLoginDialog());

        panel.add(formPanel, BorderLayout.CENTER);
        setContentPane(panel);
        setVisible(true);
    }

    private void showCustomerPanel() {
        getContentPane().removeAll();
        // New panel with the same gradient background
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 65), 0, getHeight(), new Color(102, 72, 40));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.add(Box.createVerticalStrut(60));
        
        JLabel lblEmail = new JLabel("Enter your Email to Login:");
        lblEmail.setFont(new Font("Georgia", Font.BOLD, 26)); 
        lblEmail.setForeground(new Color(235, 200, 80)); // Elegant Gold
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblEmail);
        panel.add(Box.createVerticalStrut(15));


        JTextField txtEmail = new JTextField(28); 
        txtEmail.setMaximumSize(new Dimension(400, 44)); 
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 24)); 
        txtEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(txtEmail);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        
        // Custom Button Styling (Gold/Brown theme)
        for (JButton btn : new JButton[]{btnLogin, btnRegister}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 200, 80), 2, true),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
            ));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(255, 220, 120));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(235, 200, 80));
                }
            });
        }
        panel.add(Box.createVerticalStrut(25));
        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnRegister);

        // --- CORRECTED BACK BUTTON STYLING ---
        JButton btnBack = new JButton("← Back to Role Select");
        btnBack.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnBack.setForeground(new Color(255, 230, 160)); // Light Gold text
        btnBack.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        btnBack.setBorderPainted(false); 
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false); // Ensure full transparency
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBack.setForeground(Color.WHITE); // Highlight on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBack.setForeground(new Color(255, 230, 160));
            }
        });
        // --- END CORRECTION ---

        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        panel.add(Box.createVerticalStrut(40));
        panel.add(btnBack);
        
        // ... Action Listeners (unchanged)
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your email.");
                return;
            }
            hotel.dao.CustomerDAO dao = new hotel.dao.CustomerDAO();
            hotel.models.Customer c = dao.getCustomerByEmail(email);
            if (c != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + c.getFirstName() + "!");
                dispose();
                new CustomerDashboard(c);
            } else {
                JOptionPane.showMessageDialog(this, "No user found! Please Register.");
            }
        });

        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });

        setContentPane(panel);
        revalidate();
        repaint();
        setSize(900, 540); 
        setLocationRelativeTo(null);
    }

    private void showAdminLoginDialog() {
        // ... (showAdminLoginDialog method remains unchanged)
        JPanel adminPanel = new JPanel(new GridLayout(2,2,10,10));
        adminPanel.setOpaque(true);
        adminPanel.setBackground(new Color(35, 50, 90)); // Dark background
        
        JLabel lblId = new JLabel("Admin ID:");
        lblId.setForeground(new Color(255, 230, 160));
        lblId.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(new Color(255, 230, 160));
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 16));

        JTextField adminIdField = new JTextField();
        adminIdField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JPasswordField adminPassField = new JPasswordField();
        adminPassField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        adminPanel.add(lblId);
        adminPanel.add(adminIdField);
        adminPanel.add(lblPass);
        adminPanel.add(adminPassField);

        UIManager.put("OptionPane.background", new Color(35, 50, 90));
        UIManager.put("Panel.background", new Color(35, 50, 90));
        UIManager.put("OptionPane.messageForeground", new Color(0,0,0));
        
        int result = JOptionPane.showConfirmDialog(this, adminPanel, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);

        if (result == JOptionPane.OK_OPTION) {
            String adminId = adminIdField.getText().trim();
            String adminPass = new String(adminPassField.getPassword());

            if ("admin".equals(adminId) && "root".equals(adminPass)) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
                dispose();
                new AdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials!");
            }
        }
    }
}