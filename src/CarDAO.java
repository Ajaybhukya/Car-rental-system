import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    public List<String[]> getAvailableCarsWithDetails() {
        List<String[]> cars = new ArrayList<>();
        String sql = "SELECT car_id, brand, model, year, rent_per_day FROM cars WHERE available = TRUE";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                cars.add(new String[]{
                        String.valueOf(rs.getInt("car_id")),
                        rs.getString("brand"),
                        rs.getString("model"),
                        String.valueOf(rs.getInt("year")),
                        String.valueOf(rs.getDouble("rent_per_day"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public void addCar(String model, String brand, int year, double rentPerDay) {
        if (year >= 2026) {
            System.out.println("Error: The car year must be less than 2026.");
            return;  // Prevents insertion
        }

        String sql = "INSERT INTO cars (model, brand, year, rent_per_day, available) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, model);
            stmt.setString(2, brand);
            stmt.setInt(3, year);
            stmt.setDouble(4, rentPerDay);
            stmt.executeUpdate();
            
            System.out.println("Car added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getRentPerDay(int carId) {
        String sql = "SELECT rent_per_day FROM cars WHERE car_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("rent_per_day");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isCarAvailable(int carId) {
        String sql = "SELECT available FROM cars WHERE car_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void markCarAsUnavailable(int carId) {
        String sql = "UPDATE cars SET available = FALSE WHERE car_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, carId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markCarAsAvailable(int carId) {
        String sql = "UPDATE cars SET available = TRUE WHERE car_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, carId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
