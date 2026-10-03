package InventoryManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApplication {
	 static final  String url ="jdbc:mysql://localhost:3306/inventory_management";
	 static final String username ="root";
	 static final String password="keerthi09";
	 
	 static Connection con = null;
	 
	 public static void main(String[] args) {
		
	

	        try {

	            Class.forName("com.mysql.cj.jdbc.Driver");

	            con = DriverManager.getConnection(url, username, password);

	            System.out.println("Database connected successfully");

	            Scanner sc = new Scanner(System.in);

	            TableOperations.createTables(con);

	            while (true) {

	                System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
	                System.out.println("1. Add Product");
	                System.out.println("2. View Products");
	                System.out.println("3. Update Product");
	                System.out.println("4. Delete Product");
	                System.out.println("5. Add Supplier");
	                System.out.println("6. View Suppliers");
	                System.out.println("7. Update Stock");
	                System.out.println("8. Record Sale");
	                System.out.println("9. Generate Reports");
	                System.out.println("10. Reports & Queries");
	                System.out.println("11.Batch Add");
	                
	                System.out.println("12. Exit");

	                System.out.print("Enter your choice: ");
	                int choice = sc.nextInt();

	                switch (choice) {

	                    case 1:
	                        TableOperations.addProduct(con, sc);
	                        break;

	                    case 2:
	                        TableOperations.viewProducts(con);
	                        break;

	                    case 3:
	                        TableOperations.updateProduct(con, sc);
	                        break;

	                    case 4:
	                        TableOperations.deleteProduct(con, sc);
	                        break;

	                    case 5:
	                        TableOperations.addSupplier(con, sc);
	                        break;

	                    case 6:
	                        TableOperations.viewSuppliers(con);
	                        break;

	                    case 7:
	                        TableOperations.updateStock(con, sc);
	                        break;

	                    case 8:
	                        TableOperations.recordSale(con, sc);
	                        break;

	                    case 9:
	                        TableOperations.generateReports(con);
	                        break;

	                    case 10:
	                        ValidationQueris.reportsAndQueries(con, sc);
	                        break;
	                        
	                    case 11:
	                    	TableOperations.batchAddProducts(con,sc);
	                    	break;

	                    case 12:
	                        System.out.println("Thank you!");
	                        con.close();
	                        sc.close();
	                        return;

	                    default:
	                        System.out.println("Invalid choice");
	                }
	            }

	        } catch (ClassNotFoundException | SQLException e) {

	            e.printStackTrace();
	        }
	    }
}
