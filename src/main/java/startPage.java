
import java.sql.*;
import java.util.Scanner;

public class startPage {
    Connection conn = null;

    void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(TestURL, TestUserName, TestPassword);

    }

    Scanner scanner = new Scanner(System.in);

    public String getMail() {
        System.out.print("Enter Your College email: ");
        String email = scanner.nextLine();
        email = email.toLowerCase();
        String regex = "^[a-zA-Z0-9+_.-]+@iitrpr.ac.in$";
        boolean result = email.matches(regex);
        while (!result) {
            System.out.print("Enter College email only: ");
            email = scanner.nextLine();
            email = email.toLowerCase();
            result = email.matches(regex);
        }

        return email;
    }

    public String getPassword()  {
        System.out.print("Enter password: ");

        return scanner.nextLine();
    }

    public String login(String email, String password) throws ClassNotFoundException, SQLException {
        makeConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT user_type FROM users WHERE email = ? AND password = ?");
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getString(1);
        conn.close();
        return "invalid";
    }

    public String getCurrSemOrCurrState(int n) throws SQLException, ClassNotFoundException {
        String currentSemester = "";
        makeConnection();
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM currsem");
        ResultSet rsa = stmt2.executeQuery();
        while (rsa.next()) {
            currentSemester = rsa.getString(n);

        }
        conn.close();
        return currentSemester;
    }

}
