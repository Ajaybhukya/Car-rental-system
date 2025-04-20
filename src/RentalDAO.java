import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    public int rentCar(int customerId, int carId) {
        String sql = "INSERT INTO rentals (customer_id, car_id, rent_date, total_cost) VALUES (?, ?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, carId);
            stmt.setDate(3, Date.valueOf(LocalDate.now())); // System Date
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Rental ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void returnCar(int rentalId) {
        String fetchSQL = "SELECT rentals.car_id, rentals.rent_date, cars.rent_per_day FROM rentals " +
                          "JOIN cars ON rentals.car_id = cars.car_id " +
                          "WHERE rentals.rental_id = ?";  // Explicitly specify table name

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL)) {
            
            fetchStmt.setInt(1, rentalId);
            ResultSet rs = fetchStmt.executeQuery();
            
            if (rs.next()) {
                int carId = rs.getInt("car_id");  // No ambiguity now
                LocalDate rentDate = rs.getDate("rent_date").toLocalDate();
                double rentPerDay = rs.getDouble("rent_per_day");

                LocalDate returnDate = LocalDate.now();
                long daysRented = ChronoUnit.DAYS.between(rentDate, returnDate);
                if (daysRented == 0) daysRented = 1; // Minimum 1-day charge

                double totalCost = daysRented * rentPerDay;

                // Update rental record with return date and total cost
                String updateSQL = "UPDATE rentals SET return_date = ?, total_cost = ? WHERE rental_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                    updateStmt.setDate(1, Date.valueOf(returnDate));
                    updateStmt.setDouble(2, totalCost);
                    updateStmt.setInt(3, rentalId);
                    updateStmt.executeUpdate();
                }

                // ✅ FIX: Create CarDAO instance and mark the car as available
                CarDAO carDAO = new CarDAO();
                carDAO.markCarAsAvailable(carId);

                System.out.println("✅ Car returned successfully! Total cost: ₹" + totalCost);
            } else {
                System.out.println("❌ Invalid Rental ID. No matching record found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<String[]> getCarRentCustomerInfo() {
        List<String[]> rentals = new ArrayList<>();

        String sql = "SELECT r.rental_id, r.car_id, c.model, c.brand, c.rent_per_day, cu.name, r.rent_date, r.return_date " +
                     "FROM rentals r " +
                     "JOIN cars c ON r.car_id = c.car_id " +
                     "JOIN customers cu ON r.customer_id = cu.customer_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String returnDate = (rs.getDate("return_date") == null) ? "Not Returned" : rs.getDate("return_date").toString();

                rentals.add(new String[]{
                        String.valueOf(rs.getInt("rental_id")),  // ✅ Fix: Corrected column name from rent_id to rental_id
                        String.valueOf(rs.getInt("car_id")),  
                        rs.getString("model"),                
                        rs.getString("brand"),                
                        String.valueOf(rs.getDouble("rent_per_day")), 
                        rs.getString("name"),                 
                        rs.getDate("rent_date").toString(),   
                        returnDate                            
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }
    public String[] getRentalDetailsById(int rentalId) {
        String sql = "SELECT r.rental_id, cu.name, c.model, c.brand, c.rent_per_day, r.rent_date, r.return_date " +
                     "FROM rentals r " +
                     "JOIN cars c ON r.car_id = c.car_id " +
                     "JOIN customers cu ON r.customer_id = cu.customer_id " +
                     "WHERE r.rental_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, rentalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String returnDate = (rs.getDate("return_date") == null) ? "Not Returned" : rs.getDate("return_date").toString();
                return new String[]{
                        String.valueOf(rs.getInt("rental_id")),  
                        rs.getString("name"),  
                        rs.getString("model"),  
                        rs.getString("brand"),  
                        String.valueOf(rs.getDouble("rent_per_day")),  
                        rs.getDate("rent_date").toString(),  
                        returnDate  
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // No record found
    }
}
