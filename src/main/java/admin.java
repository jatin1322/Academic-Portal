import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class admin extends user {



    Connection conn = null;
    void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(TestURL, TestUserName, TestPassword);

    }

    public boolean add_course(String courseCode, String courseDescription, String creditStructure, String preRequisite, double credits) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "INSERT INTO  courses (course_code,course_description,credit_structure,prerequisites,credits) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseCode);
        stmt.setString(2, courseDescription);
        stmt.setString(3, creditStructure);
        stmt.setString(4, preRequisite);
        stmt.setDouble(5, credits);

        stmt.executeUpdate();

        return true;
    }


    public boolean remove_course(String courseCode) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "DELETE from courses where course_code=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseCode);
        stmt.executeUpdate();

        return true;
    }

    public boolean check_user(String mail) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "Select email from users where email=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, mail);
        ResultSet rs = stmt.executeQuery();

        return !rs.next();

    }

    public boolean add_users(String mail, String UPassword, String name, int role, String contact) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "INSERT INTO  users (email,password,name,user_type,contact_info) VALUES (?,?,?,?,?)";
        String s = "";
        if (role == 1) {
            s = "student";
        }
        if (role == 2) {
            s = "faculty";
        }
        if (role == 3) {
            s = "admin";
        }
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, mail);
        stmt.setString(2, UPassword);
        stmt.setString(3, name);
        stmt.setString(4, s);
        stmt.setString(5, contact);
        stmt.executeUpdate();

        return true;
    }

    public boolean change_currState(String Cursem, String newe) throws SQLException, ClassNotFoundException {
        makeConnection();
        PreparedStatement stmt2 = conn.prepareStatement("UPDATE currsem SET curr_state = ? where semester_name=?");
        stmt2.setString(1, newe);
        stmt2.setString(2, Cursem);
        stmt2.executeUpdate();

        return true;
    }

    public boolean change_currSem(String Cursem, String newe) throws SQLException, ClassNotFoundException {

        makeConnection();
        PreparedStatement stmt2 = conn.prepareStatement("UPDATE currsem SET semester_name = ? where semester_name= ? ");
        stmt2.setString(1, newe);
        stmt2.setString(2, Cursem);

        stmt2.executeUpdate();

        return true;
    }

    public boolean Transcript(String Mail) throws SQLException, ClassNotFoundException, IOException {

        double CGPA = compute_CGPA(Mail);
        makeConnection();
        FileWriter writer;
        writer = new FileWriter("D:\\software project\\AIMS\\src\\main\\java\\transcript.txt");
        writer.write("Student Mail \tEntry Number\tCGPA \t\n");
        writer.write(Mail + "\t" + Mail.substring(0, 11) + "\t" + CGPA + "\n");
        writer.write("Course Code\tSemester\tGrade\n");
        String query2 = "SELECT course_code,semester_name,grades from course_offerings,enrollments where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id ";
        PreparedStatement stmt2 = conn.prepareStatement(query2);
        stmt2.setString(1, Mail);
        ResultSet rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            String courseCode = rs2.getString("course_code");
            String semesterName = rs2.getString("semester_name");
            int grade = rs2.getInt("grades");

            writer.write(courseCode + "\t" + semesterName + "\t" + grade + "\n");
        }

        return true;
    }

}
