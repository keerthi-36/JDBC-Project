package EmployeeLeaveManagement;
import java.util.regex.Pattern;
import java.sql.*;
import java.util.Scanner;



public class ValidationQueris {

    static boolean validateEmployee(String name, String email,
                                    String department, String joiningDate) {

        // name should contain only alphabets
        if (!name.matches("[a-zA-Z]+")) {
            return false;
        }

        // email validation
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            return false;
        }

        // department should not be empty
        if (department.trim().isEmpty()) {
            return false;
        }

        // joining date should not be empty
        if (joiningDate.trim().isEmpty()) {
            return false;
        }

        return true;
    }
    
    
    static void customQueries(Connection con, Scanner sc) {

        while (true) {

            System.out.println("\n===== CUSTOM QUERIES =====");
            System.out.println("1. View Employees by Department");
            System.out.println("2. View Pending Leave Requests");
            System.out.println("3. Count Employees");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    employeesByDepartment(con, sc);
                    break;

                case 2:
                    pendingLeaveRequests(con);
                    break;

                case 3:
                    countEmployees(con);
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
    // case 1
    static void employeesByDepartment(Connection con, Scanner sc) {

        System.out.print("Enter Department: ");
        String department = sc.next();

        String query = """
                SELECT employee_id, employee_name, email, department
                FROM employees
                WHERE department = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, department);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Email         : " + rs.getString("email"));
                System.out.println("Department    : " + rs.getString("department"));
                System.out.println("-------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching employees");
            e.printStackTrace();
        }
    }
    
    static void pendingLeaveRequests(Connection con) {

        String query = """
                SELECT leave_id, employee_id, leave_type,
                       from_date, to_date, reason, status
                FROM leave_requests
                WHERE status = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "PENDING");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== PENDING LEAVE REQUESTS =====");

            while (rs.next()) {
                System.out.println("Leave ID    : " + rs.getInt("leave_id"));
                System.out.println("Employee ID : " + rs.getInt("employee_id"));
                System.out.println("Leave Type  : " + rs.getString("leave_type"));
                System.out.println("From Date   : " + rs.getDate("from_date"));
                System.out.println("To Date     : " + rs.getDate("to_date"));
                System.out.println("Reason      : " + rs.getString("reason"));
                System.out.println("Status      : " + rs.getString("status"));
                System.out.println("-----------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching pending leaves");
            e.printStackTrace();
        }
    }
    
    
    //case 3
    
    static void countEmployees(Connection con) {

        String query = "SELECT COUNT(*) AS total_employees FROM employees";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("total_employees");

                System.out.println("Total Employees: " + count);
            }

        } catch (SQLException e) {
            System.out.println("Error while counting employees");
            e.printStackTrace();
        }
    }
    
    
    // display result
    
    static void displayResults(ResultSet rs) throws SQLException {

        while (rs.next()) {

            System.out.println("Employee ID   : " + rs.getInt("employee_id"));
            System.out.println("Employee Name : " + rs.getString("employee_name"));
            System.out.println("Email         : " + rs.getString("email"));
            System.out.println("Department    : " + rs.getString("department"));

            System.out.println("-----------------------------");
        }
    }

}
