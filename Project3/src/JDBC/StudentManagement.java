package JDBC;
import java.sql.*;
import java.util.Scanner;
import java.sql.DriverManager;


public class StudentManagement {
	
	static Connection con;
	public static void main(String[] args) {
		
	 String url ="jdbc:mysql://localhost:3306/demo";
	 String username ="root";
	 String password="keerthi09";
	 
	  Scanner sc = new Scanner(System.in);
	  try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		con =DriverManager.getConnection(url,username,password);
		
		System.out.println("Database connected successfully");
		
			int ch;
			

            do {

                System.out.println("\n===== STUDENT MANAGEMENT =====");
                System.out.println("1. Add Student");
                System.out.println("2. Read Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Fetch Student Names");
                System.out.println("6. Fetch Student by Department");
       
                System.out.println("7. Search Student by Name");
               
                System.out.println("8. Count Students");
                System.out.println("9. Transaction Management");
                System.out.println("10. Exit");

                System.out.print("Enter your choice: ");
                ch = sc.nextInt();
			
                
                switch (ch) {

                case 1:
                    addStudent(sc);
                    break;

                case 2:
                    readStudents();
                    break;

                case 3:
                    updateStudent(sc);
                    break;

                case 4:
                    deleteStudent(sc);
                    break;

                case 5:
                    fetchStudentNames();
                    break;


                case 6:
                    fetchStudentByDepartment(sc);
                    break;

               

                case 7:
                    searchStudentByName(sc);
                    break;

                case 8:
                    countStudents();
                    break;

                case 9:
                    transactionManagement(sc);
                    break;

                case 10:
                    System.out.println("Program exited.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
            }while(ch != 10);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	 
	}
	
	
	   // add students

    static void addStudent(Scanner sc) throws SQLException {

        System.out.print("Enter student id: ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter student name: ");
        String sname = sc.nextLine();

        System.out.print("Enter student email: ");
        String smail = sc.nextLine();

        System.out.print("Enter department: ");
        String department = sc.nextLine();

        String query =
                "insert into students values (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        ps.setString(2, sname);
        ps.setString(3, smail);
        ps.setString(4, department);

        int result = ps.executeUpdate();

        if (result > 0) {
            System.out.println("Student added successfully.");
        }

        ps.close();
    }
	// read
    
    

    static void readStudents() throws SQLException {

        String query = "select * from students";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(query);

        System.out.println("\nID | NAME | EMAIL | DEPARTMENT");

        while (rs.next()) {

            System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("sname") + " | " +
                    rs.getString("smail") + " | " +
                    rs.getString("department")
            );
        }

        rs.close();
        st.close();
    }
    
    //update

    static void updateStudent(Scanner sc) throws SQLException {

        System.out.print("Enter student id: ");
        int id = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter new name: ");
        String sname = sc.nextLine();

        System.out.print("Enter new email: ");
        String smail = sc.nextLine();

        System.out.print("Enter new department: ");
        String department = sc.nextLine();

        String query =
                "UPDATE students SET sname=?, smail=?, department=? WHERE id=?";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, sname);
        ps.setString(2, smail);
        ps.setString(3, department);
        ps.setInt(4, id);

        int result = ps.executeUpdate();

        if (result > 0) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Student not found.");
        }

        ps.close();
    }

	
//delete
    static void deleteStudent(Scanner sc) throws SQLException {

        System.out.print("Enter student id: ");
        int id = sc.nextInt();

        String query = "delete from students where id=?";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);

        int result = ps.executeUpdate();

        if (result > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }

        ps.close();
    }
	
    
    //fetch student name

    static void fetchStudentNames() throws SQLException {

        String query = "select  sname from students";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(query);

        System.out.println("\nStudent Names:");

        while (rs.next()) {

            System.out.println(
                    rs.getString("sname")
            );
        }

        rs.close();
        st.close();
    }
    
    // fetch student by department
    

    static void fetchStudentByDepartment(Scanner sc)
            throws SQLException {

        sc.nextLine();

        System.out.print("Enter department: ");
        String department = sc.nextLine();

        String query =
                "SELECT * FROM students WHERE department=?";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, department);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("sname") + " | " +
                    rs.getString("smail") + " | " +
                    rs.getString("department")
            );
        }

        rs.close();
        ps.close();
    }

//search student by name
    

    static void searchStudentByName(Scanner sc)
            throws SQLException {

        sc.nextLine();

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        String query =
                "select  * from  students where sname=?";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, name);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("sname") + " | " +
                    rs.getString("smail") + " | " +
                    rs.getString("department")
            );
        }

        rs.close();
        ps.close();
    }
    //count student
    

    static void countStudents()
            throws SQLException {

        String query =
                "select count(*) from  students";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(query);

        if (rs.next()) {

            System.out.println("Total students: " + rs.getInt(1) );
        }

        rs.close();
        st.close();
    }

//transaction

    static void transactionManagement(Scanner sc) {

        try {

            con.setAutoCommit(false);

            System.out.print("Enter student id: ");
            int id = sc.nextInt();

            sc.nextLine();

            System.out.print("Enter student name: ");
            String sname = sc.nextLine();

            System.out.print("Enter student email: ");
            String smail = sc.nextLine();

            System.out.print("Enter department: ");
            String department = sc.nextLine();

            String query =
                    "INSERT INTO students VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, sname);
            ps.setString(3, smail);
            ps.setString(4, department);

            ps.executeUpdate();

            con.commit();

            System.out.println(
                    "Transaction committed successfully."
            );

        } catch (Exception e) {

            try {

                if (con != null) {

                    con.rollback();

                    System.out.println(
                            "Transaction rolled back."
                    );
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }


}
