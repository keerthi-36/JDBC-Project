package EmployeeLeaveManagement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class TableOperations {
	
	
	    static void createTables(Connection con) throws SQLException {

	        // Employees table
	        String employeeTable = """
	                CREATE TABLE IF NOT EXISTS employees (
	                    employee_id INT PRIMARY KEY AUTO_INCREMENT,
	                    employee_name VARCHAR(100) NOT NULL,
	                    email VARCHAR(100) UNIQUE NOT NULL,
	                    department VARCHAR(50) NOT NULL,
	                    joining_date DATE NOT NULL
	                )
	                """;

	        // Leave Requests table
	        String leaveRequestTable = """
	                CREATE TABLE IF NOT EXISTS leave_requests (
	                    leave_id INT PRIMARY KEY AUTO_INCREMENT,
	                    employee_id INT NOT NULL,
	                    leave_type VARCHAR(30) NOT NULL,
	                    from_date DATE NOT NULL,
	                    to_date DATE NOT NULL,
	                    reason VARCHAR(255),
	                    status VARCHAR(20) DEFAULT 'PENDING',
	                    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
	                )
	                """;

	        // Leave Balance table
	        String leaveBalanceTable = """
	                CREATE TABLE IF NOT EXISTS leave_balance (
	                    balance_id INT PRIMARY KEY AUTO_INCREMENT,
	                    employee_id INT NOT NULL,
	                    casual_leave INT DEFAULT 12,
	                    sick_leave INT DEFAULT 10,
	                    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
	                )
	                """;

	        try (Statement st = con.createStatement()) {

	            st.executeUpdate(employeeTable);
	            st.executeUpdate(leaveRequestTable);
	            st.executeUpdate(leaveBalanceTable);

	            System.out.println("Tables created successfully");

	        } catch (SQLException e) {
	            System.out.println("Error while creating tables");
	            e.printStackTrace();
	        }
	    }
	    
	    
	    
	    static void addEmployee(Connection con, Scanner sc) {

	        System.out.print("Enter Employee Name: ");
	        String name = sc.next();

	        System.out.print("Enter Email: ");
	        String email = sc.next();

	        System.out.print("Enter Department: ");
	        String department = sc.next();

	        System.out.print("Enter Joining Date (yyyy-mm-dd): ");
	        String joiningDate = sc.next();

	        if (!ValidationQueris.validateEmployee(name, email, department, joiningDate)) {
	            System.out.println("Invalid employee details");
	            return;
	        }

	        String query = """
	                INSERT INTO employees
	                (employee_name, email, department, joining_date)
	                VALUES (?, ?, ?, ?)
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.setString(3, department);
	            ps.setDate(4, Date.valueOf(joiningDate));

	            ps.executeUpdate();

	            System.out.println("Employee added successfully");

	        } catch (SQLException e) {
	            System.out.println("Error while adding employee");
	            e.printStackTrace();
	        }
	    }
	    
	    
	    static void viewEmployees(Connection con) {

	        String query = "SELECT * FROM employees";

	        try (PreparedStatement ps = con.prepareStatement(query);
	             ResultSet rs = ps.executeQuery()) {

	            System.out.println("\n===== EMPLOYEE DETAILS =====");

	            while (rs.next()) {

	                System.out.println("Employee ID    : " + rs.getInt("employee_id"));
	                System.out.println("Employee Name  : " + rs.getString("employee_name"));
	                System.out.println("Email          : " + rs.getString("email"));
	                System.out.println("Department     : " + rs.getString("department"));
	                System.out.println("Joining Date   : " + rs.getDate("joining_date"));
	                System.out.println("-----------------------------");
	            }

	        } catch (SQLException e) {
	            System.out.println("Error while viewing employees");
	            e.printStackTrace();
	        }
	    }
	    
	    
	    //update
	    static void updateEmployee(Connection con, Scanner sc) {

	        System.out.print("Enter Employee ID to update: ");
	        int id = sc.nextInt();

	        System.out.print("Enter New Employee Name: ");
	        String name = sc.next();

	        System.out.print("Enter New Email: ");
	        String email = sc.next();

	        System.out.print("Enter New Department: ");
	        String department = sc.next();

	        System.out.print("Enter New Joining Date (yyyy-mm-dd): ");
	        String joiningDate = sc.next();

	        if (!ValidationQueris.validateEmployee(name, email, department, joiningDate)) {
	            System.out.println("Invalid employee details");
	            return;
	        }

	        String query = """
	                UPDATE employees
	                SET employee_name = ?, email = ?, department = ?, joining_date = ?
	                WHERE employee_id = ?
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.setString(3, department);
	            ps.setDate(4, java.sql.Date.valueOf(joiningDate));
	            ps.setInt(5, id);

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
	
	    //delete
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
	    
	    
	    //apply leave
	    static void applyLeave(Connection con, Scanner sc) {

	        System.out.print("Enter Employee ID: ");
	        int employeeId = sc.nextInt();

	        System.out.print("Enter Leave Type (CASUAL/SICK): ");
	        String leaveType = sc.next();

	        System.out.print("Enter From Date (yyyy-mm-dd): ");
	        String fromDate = sc.next();

	        System.out.print("Enter To Date (yyyy-mm-dd): ");
	        String toDate = sc.next();

	        System.out.print("Enter Reason: ");
	        String reason = sc.next();

	        String query = """
	                INSERT INTO leave_requests
	                (employee_id, leave_type, from_date, to_date, reason)
	                VALUES (?, ?, ?, ?, ?)
	                """;

	        try (PreparedStatement ps = con.prepareStatement(query)) {

	            ps.setInt(1, employeeId);
	            ps.setString(2, leaveType);
	            ps.setDate(3, Date.valueOf(fromDate));
	            ps.setDate(4, Date.valueOf(toDate));
	            ps.setString(5, reason);

	            ps.executeUpdate();

	            System.out.println("Leave applied successfully");

	        } catch (SQLException e) {
	            System.out.println("Error while applying leave");
	            e.printStackTrace();
	        }
	    }
	    
	    
	  //leave request
		
		static void viewLeaveRequests(Connection con) {

		    String query = "SELECT * FROM leave_requests";

		    try (PreparedStatement ps = con.prepareStatement(query);
		         ResultSet rs = ps.executeQuery()) {

		        System.out.println("\n===== LEAVE REQUESTS =====");

		        while (rs.next()) {
		            System.out.println("Leave ID      : " + rs.getInt("leave_id"));
		            System.out.println("Employee ID   : " + rs.getInt("employee_id"));
		            System.out.println("Leave Type    : " + rs.getString("leave_type"));
		            System.out.println("From Date     : " + rs.getDate("from_date"));
		            System.out.println("To Date       : " + rs.getDate("to_date"));
		            System.out.println("Reason        : " + rs.getString("reason"));
		            System.out.println("Status        : " + rs.getString("status"));
		            System.out.println("-----------------------------");
		        }

		    } catch (SQLException e) {
		        System.out.println("Error while viewing leave requests");
		        e.printStackTrace();
		    }
		}
		
		
		// approve leave
		// approve leave
		static void approveLeave(Connection con, Scanner sc) {

		    System.out.print("Enter Leave ID to approve: ");
		    int leaveId = sc.nextInt();

		    try {
		        con.setAutoCommit(false);

		        // 1. Get leave details
		        String selectLeave = """
		                SELECT employee_id, leave_type, from_date, to_date, status
		                FROM leave_requests
		                WHERE leave_id = ?
		                """;

		        int employeeId;
		        String leaveType;
		        Date fromDate;
		        Date toDate;
		        String status;

		        try (PreparedStatement ps = con.prepareStatement(selectLeave)) {

		            ps.setInt(1, leaveId);

		            ResultSet rs = ps.executeQuery();

		            if (!rs.next()) {
		                System.out.println("Leave ID not found");
		                con.rollback();
		                return;
		            }

		            employeeId = rs.getInt("employee_id");
		            leaveType = rs.getString("leave_type");
		            fromDate = rs.getDate("from_date");
		            toDate = rs.getDate("to_date");
		            status = rs.getString("status");
		        }

		        // 2. Check leave status
		        if (!status.equals("PENDING")) {
		            System.out.println("Leave is already processed");
		            con.rollback();
		            return;
		        }

		        // 3. Calculate number of leave days
		        long difference = toDate.getTime() - fromDate.getTime();
		        int days = (int) (difference / (1000 * 60 * 60 * 24)) + 1;

		        // 4. Deduct leave balance
		        String updateBalance;

		        if (leaveType.equalsIgnoreCase("CASUAL")) {

		            updateBalance = """
		                    UPDATE leave_balance
		                    SET casual_leave = casual_leave - ?
		                    WHERE employee_id = ?
		                    AND casual_leave >= ?
		                    """;

		        } else if (leaveType.equalsIgnoreCase("SICK")) {

		            updateBalance = """
		                    UPDATE leave_balance
		                    SET sick_leave = sick_leave - ?
		                    WHERE employee_id = ?
		                    AND sick_leave >= ?
		                    """;

		        } else {

		            System.out.println("Invalid leave type");
		            con.rollback();
		            return;
		        }

		        try (PreparedStatement ps = con.prepareStatement(updateBalance)) {

		            ps.setInt(1, days);
		            ps.setInt(2, employeeId);
		            ps.setInt(3, days);

		            int rows = ps.executeUpdate();

		            if (rows == 0) {
		                System.out.println("Insufficient leave balance");
		                con.rollback();
		                return;
		            }
		        }

		        // 5. Update leave status
		        String updateLeave = """
		                UPDATE leave_requests
		                SET status = 'APPROVED'
		                WHERE leave_id = ?
		                """;

		        try (PreparedStatement ps = con.prepareStatement(updateLeave)) {

		            ps.setInt(1, leaveId);
		            ps.executeUpdate();
		        }

		        // 6. Commit transaction
		        con.commit();

		        System.out.println("Leave approved successfully");
		        System.out.println("Leave balance updated successfully");

		    } catch (SQLException e) {

		        try {
		            con.rollback();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }

		        System.out.println("Transaction failed - changes rolled back");
		        e.printStackTrace();

		    } finally {

		        try {
		            con.setAutoCommit(true);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
		
	//reject leave
		static void rejectLeave(Connection con, Scanner sc) {

		    System.out.print("Enter Leave ID to reject: ");
		    int leaveId = sc.nextInt();

		    String query = """
		            UPDATE leave_requests
		            SET status = 'REJECTED'
		            WHERE leave_id = ?
		            """;

		    try (PreparedStatement ps = con.prepareStatement(query)) {

		        ps.setInt(1, leaveId);

		        int rows = ps.executeUpdate();

		        if (rows > 0) {
		            System.out.println("Leave rejected successfully");
		        } else {
		            System.out.println("Leave ID not found");
		        }

		    } catch (SQLException e) {
		        System.out.println("Error while rejecting leave");
		        e.printStackTrace();
		    }
		}
		
// view leave balance
		
		static void viewLeaveBalance(Connection con) {

		    String query = """
		            SELECT * FROM leave_balance
		            """;

		    try (PreparedStatement ps = con.prepareStatement(query);
		         ResultSet rs = ps.executeQuery()) {

		        System.out.println("\n===== LEAVE BALANCE =====");

		        while (rs.next()) {
		            System.out.println("Balance ID    : " + rs.getInt("balance_id"));
		            System.out.println("Employee ID   : " + rs.getInt("employee_id"));
		            System.out.println("Casual Leave  : " + rs.getInt("casual_leave"));
		            System.out.println("Sick Leave    : " + rs.getInt("sick_leave"));
		            System.out.println("-----------------------------");
		        }

		    } catch (SQLException e) {
		        System.out.println("Error while viewing leave balance");
		        e.printStackTrace();
		    }
		}
		
		

}






