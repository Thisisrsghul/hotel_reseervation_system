package hotel.models;

import java.sql.Date;

public class Reservation {
    private int reservationId;
    private int customerId;
    private int roomNumber;
    private Date checkinDate, checkoutDate;
    private boolean paid;

    public Reservation(int reservationId, int customerId, int roomNumber, Date checkinDate, Date checkoutDate, boolean paid) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.roomNumber = roomNumber;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.paid = paid;
    }

    public int getReservationId() { return reservationId; }
    public int getCustomerId() { return customerId; }
    public int getRoomNumber() { return roomNumber; }
    public Date getCheckinDate() { return checkinDate; }
    public Date getCheckoutDate() { return checkoutDate; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
}