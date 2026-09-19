
package hotel.dao;

import hotel.DBConnection;
import hotel.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void addCustomer(Customer c) {
        String sql = "INSERT INTO Customers (first_name, last_name, email) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getLastName());
            stmt.setString(3, c.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customers WHERE email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new Customer(rs.getInt("customer_id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                list.add(new Customer(rs.getInt("customer_id"),
                                      rs.getString("first_name"),
                                      rs.getString("last_name"),
                                      rs.getString("email")));
            }
        } catch(SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Update customer details
    public void updateCustomer(Customer c) {
        String sql = "UPDATE Customers SET first_name=?, last_name=?, email=? WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getLastName());
            stmt.setString(3, c.getEmail());
            stmt.setInt(4, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete customer by ID
    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customers WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}