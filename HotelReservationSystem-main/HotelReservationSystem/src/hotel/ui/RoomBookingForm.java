package hotel.ui;

import hotel.dao.RoomDAO;
import hotel.models.Customer;
import hotel.models.Room;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class RoomBookingForm extends JFrame {
    public RoomBookingForm(Customer c, int roomNumber, Date checkin, Date checkout) {
        setTitle("Book Room");
        setSize(900, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set main frame background to Deep Blue
        getContentPane().setBackground(new Color(20, 30, 65));

        // Use GridBagLayout to center the flow panel in the larger frame
        setLayout(new GridBagLayout()); 
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(20, 30, 65));

        JButton btnProceedPayment = new JButton("Proceed to Payment");
        JButton btnBack = new JButton("Back");

        // Style action buttons
        for (JButton btn : new JButton[]{btnProceedPayment, btnBack}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        panel.add(btnProceedPayment);
        panel.add(btnBack);
        
        // Add the flow panel to the center of the GridBagLayout
        add(panel, new GridBagConstraints());

        btnProceedPayment.addActionListener(e -> {
            Room room = new RoomDAO().getRoomByNumber(roomNumber);
            int days = (int) ((checkout.getTime() - checkin.getTime()) / (1000 * 60 * 60 * 24));
            double totalPayment = days * room.getPrice();
            new PaymentPage(c, roomNumber, checkin, checkout, days, totalPayment);
            dispose();
        });

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }
}