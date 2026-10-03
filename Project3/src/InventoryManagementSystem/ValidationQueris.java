package InventoryManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidationQueris {

    static boolean validateProduct(String name, String category,
                                   double price, int stock) {

        if (!name.matches("[a-zA-Z]+")) {
            return false;
        }

        if (category.trim().isEmpty()) {
            return false;
        }

        if (price <= 0) {
            return false;
        }

        if (stock < 0) {
            return false;
        }

        return true;
    }


    static boolean validateSupplier(String name, String email, String phone) {

        if (!name.matches("[a-zA-Z]+")) {
            return false;
        }

        if (!Pattern.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                email)) {
            return false;
        }

        if (!phone.matches("[0-9]{10}")) {
            return false;
        }

        return true;
    }


    static void reportsAndQueries(Connection con, Scanner sc) {

        while (true) {

            System.out.println("\n===== REPORTS & QUERIES =====");
            System.out.println("1. View Products by Category");
            System.out.println("2. View Low Stock Products");
            System.out.println("3. View Total Sales");
            System.out.println("4. View Most Expensive Product");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    productsByCategory(con, sc);
                    break;

                case 2:
                    lowStockProducts(con);
                    break;

                case 3:
                    totalSales(con);
                    break;

                case 4:
                    mostExpensiveProduct(con);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }


    static void productsByCategory(Connection con, Scanner sc) {

        System.out.print("Enter Category: ");
        String category = sc.next();

        String query = """
                SELECT * FROM products
                WHERE category = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println("Product ID     : "
                        + rs.getInt("product_id"));

                System.out.println("Product Name   : "
                        + rs.getString("product_name"));

                System.out.println("Category       : "
                        + rs.getString("category"));

                System.out.println("Price          : "
                        + rs.getDouble("price"));

                System.out.println("Stock Quantity : "
                        + rs.getInt("stock_quantity"));

                System.out.println("-----------------------------");
            }

        } catch (SQLException e) {

            System.out.println("Error while fetching products");
            e.printStackTrace();
        }
    }


    static void lowStockProducts(Connection con) {

        String query = """
                SELECT * FROM products
                WHERE stock_quantity < 10
                """;

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

            System.out.println("Error while fetching low stock products");
            e.printStackTrace();
        }
    }


    static void totalSales(Connection con) {

        String query = """
                SELECT SUM(total_amount) AS total_sales
                FROM sales
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                System.out.println("Total Sales Amount : "
                        + rs.getDouble("total_sales"));
            }

        } catch (SQLException e) {

            System.out.println("Error while calculating total sales");
            e.printStackTrace();
        }
    }


    static void mostExpensiveProduct(Connection con) {

        String query = """
                SELECT product_name, price
                FROM products
                WHERE price = (SELECT MAX(price) FROM products)
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println("Product Name : "
                        + rs.getString("product_name"));

                System.out.println("Price        : "
                        + rs.getDouble("price"));
            }

        } catch (SQLException e) {

            System.out.println("Error while finding product");
            e.printStackTrace();
        }
    }
}