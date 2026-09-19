package hotel.ui;

import hotel.dao.ReservationDAO;
import hotel.dao.RoomDAO;
import hotel.models.Customer;
import hotel.models.Reservation;
import hotel.models.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ReservationViewForm extends JFrame {
    private Customer customer;
    private JTable table;
    private DefaultTableModel model;

    public ReservationViewForm(Customer c) {
        this.customer = c;
        setTitle("My Reservations");
        setSize(900, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set main frame background to Deep Blue
        getContentPane().setBackground(new Color(20, 30, 65));
        
        // Title Label
        JLabel title = new JLabel("Your Bookings, " + c.getFirstName());
        title.setFont(new Font("Georgia", Font.BOLD, 24));
        title.setForeground(new Color(235, 200, 80));
        title.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        
        model = new DefaultTableModel(new Object[]{"ID", "Room", "Check-in", "Check-out", "Days", "Total Payment", "Paid"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class; // ID
                    case 1: return Integer.class; // Room
                    case 2: return Date.class;    // Check-in
                    case 3: return Date.class;    // Check-out
                    case 4: return Integer.class; // Days
                    case 5: return Double.class;  // Total Payment
                    case 6: return String.class;  // Paid
                    default: return Object.class;
                }
            }
        };
        table = new JTable(model);
        // Style Table for Dark Theme
        table.getTableHeader().setBackground(new Color(102, 72, 40)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(new Color(50, 65, 110)); 
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(235, 200, 80));
        table.setSelectionForeground(Color.BLACK);
        
        loadReservations();

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCancelBooking = new JButton("Cancel Booking");
        JButton btnBack = new JButton("Back");
        
        // Style action buttons
        for (JButton btn : new JButton[]{btnCancelBooking, btnBack}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        btnCancelBooking.addActionListener(e -> cancelSelectedBooking());
        btnBack.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(35, 50, 90));
        bottomPanel.add(btnCancelBooking);
        bottomPanel.add(btnBack);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadReservations() {
        model.setRowCount(0);
        List<Reservation> reservations = new ReservationDAO().getReservationsByCustomer(customer.getId());
        RoomDAO roomDAO = new RoomDAO();
        for (Reservation r : reservations) {
            Room room = roomDAO.getRoomByNumber(r.getRoomNumber());
            int days = (int) ((r.getCheckoutDate().getTime() - r.getCheckinDate().getTime()) / (1000 * 60 * 60 * 24));
            double totalPayment = (room != null) ? days * room.getPrice() : 0.0;
            model.addRow(new Object[]{
                r.getReservationId(),
                r.getRoomNumber(),
                r.getCheckinDate(),
                r.getCheckoutDate(),
                days,
                totalPayment,
                r.isPaid() ? "Paid" : "Unpaid"
            });
        }
    }

    private void cancelSelectedBooking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a booking to cancel.");
            return;
        }
        int resId = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = new ReservationDAO().cancelReservation(resId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking cancelled.");
                loadReservations();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking.");
            }
        }
    }
}