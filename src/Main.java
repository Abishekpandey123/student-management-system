import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nStudent Management System");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent(conn, scanner);
                        break;
                    case 2:
                        viewStudents(conn);
                        break;
                    case 3:
                        updateStudent(conn, scanner);
                        break;
                    case 4:
                        deleteStudent(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found! Add MySQL Connector JAR.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }


    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();

        String sql = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setInt(2, age);
        stmt.setString(3, grade);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Student added successfully!");
        }
        stmt.close();
    }


    private static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nStudent List:");
        System.out.println("------------------------------------------------");
        System.out.printf("%-5s %-20s %-5s %-20s%n", "ID", "Name", "Age", "Grade");
        System.out.println("------------------------------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String grade = rs.getString("grade");
            System.out.printf("%-5d %-20s %-5d %-20s%n", id, name, age, grade);
        }
        stmt.close();
    }


    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter New Grade: ");
        String grade = scanner.nextLine();

        String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setInt(2, age);
        stmt.setString(3, grade);
        stmt.setInt(4, id);

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student ID not found.");
        }
        stmt.close();
    }


    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student ID not found.");
        }
        stmt.close();
    }
}