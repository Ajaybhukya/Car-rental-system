import java.sql.*;

public class CustomerDAO {
	public int addCustomer(String name, String phone) {
	    String sql = "INSERT INTO customers (name, phone) VALUES (?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, name);
	        stmt.setString(2, phone);
	        stmt.executeUpdate();
	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            return rs.getInt(1); // Return generated customer ID
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}

    public void removeCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            System.out.println("✅ Customer record removed after return!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
