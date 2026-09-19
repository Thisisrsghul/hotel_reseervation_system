package hotel.ui;

import hotel.dao.RoomDAO;
import hotel.models.Customer;
import hotel.models.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoomSearchForm extends JFrame {
    private Customer customer;
    private JTable roomTable;
    private JSpinner checkinSpinner, checkoutSpinner;
    private JButton btnBook;

    public RoomSearchForm(Customer c) {
        this.customer = c;

        setTitle("Search & Book Rooms");
        setSize(900, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 30, 65));
        setLayout(new BorderLayout());

        // --- Date Panel ---
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        datePanel.setBackground(new Color(35, 50, 90));

        JLabel lblIn = new JLabel("Check-in:");
        JLabel lblOut = new JLabel("Check-out:");
        lblIn.setForeground(new Color(255, 230, 160));
        lblOut.setForeground(new Color(255, 230, 160));
        datePanel.add(lblIn);

        // --- Check-in Spinner ---
        java.util.Date today = new java.util.Date();
        SpinnerDateModel checkinModel = new SpinnerDateModel(today, today, null, Calendar.DAY_OF_MONTH);
        checkinSpinner = new JSpinner(checkinModel);
        JSpinner.DateEditor checkinEditor = new JSpinner.DateEditor(checkinSpinner, "yyyy-MM-dd");
        
        // CORRECTION: Removed checkinEditor.getTextField().setEditable(false);
        // This allows the user to change the date using the spinner controls.
        
        checkinSpinner.setEditor(checkinEditor);
        checkinSpinner.setPreferredSize(new Dimension(150, 30));
        datePanel.add(checkinSpinner);

        datePanel.add(lblOut);

        // --- Check-out Spinner ---
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        java.util.Date tomorrow = cal.getTime();
        SpinnerDateModel checkoutModel = new SpinnerDateModel(tomorrow, tomorrow, null, Calendar.DAY_OF_MONTH);
        checkoutSpinner = new JSpinner(checkoutModel);
        JSpinner.DateEditor checkoutEditor = new JSpinner.DateEditor(checkoutSpinner, "yyyy-MM-dd");
        
        // CORRECTION: Removed checkoutEditor.getTextField().setEditable(false);
        // This allows the user to change the date using the spinner controls.
        
        checkoutSpinner.setEditor(checkoutEditor);
        checkoutSpinner.setPreferredSize(new Dimension(150, 30));
        datePanel.add(checkoutSpinner);

        // Ensure checkout ≥ checkin + 1 day
        checkinSpinner.addChangeListener(e -> {
            java.util.Date checkinDate = (java.util.Date) checkinSpinner.getValue();
            Calendar cMin = Calendar.getInstance();
            cMin.setTime(checkinDate);
            cMin.add(Calendar.DAY_OF_MONTH, 1);
            java.util.Date minCheckout = cMin.getTime();

            SpinnerDateModel checkoutModel1 = (SpinnerDateModel) checkoutSpinner.getModel();
            checkoutModel1.setStart(minCheckout);

            if (((java.util.Date) checkoutSpinner.getValue()).before(minCheckout)) {
                checkoutSpinner.setValue(minCheckout);
            }
        });

        // --- Search Button ---
        JButton btnSearch = new JButton("Show Available Rooms");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(235, 200, 80));
        btnSearch.setForeground(new Color(60, 40, 20));
        btnSearch.setFocusPainted(false);
        datePanel.add(btnSearch);

        add(datePanel, BorderLayout.NORTH);

        // --- Room Table ---
        roomTable = new JTable();
        roomTable.getTableHeader().setBackground(new Color(102, 72, 40));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        roomTable.setBackground(new Color(50, 65, 110));
        roomTable.setForeground(Color.WHITE);
        roomTable.setSelectionBackground(new Color(235, 200, 80));
        roomTable.setSelectionForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Buttons Panel ---
        btnBook = new JButton("Book Selected Room");
        btnBook.setEnabled(false);
        JButton btnBack = new JButton("Back");
        JButton btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(35, 50, 90));

        for (JButton btn : new JButton[]{btnBook, btnBack, btnCancel}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(new Color(235, 200, 80));
            btn.setForeground(new Color(60, 40, 20));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        buttonPanel.add(btnBook);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnSearch.addActionListener(e -> loadAvailableRooms());
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            btnBook.setEnabled(roomTable.getSelectedRow() != -1);
        });
        btnBook.addActionListener(e -> bookSelectedRoom());
        btnBack.addActionListener(e -> dispose());
        btnCancel.addActionListener(e -> {
            dispose();
            new CustomerDashboard(customer);
        });

        setVisible(true);
    }

    private void loadAvailableRooms() {
        java.util.Date checkinDate = (java.util.Date) checkinSpinner.getValue();
        java.util.Date checkoutDate = (java.util.Date) checkoutSpinner.getValue();

        if (!checkoutDate.after(checkinDate)) {
            JOptionPane.showMessageDialog(this, "Check-out must be after check-in!", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long diff = checkoutDate.getTime() - checkinDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (days < 1) {
            JOptionPane.showMessageDialog(this, "Minimum booking is one night.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Room> rooms = new RoomDAO().getAvailableRooms(
                new Date(checkinDate.getTime()),
                new Date(checkoutDate.getTime())
        );

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Room #", "Type", "Price", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Double.class : Object.class;
            }
        };

        for (Room r : rooms) {
            model.addRow(new Object[]{r.getRoomNumber(), r.getRoomType(), r.getPrice(), "YES"});
        }

        if (rooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No rooms available for selected dates.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }

        roomTable.setModel(model);
    }

    private void bookSelectedRoom() {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to book.");
            return;
        }
        int roomNum = (int) roomTable.getValueAt(row, 0);
        java.util.Date checkinDate = (java.util.Date) checkinSpinner.getValue();
        java.util.Date checkoutDate = (java.util.Date) checkoutSpinner.getValue();

        new RoomBookingForm(customer, roomNum, new Date(checkinDate.getTime()), new Date(checkoutDate.getTime()));
        dispose();
    }
}