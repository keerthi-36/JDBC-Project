package InventoryManagementSystem;
import java.sql.PreparedStatement;
import java.util.Scanner;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;


public class TableOperations {

	    static void createTables(Connection con) throws SQLException {

	        String supplierTable = """
	                CREATE TABLE IF NOT EXISTS suppliers (
	                    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
	                    supplier_name VARCHAR(100) NOT NULL,
	                    email VARCHAR(100) UNIQUE NOT NULL,
	                    phone VARCHAR(15) NOT NULL
	                )
	                """;

	        String productTable = """
	                CREATE TABLE IF NOT EXISTS products (
	                    product_id INT PRIMARY KEY AUTO_INCREMENT,
	                    product_name VARCHAR(100) NOT NULL,
	                    category VARCHAR(50) NOT NULL,
	                    price DECIMAL(10,2) NOT NULL,
	                    stock_quantity INT NOT NULL,
	                    supplier_id INT,
	                    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
	                )
	                """;

	        String salesTable = """
	                CREATE TABLE IF NOT EXISTS sales (
	                    sale_id INT PRIMARY KEY AUTO_INCREMENT,
	                    product_id INT NOT NULL,
	                    quantity INT NOT NULL,
	                    total_amount DECIMAL(10,2) NOT NULL,
	                    sale_date DATE NOT NULL,
	                    FOREIGN KEY (product_id) REFERENCES products(product_id)
	                )
	                """;

	        try (Statement st = con.createStatement()) {

	            st.executeUpdate(supplierTable);
	            st.executeUpdate(productTable);
	            st.executeUpdate(salesTable);

	            System.out.println("Tables created successfully");

	        } catch (SQLException e) {

	            System.out.println("Error while creating tables");
	            e.printStackTrace();
	        }
	    }
	    
	    
	// add suppliers
	    
