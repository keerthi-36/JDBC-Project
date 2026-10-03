package PayrollManagementSystem;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class TableOperations {


    static void createTables(Connection con) throws SQLException {

        String employeeTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_name VARCHAR(100) NOT NULL,
                    department VARCHAR(50) NOT NULL,
                    phone VARCHAR(15) NOT NULL
                )
                """;

        String salaryTable = """
                CREATE TABLE IF NOT EXISTS salaries (
                    salary_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_id INT NOT NULL,
                    basic_salary DECIMAL(10,2) NOT NULL,
                    allowance DECIMAL(10,2) NOT NULL,
                    deduction DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (employee_id)
                    REFERENCES employees(employee_id)
                )
                """;

        String payrollTable = """
                CREATE TABLE IF NOT EXISTS payroll (
                    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_id INT NOT NULL,
                    gross_salary DECIMAL(10,2) NOT NULL,
                    tax_amount DECIMAL(10,2) NOT NULL,
                    net_salary DECIMAL(10,2) NOT NULL,
                    payroll_date DATE NOT NULL,
                    FOREIGN KEY (employee_id)
                    REFERENCES employees(employee_id)
                )
                """;

        try (Statement st = con.createStatement()) {

            st.executeUpdate(employeeTable);
            st.executeUpdate(salaryTable);
            st.executeUpdate(payrollTable);

            System.out.println("Tables created successfully");

        } catch (SQLException e) {

            System.out.println("Error while creating tables");
            e.printStackTrace();
        }
    }
    
    // add emp
    
    static void addEmployee(Connection con, Scanner sc) {

        System.out.print("Enter Employee Name: ");
        String name = sc.next();

        System.out.print("Enter Department: ");
        String department = sc.next();

        System.out.print("Enter Phone: ");
        String phone = sc.next();

        if (!ValidationQueris.validateEmployee(name, department, phone)) {
            System.out.println("Invalid employee details");
            return;
        }

        String query = """
                INSERT INTO employees
                (employee_name, department, phone)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, phone);

            ps.executeUpdate();

            System.out.println("Employee added successfully");

        } catch (SQLException e) {

            System.out.println("Error while adding employee");
            e.printStackTrace();
        }
    }
    
    // view emp
    static void viewEmployees(Connection con) {

        String query = "SELECT * FROM employees";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ EMPLOYEE DETAILS ================");

            System.out.printf("%-5s %-20s %-20s %-15s%n",
                    "ID", "NAME", "DEPARTMENT", "PHONE");

            System.out.println("------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-20s %-15s%n",
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("department"),
                        rs.getString("phone"));
            }

            System.out.println("------------------------------------------------------------");

        } catch (SQLException e) {

            System.out.println("Error while viewing employees");
            e.printStackTrace();
        }
    }
    
    // update emp
    static void updateEmployee(Connection con, Scanner sc) {

        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();

        System.out.print("Enter New Employee Name: ");
        String name = sc.next();

        System.out.print("Enter New Department: ");
        String department = sc.next();

        System.out.print("Enter New Phone: ");
        String phone = sc.next();

        if (!ValidationQueris.validateEmployee(name, department, phone)) {
            System.out.println("Invalid employee details");
            return;
        }

        String query = """
                UPDATE employees
                SET employee_name = ?, department = ?, phone = ?
                WHERE employee_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee updated successfully");
            } else {
                System.out.println("Employee ID not found");
            }

        } catch (SQLException e) {

            System.out.println("Error while updating employee");
            e.printStackTrace();
        }
    }
    
    // delete emp
    static void deleteEmployee(Connection con, Scanner sc) {

        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM employees WHERE employee_id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee deleted successfully");
            } else {
                System.out.println("Employee ID not found");
            }

        } catch (SQLException e) {

            System.out.println("Error while deleting employee");
            e.printStackTrace();
        }
    }
    
    // salary management
    
    static void salaryManagement(Connection con, Scanner sc) {

        System.out.print("Enter Employee ID: ");
        int employeeId = sc.nextInt();

        System.out.print("Enter Basic Salary: ");
        double basicSalary = sc.nextDouble();

        System.out.print("Enter Allowance: ");
        double allowance = sc.nextDouble();

        System.out.print("Enter Deduction: ");
        double deduction = sc.nextDouble();

        double grossSalary = basicSalary + allowance;

        double taxAmount;

        if (grossSalary <= 25000) {
            taxAmount = grossSalary * 0.05;
        } else if (grossSalary <= 50000) {
            taxAmount = grossSalary * 0.10;
        } else {
            taxAmount = grossSalary * 0.15;
        }

        double netSalary = grossSalary - deduction - taxAmount;

        String salaryQuery = """
                INSERT INTO salaries
                (employee_id, basic_salary, allowance, deduction)
                VALUES (?, ?, ?, ?)
                """;

        String payrollQuery = """
                INSERT INTO payroll
                (employee_id, gross_salary, tax_amount,
                 net_salary, payroll_date)
                VALUES (?, ?, ?, ?, CURDATE())
                """;

        try {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(salaryQuery)) {

                ps.setInt(1, employeeId);
                ps.setDouble(2, basicSalary);
                ps.setDouble(3, allowance);
                ps.setDouble(4, deduction);

                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(payrollQuery)) {

                ps.setInt(1, employeeId);
                ps.setDouble(2, grossSalary);
                ps.setDouble(3, taxAmount);
                ps.setDouble(4, netSalary);

                ps.executeUpdate();
            }

            con.commit();

            System.out.println("\nSalary calculated successfully");
            System.out.println("Gross Salary : " + grossSalary);
            System.out.println("Tax Amount   : " + taxAmount);
            System.out.println("Net Salary   : " + netSalary);

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Salary calculation failed - changes rolled back");

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // generate payslip
    static void generatePayslip(Connection con, Scanner sc) {

        System.out.print("Enter Employee ID: ");
        int employeeId = sc.nextInt();

        String query = """
                SELECT e.employee_id,
                       e.employee_name,
                       e.department,
                       s.basic_salary,
                       s.allowance,
                       s.deduction,
                       p.gross_salary,
                       p.tax_amount,
                       p.net_salary,
                       p.payroll_date
                FROM employees e
                JOIN salaries s
                    ON e.employee_id = s.employee_id
                JOIN payroll p
                    ON e.employee_id = p.employee_id
                WHERE e.employee_id = ?
                ORDER BY p.payroll_id DESC
                LIMIT 1
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, employeeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n================ PAYSLIP ================");

                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Department    : " + rs.getString("department"));
                System.out.println("-----------------------------------------");
                System.out.println("Basic Salary  : " + rs.getDouble("basic_salary"));
                System.out.println("Allowance     : " + rs.getDouble("allowance"));
                System.out.println("Deduction     : " + rs.getDouble("deduction"));
                System.out.println("Gross Salary  : " + rs.getDouble("gross_salary"));
                System.out.println("Tax Amount    : " + rs.getDouble("tax_amount"));
                System.out.println("Net Salary    : " + rs.getDouble("net_salary"));
                System.out.println("Payroll Date  : " + rs.getDate("payroll_date"));
                System.out.println("=========================================");

            } else {

                System.out.println("Payroll details not found");
            }

        } catch (SQLException e) {

            System.out.println("Error while generating payslip");
            e.printStackTrace();
        }
    }
    
  // batch add
    
    static void batchAddEmployees(Connection con, Scanner sc) {

        System.out.print("Enter number of employees: ");
        int count = sc.nextInt();

        String query = """
                INSERT INTO employees
                (employee_name, department, phone)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {

            for (int i = 1; i <= count; i++) {

                System.out.println("\nEnter Employee " + i + " details");

                System.out.print("Employee Name: ");
                String name = sc.next();

                System.out.print("Department: ");
                String department = sc.next();

                System.out.print("Phone: ");
                String phone = sc.next();

                if (!ValidationQueris.validateEmployee(
                        name, department, phone)) {

                    System.out.println("Invalid employee details");
                    continue;
                }

                ps.setString(1, name);
                ps.setString(2, department);
                ps.setString(3, phone);

                ps.addBatch();
            }

            ps.executeBatch();

            System.out.println(
                    "Employees added successfully using batch processing");

        } catch (SQLException e) {

            System.out.println("Error while batch adding employees");
            e.printStackTrace();
        }
    }


}
