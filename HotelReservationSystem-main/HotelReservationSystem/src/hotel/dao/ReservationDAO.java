package hotel.dao;

import hotel.DBConnection;
import hotel.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Check if a room is available for the given dates
    public boolean isRoomAvailable(int roomNumber, Date checkin, Date checkout) {
        String sql = "SELECT * FROM Reservations WHERE room_number=? AND NOT (checkout_date <= ? OR checkin_date >= ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            stmt.setDate(2, checkin);
            stmt.setDate(3, checkout);
            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // true if no conflicting reservation found
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add reservation, "paid" is set to false at first (payment after booking)
    public boolean addReservation(int customerId, int roomNumber, Date checkin, Date checkout) {
        if (!isRoomAvailable(roomNumber, checkin, checkout)) {
            return false;
        }
        String sql = "INSERT INTO Reservations (customer_id, room_number, checkin_date, checkout_date, paid) VALUES (?,?,?,?,false)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomNumber);
            stmt.setDate(3, checkin);
            stmt.setDate(4, checkout);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all reservations for a customer
    public List<Reservation> getReservationsByCustomer(int customerId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Reservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_number"),
                    rs.getDate("checkin_date"),
                    rs.getDate("checkout_date"),
                    rs.getBoolean("paid")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get all reservations (admin view)
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservations";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Reservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_number"),
                    rs.getDate("checkin_date"),
                    rs.getDate("checkout_date"),
                    rs.getBoolean("paid")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cancel reservation
    public boolean cancelReservation(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Change the room for a reservation
    public boolean updateReservationRoom(int reservationId, int customerId, int roomNumber, Date checkin, Date checkout) {
        String sql = "UPDATE Reservations SET room_number=? WHERE reservation_id=? AND customer_id=? AND checkin_date=? AND checkout_date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            stmt.setInt(2, reservationId);
            stmt.setInt(3, customerId);
            stmt.setDate(4, checkin);
            stmt.setDate(5, checkout);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Mark reservation as paid
    public boolean markAsPaid(int reservationId) {
        String sql = "UPDATE Reservations SET paid=true WHERE reservation_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get latest reservation id for a customer with specific room and dates
    public int getLatestReservationId(int customerId, int roomNumber, Date checkin, Date checkout) {
        String sql = "SELECT reservation_id FROM Reservations WHERE customer_id=? AND room_number=? AND checkin_date=? AND checkout_date=? ORDER BY reservation_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, roomNumber);
            stmt.setDate(3, checkin);
            stmt.setDate(4, checkout);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("reservation_id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }
}