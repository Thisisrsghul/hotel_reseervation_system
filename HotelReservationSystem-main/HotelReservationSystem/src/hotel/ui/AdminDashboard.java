package hotel.ui;

import hotel.dao.CustomerDAO;
import hotel.dao.RoomDAO;
import hotel.dao.ReservationDAO;
import hotel.models.Customer;
import hotel.models.Room;
import hotel.models.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
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
        gbc.insets = new Insets(20,20,20,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Administrator Control Panel");
        title.setFont(new Font("Georgia", Font.BOLD, 30));
        title.setForeground(new Color(235, 200, 80)); // Elegant gold
        
        // Add title to the panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(title, gbc);
        gbc.gridwidth = 1;

        JButton btnViewCustomers = new JButton("View/Edit Customers");
        JButton btnViewRooms = new JButton("View/Edit Rooms");
        JButton btnViewReservations = new JButton("View/Edit Reservations");
        JButton btnAddRoom = new JButton("Add New Room");
        JButton btnLogout = new JButton("Logout"); 

        // Style all buttons for luxury theme
        for (JButton btn : new JButton[]{btnViewCustomers, btnViewRooms, btnViewReservations, btnAddRoom, btnLogout}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(250, 50));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // Layout
        gbc.gridx = 0; gbc.gridy = 1; panel.add(btnViewCustomers, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(btnViewRooms, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(btnViewReservations, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(btnAddRoom, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; 
        gbc.insets = new Insets(40,20,20,20); // More spacing for logout
        panel.add(btnLogout, gbc); 

        add(panel);

        btnViewCustomers.addActionListener(e -> showCustomers());
        btnViewRooms.addActionListener(e -> showRooms());
        btnViewReservations.addActionListener(e -> showReservations());
        btnAddRoom.addActionListener(e -> showAddRoomForm());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setVisible(true);
    }

    private void styleDialog(JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.getContentPane().setBackground(new Color(35, 50, 90)); 
    }
    
    private void styleTable(JTable table) {
        table.getTableHeader().setBackground(new Color(102, 72, 40)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(new Color(50, 65, 110)); 
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(235, 200, 80));
        table.setSelectionForeground(Color.BLACK);
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(new Color(235, 200, 80));
        btn.setForeground(new Color(60, 40, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // View & Edit Customers
    private void showCustomers() {
        JDialog dialog = new JDialog(this, "Customers", true);
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(this);
        styleDialog(dialog, "View/Edit Customers");

        List<Customer> customers = new CustomerDAO().getAllCustomers();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Email"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        for (Customer c : customers) {
            model.addRow(new Object[]{c.getId(), c.getFirstName(), c.getLastName(), c.getEmail()});
        }
        JTable table = new JTable(model);
        styleTable(table);

        JButton btnEdit = createStyledButton("Edit Customer");
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a customer to edit.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            String firstName = (String) model.getValueAt(row, 1);
            String lastName = (String) model.getValueAt(row, 2);
            String email = (String) model.getValueAt(row, 3);

            JTextField txtFirstName = new JTextField(firstName, 12);
            JTextField txtLastName = new JTextField(lastName, 12);
            JTextField txtEmail = new JTextField(email, 12);
            
            // Styled Edit Panel
            JPanel editPanel = new JPanel(new GridLayout(3,2,8,8));
            editPanel.setBackground(new Color(35, 50, 90)); 
            JLabel lbl1 = new JLabel("First Name:"); lbl1.setForeground(Color.WHITE); editPanel.add(lbl1); editPanel.add(txtFirstName);
            JLabel lbl2 = new JLabel("Last Name:"); lbl2.setForeground(Color.WHITE); editPanel.add(lbl2); editPanel.add(txtLastName);
            JLabel lbl3 = new JLabel("Email:"); lbl3.setForeground(Color.WHITE); editPanel.add(lbl3); editPanel.add(txtEmail);

            int result = JOptionPane.showConfirmDialog(dialog, editPanel, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Customer updated = new Customer(id, txtFirstName.getText(), txtLastName.getText(), txtEmail.getText());
                new CustomerDAO().updateCustomer(updated);
                model.setValueAt(txtFirstName.getText(), row, 1);
                model.setValueAt(txtLastName.getText(), row, 2);
                model.setValueAt(txtEmail.getText(), row, 3);
                JOptionPane.showMessageDialog(dialog, "Customer updated.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(35, 50, 90));
        bottomPanel.add(btnEdit);

        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // View, Edit, Delete Rooms
    private void showRooms() {
        JDialog dialog = new JDialog(this, "Rooms", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        styleDialog(dialog, "View/Edit Rooms");

        List<Room> rooms = new RoomDAO().getAllRooms();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Room Number", "Type", "Price"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        for (Room r : rooms) {
            model.addRow(new Object[]{r.getRoomNumber(), r.getRoomType(), r.getPrice()});
        }
        JTable table = new JTable(model);
        styleTable(table);

        JButton btnEdit = createStyledButton("Edit Room");
        JButton btnDelete = createStyledButton("Delete Room");

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a room to edit.");
                return;
            }
            int roomNum = (int) model.getValueAt(row, 0);
            String type = (String) model.getValueAt(row, 1);
            double price = (double) model.getValueAt(row, 2);

            JTextField txtType = new JTextField(type, 12);
            JTextField txtPrice = new JTextField(String.valueOf(price), 12);

            JPanel editPanel = new JPanel(new GridLayout(2,2,8,8));
            editPanel.setBackground(new Color(35, 50, 90));
            JLabel lbl1 = new JLabel("Type:"); lbl1.setForeground(Color.WHITE); editPanel.add(lbl1); editPanel.add(txtType);
            JLabel lbl2 = new JLabel("Price:"); lbl2.setForeground(Color.WHITE); editPanel.add(lbl2); editPanel.add(txtPrice);

            int result = JOptionPane.showConfirmDialog(dialog, editPanel, "Edit Room", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newType = txtType.getText();
                    double newPrice = Double.parseDouble(txtPrice.getText());
                    Room updated = new Room(roomNum, newType, newPrice);
                    new RoomDAO().updateRoom(updated);
                    model.setValueAt(newType, row, 1);
                    model.setValueAt(newPrice, row, 2);
                    JOptionPane.showMessageDialog(dialog, "Room updated.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid price.");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a room to delete.");
                return;
            }
            int roomNum = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete room #" + roomNum + "?", "Delete Room", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new RoomDAO().deleteRoom(roomNum);
                model.removeRow(row);
                JOptionPane.showMessageDialog(dialog, "Room deleted.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(35, 50, 90));
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // View/Edit Reservations
    private void showReservations() {
        JDialog dialog = new JDialog(this, "Reservations", true);
        dialog.setSize(900, 450);
        dialog.setLocationRelativeTo(this);
        styleDialog(dialog, "View/Edit Reservations");

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Reservation ID", "Customer ID", "Room Number", "Room Type", "Room Price", "Check-in", "Check-out"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Load reservations with room details
        reloadReservations(model, table);

        JButton btnChangeRoom = createStyledButton("Change Room");
        JButton btnCancelReservation = createStyledButton("Cancel Reservation");
        JButton btnBack = createStyledButton("Back");
        
        // ... (Action Listeners remain the same)
        btnChangeRoom.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a reservation to change its room.");
                return;
            }
            int reservationId = (int) model.getValueAt(row, 0);
            int customerId = (int) model.getValueAt(row, 1);
            int oldRoomNumber = (int) model.getValueAt(row, 2);
            Date checkin = (Date) model.getValueAt(row, 5);
            Date checkout = (Date) model.getValueAt(row, 6);

            List<Room> availableRooms = new RoomDAO().getAvailableRooms(checkin, checkout);
            JComboBox<Integer> roomCombo = new JComboBox<>();
            for (Room r : availableRooms) {
                if (r.getRoomNumber() != oldRoomNumber) 
                    roomCombo.addItem(r.getRoomNumber());
            }

            if (roomCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "No other rooms available for these dates!");
                return;
            }

            JPanel panel = new JPanel(new GridLayout(1,2,8,8));
            panel.setBackground(new Color(35, 50, 90));
            JLabel lbl = new JLabel("Select new room number:"); lbl.setForeground(Color.WHITE);
            panel.add(lbl);
            panel.add(roomCombo);

            int result = JOptionPane.showConfirmDialog(dialog, panel, "Change Room", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int newRoomNumber = (Integer) roomCombo.getSelectedItem();
                boolean success = updateReservationRoom(reservationId, customerId, newRoomNumber, checkin, checkout);
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Room changed successfully.");
                    reloadReservations(model, table);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to change room.");
                }
            }
        });

        btnCancelReservation.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Select a reservation to cancel.");
                return;
            }
            int reservationId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to cancel this reservation?", "Cancel Reservation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = new ReservationDAO().cancelReservation(reservationId);
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Reservation cancelled.");
                    reloadReservations(model, table);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to cancel reservation.");
                }
            }
        });

        btnBack.addActionListener(e -> dialog.dispose());


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(35, 50, 90));
        bottomPanel.add(btnChangeRoom);
        bottomPanel.add(btnCancelReservation);
        bottomPanel.add(btnBack);

        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Helper to reload reservation table
    private void reloadReservations(DefaultTableModel model, JTable table) {
        model.setRowCount(0);
        List<Reservation> reservations = new ReservationDAO().getAllReservations();
        RoomDAO roomDAO = new RoomDAO();
        for (Reservation r : reservations) {
            Room room = roomDAO.getRoomByNumber(r.getRoomNumber());
            model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getCustomerId(),
                    r.getRoomNumber(),
                    room != null ? room.getRoomType() : "",
                    room != null ? room.getPrice() : 0.0,
                    r.getCheckinDate(),
                    r.getCheckoutDate()
            });
        }
    }

    // You need this method in ReservationDAO:
    // public boolean updateReservationRoom(int reservationId, int customerId, int roomNumber, Date checkin, Date checkout)
    private boolean updateReservationRoom(int reservationId, int customerId, int roomNumber, Date checkin, Date checkout) {
        String sql = "UPDATE Reservations SET room_number=? WHERE reservation_id=? AND customer_id=? AND checkin_date=? AND checkout_date=?";
        try (java.sql.Connection conn = hotel.DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            stmt.setInt(2, reservationId);
            stmt.setInt(3, customerId);
            stmt.setDate(4, checkin);
            stmt.setDate(5, checkout);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void showAddRoomForm() {
        JDialog dialog = new JDialog(this, "Add Room", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        styleDialog(dialog, "Add New Room");
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);

        JTextField txtRoomNum = new JTextField(10);
        JTextField txtRoomType = new JTextField(10);
        JTextField txtRoomPrice = new JTextField(10);
        JButton btnSave = createStyledButton("Add Room");
        
        JLabel lbl1 = new JLabel("Room Number:"); lbl1.setForeground(Color.WHITE);
        JLabel lbl2 = new JLabel("Room Type:"); lbl2.setForeground(Color.WHITE);
        JLabel lbl3 = new JLabel("Price per night:"); lbl3.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(lbl1, gbc);
        gbc.gridx = 1; gbc.gridy = 0; dialog.add(txtRoomNum, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(lbl2, gbc);
        gbc.gridx = 1; gbc.gridy = 1; dialog.add(txtRoomType, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(lbl3, gbc);
        gbc.gridx = 1; gbc.gridy = 2; dialog.add(txtRoomPrice, gbc);

        gbc.gridx = 1; gbc.gridy = 3; dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                int num = Integer.parseInt(txtRoomNum.getText().trim());
                String type = txtRoomType.getText().trim();
                double price = Double.parseDouble(txtRoomPrice.getText().trim());
                new RoomDAO().addRoom(new Room(num, type, price));
                JOptionPane.showMessageDialog(dialog, "Room added!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Check your input values!");
            }
        });

        dialog.setVisible(true);
    }
}