	    static void addSupplier(Connection con, Scanner sc) {

	        System.out.print("Enter Supplier Name: ");
	        String name = sc.next();

	        System.out.print("Enter Email: ");
	        String email = sc.next();

	        System.out.print("Enter Phone: ");
	        String phone = sc.next();
	        
	        if(!ValidationQueris.validateSupplier(name, email, phone)) {
	        	System.out.println("Invalid Supplier details");
	        	return;
	        }

	        String query = """
	                INSERT INTO suppliers
	                (supplier_name, email, phone)
	                VALUES (?, ?, ?)
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.setString(3, phone);

	            ps.executeUpdate();

	            System.out.println("Supplier added successfully");

	        } catch (SQLException e) {

	            System.out.println("Error while adding supplier");
	            e.printStackTrace();
	        }
	    }
	    
	    // view supplier
	    static void viewSuppliers(Connection con) {

	        String query = "SELECT * FROM suppliers";

	        try (PreparedStatement ps = con.prepareStatement(query);
	             ResultSet rs = ps.executeQuery()) {

	        	System.out.println("\n================ SUPPLIER DETAILS ================");

	            System.out.printf("%-5s %-20s %-30s %-15s%n",
	                    "ID", "SUPPLIER NAME", "EMAIL", "PHONE");

	            System.out.println("-----------------------------------------------------------------------");

	            while (rs.next()) {

	                System.out.printf("%-5d %-20s %-30s %-15s%n",
	                        rs.getInt("supplier_id"),
	                        rs.getString("supplier_name"),
	                        rs.getString("email"),
	                        rs.getString("phone"));
	            }

	            System.out.println("-----------------------------------------------------------------------");
	            

	        } catch (SQLException e) {

	            System.out.println("Error while viewing suppliers");
	            e.printStackTrace();
	        }
	    }
	    
	    //add product
	    
	    static void addProduct(Connection con, Scanner sc) {

	        System.out.print("Enter Product Name: ");
	        String name = sc.next();

	        System.out.print("Enter Category: ");
	        String category = sc.next();

	        System.out.print("Enter Price: ");
	        double price = sc.nextDouble();

	        System.out.print("Enter Stock Quantity: ");
	        int stock = sc.nextInt();

	        System.out.print("Enter Supplier ID: ");
	        int supplierId = sc.nextInt();
	        
	        if(!ValidationQueris.validateProduct(name, category, price, stock)) {
	        	System.out.println("Invalid product details");
	        	return;
	        }

	        String query = """
	                INSERT INTO products
	                (product_name, category, price, stock_quantity, supplier_id)
	                VALUES (?, ?, ?, ?, ?)
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setString(1, name);
	            ps.setString(2, category);
	            ps.setDouble(3, price);
	            ps.setInt(4, stock);
	            ps.setInt(5, supplierId);

	            ps.executeUpdate();

	            System.out.println("Product added successfully");

	        } catch (SQLException e) {

	            System.out.println("Error while adding product");
	            e.printStackTrace();
	        }
	    }
	    
	    // view product
	    
	    static void viewProducts(Connection con) {

	        String query = "SELECT * FROM products";

	        try (PreparedStatement ps = con.prepareStatement(query);
	             ResultSet rs = ps.executeQuery()) {

	            System.out.println("\n=================== PRODUCT DETAILS  ===================");

	            System.out.printf("%-5s %-15s %-15s %-10s %-10s %-10s%n",
	                    "ID", "NAME", "CATEGORY", "PRICE", "STOCK", "SUPPLIER");

	            System.out.println("---------------------------------------------------------------");

	            while (rs.next()) {

	                System.out.printf("%-5d %-15s %-15s %-10.2f %-10d %-10d%n",
	                        rs.getInt("product_id"),
	                        rs.getString("product_name"),
	                        rs.getString("category"),
	                        rs.getDouble("price"),
	                        rs.getInt("stock_quantity"),
	                        rs.getInt("supplier_id"));
	            }

	            System.out.println("---------------------------------------------------------------");
	            

	        } catch (SQLException e) {

	            System.out.println("Error while viewing products");
	            e.printStackTrace();
	        }
	    }
	    
	    //update product
	    
	    static void updateProduct(Connection con, Scanner sc) {

	        System.out.print("Enter Product ID to update: ");
	        int id = sc.nextInt();

	        System.out.print("Enter New Product Name: ");
	        String name = sc.next();

	        System.out.print("Enter New Category: ");
	        String category = sc.next();

	        System.out.print("Enter New Price: ");
	        double price = sc.nextDouble();

	        String query = """
	                UPDATE products
	                SET product_name = ?, category = ?, price = ?
	                WHERE product_id = ?
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setString(1, name);
	            ps.setString(2, category);
	            ps.setDouble(3, price);
	            ps.setInt(4, id);

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                System.out.println("Product updated successfully");
	            } else {
	                System.out.println("Product ID not found");
	            }

	        } catch (SQLException e) {

	            System.out.println("Error while updating product");
	            e.printStackTrace();
	        }
	    }
	    
	    // delete product
	    
	    static void deleteProduct(Connection con, Scanner sc) {

	        System.out.print("Enter Product ID to delete: ");
	        int id = sc.nextInt();

	        String query = "DELETE FROM products WHERE product_id = ?";

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setInt(1, id);

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                System.out.println("Product deleted successfully");
	            } else {
	                System.out.println("Product ID not found");
	            }

	        } catch (SQLException e) {

	            System.out.println("Error while deleting product");
	            e.printStackTrace();
	        }
	    }
	    
	    // update stock
	    
	    static void updateStock(Connection con, Scanner sc) {

	        System.out.print("Enter Product ID: ");
	        int productId = sc.nextInt();

	        System.out.print("Enter Stock Quantity to Add: ");
	        int quantity = sc.nextInt();

	        String query = """
	                UPDATE products
	                SET stock_quantity = stock_quantity + ?
	                WHERE product_id = ?
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setInt(1, quantity);
	            ps.setInt(2, productId);

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                System.out.println("Stock updated successfully");
	            } else {
	                System.out.println("Product ID not found");
	            }

	        } catch (SQLException e) {

	            System.out.println("Error while updating stock");
	            e.printStackTrace();
	        }
	    }
	    
	    // recordsale
	    static void recordSale(Connection con, Scanner sc) {

	        System.out.print("Enter Product ID: ");
	        int productId = sc.nextInt();

	        System.out.print("Enter Quantity Sold: ");
	        int quantity = sc.nextInt();

	        try {

	            con.setAutoCommit(false);

	            // 1. Get product price and stock
	            
	            String selectQuery = """
	                    SELECT price, stock_quantity
	                    FROM products
	                    WHERE product_id = ?
	                    """;

	            double price;
	            int stock;

	            try (PreparedStatement ps = con.prepareStatement(selectQuery)) {

	                ps.setInt(1, productId);

	                ResultSet rs = ps.executeQuery();

	                if (!rs.next()) {
	                    System.out.println("Product ID not found");
	                    con.rollback();
	                    return;
	                }

	                price = rs.getDouble("price");
	                stock = rs.getInt("stock_quantity");
	            }

	            // 2. Check stock
	            
	            if (quantity > stock) {
	                System.out.println("Insufficient stock");
	                con.rollback();
	                return;
	            }

	            // 3. Calculate total amount
	            double totalAmount = price * quantity;

	            // 4. Insert sale
	            String saleQuery = """
	                    INSERT INTO sales
	                    (product_id, quantity, total_amount, sale_date)
	                    VALUES (?, ?, ?, CURDATE())
	                    """;

	            try (PreparedStatement ps = con.prepareStatement(saleQuery)) {

	                ps.setInt(1, productId);
	                ps.setInt(2, quantity);
	                ps.setDouble(3, totalAmount);

	                ps.executeUpdate();
	            }

	            // 5. Reduce stock
	            String stockQuery = """
	                    UPDATE products
	                    SET stock_quantity = stock_quantity - ?
	                    WHERE product_id = ?
	                    """;

	            try (PreparedStatement ps = con.prepareStatement(stockQuery)) {

	                ps.setInt(1, quantity);
	                ps.setInt(2, productId);

	                ps.executeUpdate();
	            }

	            
	            con.commit();

	            System.out.println("Sale recorded successfully");
	            System.out.println("Total Amount: " + totalAmount);
	            System.out.println("Stock updated successfully");

	        } catch (SQLException e) {

	            try {
	                con.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }

	            System.out.println("Sale failed - changes rolled back");
	            e.printStackTrace();

	        } finally {

	            try {
	                con.setAutoCommit(true);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    } 
	    
	    // generate records
	
	    static void generateReports(Connection con) {

	        try {

	            System.out.println("\n===== INVENTORY REPORTS =====");

	            // 1. Total Products
	            
	            String query1 = "SELECT COUNT(*) AS total_products FROM products";

	            try (PreparedStatement ps = con.prepareStatement(query1);
	                 ResultSet rs = ps.executeQuery()) {

	                if (rs.next()) {
	                    System.out.println("Total Products      : "
	                            + rs.getInt("total_products"));
	                }
	            }

	            // 2. Total Stock
	            
	            String query2 = "SELECT SUM(stock_quantity) AS total_stock FROM products";

	            try (PreparedStatement ps = con.prepareStatement(query2);
	                 ResultSet rs = ps.executeQuery()) {

	                if (rs.next()) {
	                    System.out.println("Total Stock         : "
	                            + rs.getInt("total_stock"));
	                }
	            }

	            // 3. Average Product Price
	            String query3 = "SELECT AVG(price) AS average_price FROM products";

	            try (PreparedStatement ps = con.prepareStatement(query3);
	                 ResultSet rs = ps.executeQuery()) {

	                if (rs.next()) {
	                    System.out.println("Average Price       : "
	                            + rs.getDouble("average_price"));
	                }
	            }

	            // 4. Highest Product Price
	            
	            String query4 = "SELECT MAX(price) AS highest_price FROM products";

	            try (PreparedStatement ps = con.prepareStatement(query4);
	                 ResultSet rs = ps.executeQuery()) {

	                if (rs.next()) {
	                    System.out.println("Highest Price       : "
	                            + rs.getDouble("highest_price"));
	                }
	            }

	            // 5. Total Sales Amount
	            
	            String query5 = "SELECT SUM(total_amount) AS total_sales FROM sales";

	            try (PreparedStatement ps = con.prepareStatement(query5);
	                 ResultSet rs = ps.executeQuery()) {

	                if (rs.next()) {
	                    System.out.println("Total Sales Amount  : "
	                            + rs.getDouble("total_sales"));
	                }
	            }

	        } catch (SQLException e) {

	            System.out.println("Error while generating reports");
	            e.printStackTrace();
	        }
	    }
	    
	    //batch
	    static void batchAddProducts(Connection con, Scanner sc) {

	        System.out.print("Enter number of products: ");
	        int count = sc.nextInt();

	        String query = """
	                INSERT INTO products
	                (product_name, category, price, stock_quantity, supplier_id)
	                VALUES (?, ?, ?, ?, ?)
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            for (int i = 1; i <= count; i++) {

	                System.out.println("\nEnter Product " + i + " details");

	                System.out.print("Product Name: ");
	                String name = sc.next();

	                System.out.print("Category: ");
	                String category = sc.next();

	                System.out.print("Price: ");
	                double price = sc.nextDouble();

	                System.out.print("Stock Quantity: ");
	                int stock = sc.nextInt();

	                System.out.print("Supplier ID: ");
	                int supplierId = sc.nextInt();

	                if (!ValidationQueris.validateProduct(
	                        name, category, price, stock)) {

	                    System.out.println("Invalid product details");
	                    continue;
	                }

	                ps.setString(1, name);
	                ps.setString(2, category);
	                ps.setDouble(3, price);
	                ps.setInt(4, stock);
	                ps.setInt(5, supplierId);

	                ps.addBatch();
	            }

	            ps.executeBatch();

	            System.out.println("Products added successfully using batch processing");

	        } catch (SQLException e) {

	            System.out.println("Error while batch adding products");
	            e.printStackTrace();
	        }
	    }
	    
	}


