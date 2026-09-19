package hotel.dao;

import hotel.DBConnection;
import hotel.models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room r) {
        String sql = "INSERT INTO Rooms (room_number, room_type, price_per_night) VALUES (?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getRoomNumber());
            stmt.setString(2, r.getRoomType());
            stmt.setDouble(3, r.getPrice());
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM Rooms";
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                list.add(new Room(rs.getInt("room_number"),
                                  rs.getString("room_type"),
                                  rs.getDouble("price_per_night")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Room> getAvailableRooms(Date checkin, Date checkout) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms r WHERE r.room_number NOT IN "
                   + "(SELECT room_number FROM Reservations "
                   + "WHERE NOT (checkout_date <= ? OR checkin_date >= ?))";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                rooms.add(new Room(rs.getInt("room_number"),
                                   rs.getString("room_type"),
                                   rs.getDouble("price_per_night")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM Rooms WHERE room_number = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Room(rs.getInt("room_number"),
                                rs.getString("room_type"),
                                rs.getDouble("price_per_night"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRoom(Room r) {
        String sql = "UPDATE Rooms SET room_type = ?, price_per_night = ? WHERE room_number = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getRoomType());
            ps.setDouble(2, r.getPrice());
            ps.setInt(3, r.getRoomNumber());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(int roomNumber) {
        String sql = "DELETE FROM Rooms WHERE room_number = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}