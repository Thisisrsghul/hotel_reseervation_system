package hotel.ui;

import hotel.dao.ReservationDAO;
import hotel.dao.RoomDAO;
import hotel.models.Customer;
import hotel.models.Room;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class PaymentPage extends JFrame {
    private Customer customer;
    private Room room;
    private Date checkin;
    private Date checkout;
    private int days;
    private double totalPayment;

    public PaymentPage(Customer customer, int roomNumber, Date checkin, Date checkout, int days, double totalPayment) {
        this.customer = customer;
        this.room = new RoomDAO().getRoomByNumber(roomNumber);
        this.checkin = checkin;
        this.checkout = checkout;
        this.days = days;
        this.totalPayment = totalPayment;

        setTitle("Payment Summary");
        setSize(900, 540); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(new Color(20, 30, 65));

        JPanel panel = new JPanel(new GridLayout(7,2,10,10));
        panel.setBackground(new Color(35, 50, 90));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Use the helper methods
        panel.add(lbl("Customer:")); panel.add(val(customer.getFirstName() + " " + customer.getLastName()));
        panel.add(lbl("Room Number:")); panel.add(val(String.valueOf(room.getRoomNumber()) + " (" + room.getRoomType() + ")"));
        panel.add(lbl("Check-in:")); panel.add(val(checkin.toString()));
        panel.add(lbl("Check-out:")); panel.add(val(checkout.toString()));
        panel.add(lbl("No. of Days:")); panel.add(val(String.valueOf(days)));
        
        JLabel lblTotal = lbl("TOTAL PAYMENT:");
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 16));
        JLabel valTotal = val(String.format(" %.2f", totalPayment));
        valTotal.setFont(new Font("Georgia", Font.BOLD, 16));
        valTotal.setForeground(new Color(235, 200, 80)); 
        
        panel.add(lblTotal); panel.add(valTotal);

        JButton btnPay = new JButton("Pay & Book");
        JButton btnBack = new JButton("Back");
        
        for (JButton btn : new JButton[]{btnPay, btnBack}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(new Color(35, 50, 90));
        buttonPanel.add(btnPay);
        buttonPanel.add(btnBack);

        // Use BorderLayout in a container panel to structure the summary and buttons
        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(false);
        contentContainer.add(panel, BorderLayout.CENTER);
        contentContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        // Use GridBagLayout on the JFrame to center the contentContainer in the large 900x540 frame
        setLayout(new GridBagLayout());
        add(contentContainer, new GridBagConstraints());


        btnPay.addActionListener(e -> {
            boolean paymentSuccess = processPayment();
            if (paymentSuccess) {
                ReservationDAO dao = new ReservationDAO();
                if (dao.isRoomAvailable(room.getRoomNumber(), checkin, checkout)) {
                    boolean booked = dao.addReservation(customer.getId(), room.getRoomNumber(), checkin, checkout);
                    if (booked) {
                        int reservationId = dao.getLatestReservationId(customer.getId(), room.getRoomNumber(), checkin, checkout);
                        if (reservationId > 0) {
                            dao.markAsPaid(reservationId);
                        }
                        JOptionPane.showMessageDialog(this, "Payment successful! Room booked.");
                        dispose();
                        new ReservationViewForm(customer); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Booking failed! Room may not be available.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Room not available anymore!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed! Room not booked.");
            }
        });

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Helper to create styled labels
    private JLabel lbl(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(255, 230, 160)); // Light Gold for labels
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    // Helper to create styled value labels
    private JLabel val(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    // Simulate payment process; replace with real payment gateway as needed
    private boolean processPayment() {
        return true;
    }
}