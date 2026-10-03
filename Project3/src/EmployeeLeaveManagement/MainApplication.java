package EmployeeLeaveManagement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApplication {
	 static final  String url ="jdbc:mysql://localhost:3306/employee_leave_management";
	 static final String username ="root";
	 static final String password="keerthi09";
	 
	 static Connection con = null;
	
	public static void main(String[] args) {
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		con =DriverManager.getConnection(url,username,password);
			
			System.out.println("Database connected successfully");
			
			Scanner sc = new Scanner(System.in);
			
			TableOperations.createTables(con);
			 while (true) {

	                System.out.println("\n===== EMPLOYEE LEAVE MANAGEMENT SYSTEM =====");
	                System.out.println("1. Add Employee");
	                System.out.println("2. View Employees");
	                System.out.println("3. Update Employee");
	                System.out.println("4. Delete Employee");
	                System.out.println("5. Apply Leave");
	                System.out.println("6. View Leave Requests");
	                System.out.println("7. Approve Leave");
	                System.out.println("8. Reject Leave");
	                System.out.println("9. View Leave Balance");
	                System.out.println("10. Custom Queries");
	                System.out.println("11. Exit");

	                System.out.print("Enter your choice: ");
	                int choice = sc.nextInt();

	                switch (choice) {

	                case 1:
	                    TableOperations.addEmployee(con, sc);
	                    break;

	                case 2:
	                    TableOperations.viewEmployees(con);
	                    break;

	                case 3:
	                    TableOperations.updateEmployee(con, sc);
	                    break;

	                case 4:
	                    TableOperations.deleteEmployee(con, sc);
	                    break;

	                case 5:
	                    TableOperations.applyLeave(con, sc);
	                    break;

	                case 6:
	                    TableOperations.viewLeaveRequests(con);
	                    break;

	                case 7:
	                    TableOperations.approveLeave(con, sc);
	                    break;

	                case 8:
	                    TableOperations.rejectLeave(con, sc);
	                    break;

	                case 9:
	                    TableOperations.viewLeaveBalance(con);
	                    break;

	                case 10:
	                    ValidationQueris.customQueries(con, sc);
	                    break;

	                case 11:
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


