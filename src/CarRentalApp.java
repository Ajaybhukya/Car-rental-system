import java.util.List;
import java.util.Scanner;

public class CarRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarDAO carDAO = new CarDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        RentalDAO rentalDAO = new RentalDAO();

        while (true) {
            System.out.println("\n--- Car Rental System ---");
            System.out.println("1. Add Car");
            System.out.println("2. View Available Cars");
            System.out.println("3. Rent a Car");
            System.out.println("4. Return a Car");
            System.out.println("5. View Car Rent and Customer Information");
            System.out.println("6. Search Rental by ID");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Car Model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter Brand: ");
                    String brand = scanner.nextLine();
                    System.out.print("Enter Year: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter Rent Per Day: ");
                    double rentPerDay = scanner.nextDouble();
                    carDAO.addCar(model, brand, year, rentPerDay);
                    break;

                case 2:
                    System.out.println("\nAvailable Cars:");
                    List<String[]> availableCars = carDAO.getAvailableCarsWithDetails();
                    if (availableCars.isEmpty()) {
                        System.out.println("No cars available.");
                    } else {
                        // Print table header
                        System.out.printf("%-8s %-15s %-15s %-6s %-10s\n", "Car ID", "Brand", "Model", "Year", "Rent/Day");
                        System.out.println("------------------------------------------------------");

                        // Print car details in tabular format
                        for (String[] car : availableCars) {
                            System.out.printf("%-8s %-15s %-15s %-6s ₹%-10s\n", car[0], car[1], car[2], car[3], car[4]);
                        }
                    }
                    break;

                case 3:
                    System.out.println("\nAvailable Cars:");
                    List<String[]> cars = carDAO.getAvailableCarsWithDetails();
                    if (cars.isEmpty()) {
                        System.out.println("No cars available for rent.");
                        break;
                    }

                    // Print available cars
                    System.out.printf("%-8s %-15s %-15s %-6s %-10s\n", "Car ID", "Brand", "Model", "Year", "Rent/Day");
                    System.out.println("------------------------------------------------------");
                    for (String[] car : cars) {
                        System.out.printf("%-8s %-15s %-15s %-6s ₹%-10s\n", car[0], car[1], car[2], car[3], car[4]);
                    }

                    System.out.print("\nEnter Customer Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    int customerId = customerDAO.addCustomer(name, phone);  // No email needed

                    System.out.print("Enter Car ID to Rent: ");
                    int carId = scanner.nextInt();

                    if (carDAO.isCarAvailable(carId)) {
                        int rentalId = rentalDAO.rentCar(customerId, carId);
                        carDAO.markCarAsUnavailable(carId);
                        System.out.println("Car rented successfully. Rental ID: " + rentalId);
                    } else {
                        System.out.println("Car is not available.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Rental ID: ");
                    int rentalId = scanner.nextInt();

                    rentalDAO.returnCar(rentalId);  // Only pass Rental ID
                    break;
                
                case 5:
                    System.out.println("\n--- Car Rent & Customer Details ---");
                    List<String[]> rentalInfo = rentalDAO.getCarRentCustomerInfo();
                    if (rentalInfo.isEmpty()) {
                        System.out.println("No rental records found.");
                    } else {
                        System.out.printf("%-8s %-8s %-15s %-15s %-10s %-15s %-12s %-12s\n", 
                                          "Rent ID", "Car ID", "Car Name", "Brand", "Rent/Day", 
                                          "Customer Name", "Rent Date", "Return Date");
                        System.out.println("--------------------------------------------------------------------------------------");

                        for (String[] record : rentalInfo) {
                            System.out.printf("%-8s %-8s %-15s %-15s ₹%-10s %-15s %-12s %-12s\n", 
                                              record[0], record[1], record[2], record[3], record[4], 
                                              record[5], record[6], record[7]);
                        }
                    }
                    break;
                case 6:  // ✅ Case for searching rental by ID
                    System.out.print("Enter Rental ID to Search: ");
                    int rentalId1 = scanner.nextInt();

                    String[] rentalDetails = rentalDAO.getRentalDetailsById(rentalId1);
                    if (rentalDetails == null) {
                        System.out.println("Rental ID not found.");
                    } else {
                        System.out.printf("\n%-10s %-15s %-15s %-15s %-10s %-15s %-15s\n",
                                "Rent ID", "Customer Name", "Car Model", "Brand", "Cost/Day", "Rent Date", "Return Date");
                        System.out.println("---------------------------------------------------------------------------------------");
                        System.out.printf("%-10s %-15s %-15s %-15s ₹%-9s %-15s %-15s\n",
                                rentalDetails[0], rentalDetails[1], rentalDetails[2], rentalDetails[3],
                                rentalDetails[4], rentalDetails[5], rentalDetails[6]);
                    }
                    break;      
              case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
