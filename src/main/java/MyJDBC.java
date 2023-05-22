import java.sql.*;
import java.util.Scanner;

public class MyJDBC {

    static Connection connection1 = null;
    static Connection connection2 = null;

    public static void connectDatabase() {
        System.out.println("Enter Database Credentials");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.print("Enter Username: ");
        String userName = myObj.nextLine();

        System.out.print("Enter Password: ");
        String password = myObj.nextLine();

        String db1 = "jdbc:mysql://localhost:3306/";
        System.out.print("Enter database name which has Master Data: ");
        db1 += myObj.nextLine();

        String db2 = "jdbc:mysql://localhost:3306/";
        System.out.print("Enter database name which has your DW: ");
        db2 += myObj.nextLine();

        try {
            connection1 = DriverManager.getConnection(db2, userName, password);
            connection2 = DriverManager.getConnection(db1, userName, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
