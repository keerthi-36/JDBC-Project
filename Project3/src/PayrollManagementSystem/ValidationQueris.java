package PayrollManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidationQueris {

    static boolean validateEmployee(String name,
                                    String department,
                                    String phone) {

        if (!name.matches("[a-zA-Z]+")) {
            return false;
        }

        if (department.trim().isEmpty()) {
            return false;
        }

        if (!phone.matches("[0-9]{10}")) {
            return false;
        }

        return true;
    }


    static void reportsAndQueries(Connection con) {

        while (true) {

            System.out.println("\n===== REPORTS & QUERIES =====");
            System.out.println("1. Total Employees");
            System.out.println("2. Total Salary");
            System.out.println("3. Average Salary");
            System.out.println("4. Highest Salary");
            System.out.println("5. Department Wise Employees");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            java.util.Scanner sc = new java.util.Scanner(System.in);

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    totalEmployees(con);
                    break;

                case 2:
                    totalSalary(con);
                    break;

                case 3:
                    averageSalary(con);
                    break;

                case 4:
                    highestSalary(con);
                    break;

                case 5:
                    departmentWiseEmployees(con);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }


    static void totalEmployees(Connection con) {

        String query = """
                SELECT COUNT(*) AS total_employees
                FROM employees
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ EMPLOYEE REPORT ================");

            System.out.printf("%-20s%n", "TOTAL EMPLOYEES");
            System.out.println("--------------------");

            if (rs.next()) {

                System.out.printf("%-20d%n",
                        rs.getInt("total_employees"));
            }

        } catch (SQLException e) {

            System.out.println("Error while calculating employees");
            e.printStackTrace();
        }
    }


    static void totalSalary(Connection con) {

        String query = """
                SELECT SUM(net_salary) AS total_salary
                FROM payroll
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ SALARY REPORT ================");

            System.out.printf("%-20s%n", "TOTAL SALARY");
            System.out.println("--------------------");

            if (rs.next()) {

                System.out.printf("%-20.2f%n",
                        rs.getDouble("total_salary"));
            }

        } catch (SQLException e) {

            System.out.println("Error while calculating total salary");
            e.printStackTrace();
        }
    }


    static void averageSalary(Connection con) {

        String query = """
                SELECT AVG(net_salary) AS average_salary
                FROM payroll
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ AVERAGE SALARY ================");

            System.out.printf("%-20s%n", "AVERAGE SALARY");
            System.out.println("--------------------");

            if (rs.next()) {

                System.out.printf("%-20.2f%n",
                        rs.getDouble("average_salary"));
            }

        } catch (SQLException e) {

            System.out.println("Error while calculating average salary");
            e.printStackTrace();
        }
    }


    static void highestSalary(Connection con) {

        String query = """
                SELECT e.employee_name,
                       p.net_salary
                FROM employees e
                JOIN payroll p
                    ON e.employee_id = p.employee_id
                WHERE p.net_salary = (
                    SELECT MAX(net_salary)
                    FROM payroll
                )
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ HIGHEST SALARY ================");

            System.out.printf("%-20s %-15s%n",
                    "EMPLOYEE NAME", "NET SALARY");

            System.out.println("-----------------------------------------");

            while (rs.next()) {

                System.out.printf("%-20s %-15.2f%n",
                        rs.getString("employee_name"),
                        rs.getDouble("net_salary"));
            }

        } catch (SQLException e) {

            System.out.println("Error while finding highest salary");
            e.printStackTrace();
        }
    }


    static void departmentWiseEmployees(Connection con) {

        String query = """
                SELECT department,
                       COUNT(*) AS employee_count
                FROM employees
                GROUP BY department
                """;

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n========== DEPARTMENT WISE EMPLOYEES ==========");

            System.out.printf("%-20s %-15s%n",
                    "DEPARTMENT", "EMPLOYEE COUNT");

            System.out.println("-----------------------------------------");

            while (rs.next()) {

                System.out.printf("%-20s %-15d%n",
                        rs.getString("department"),
                        rs.getInt("employee_count"));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while fetching department details");
            e.printStackTrace();
        }
    }
}