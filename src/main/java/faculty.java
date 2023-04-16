import java.io.*;
import java.sql.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class faculty extends user {
    Connection conn = null;

     void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(TestURL, TestUserName, TestPassword);

    }


    public String offer_course(String mail, String currentSemester, String coursecode, double minCgpa) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "INSERT INTO  course_offerings (course_code,faculty_mail,semester_name,cgpa_constraint) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, coursecode);
        stmt.setString(2, mail);
        stmt.setString(3, currentSemester);
        stmt.setDouble(4, minCgpa);
        stmt.executeUpdate();
        conn.close();
        return "done";
    }

    public String remove_enrollments(String mail, String currentSemester, String courseCode_remove) throws SQLException, ClassNotFoundException {
        makeConnection();
        String q = "Select course_offering_id from course_offerings where faculty_mail=? and course_code=? and semester_name=?";
        PreparedStatement stmt2 = conn.prepareStatement(q);
        stmt2.setString(1, mail);
        stmt2.setString(2, courseCode_remove);
        stmt2.setString(3, currentSemester);
        ResultSet rs = stmt2.executeQuery();
        int a = 0;
        while (rs.next()) {
            a = rs.getInt(1);

        }
        if (a == 0) {
            conn.close();
            return "coursenotexist";
        }
        String query = "DELETE  from enrollments where course_offering_id= ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, a);
        stmt.executeUpdate();
        conn.close();
        return "done";
    }

    public String remove_course(String mail, String currentSemester, String courseCode_remove) throws SQLException, ClassNotFoundException {
        makeConnection();

        String query = "DELETE  from course_offerings where course_code= ? and faculty_mail =? and semester_name = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseCode_remove);
        stmt.setString(2, mail);
        stmt.setString(3, currentSemester);
        stmt.executeUpdate();
        conn.close();
        return "done";
    }

    public  boolean download_classList(String mail, String courseName, String semesterName) throws SQLException, ClassNotFoundException, IOException {
        makeConnection();
        String query = "SELECT enrollments.student_mail,course_offerings.course_code,course_offerings.semester_name,enrollments.grades FROM course_offerings,enrollments where enrollments.course_offering_id=course_offerings.course_offering_id and course_code=? and course_offerings.semester_name=? and faculty_mail=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseName);
        stmt.setString(2, semesterName);
        stmt.setString(3, mail);
        ResultSet rs = stmt.executeQuery();

        File file = new File("D:\\software project\\AIMS\\src\\main\\java\\output.csv");

        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);
        String[] headers = {"student_mail", "course_code", "Semester_name", "grades"};
        writer.writeNext(headers);

        // Write the data to the CSV file
        while (rs.next()) {
            String[] data = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
            writer.writeNext(data);
        }
        conn.close();
        writer.close();
        return true;
    }

    public boolean grade_entry(String mail) throws SQLException, ClassNotFoundException, IOException {

        makeConnection();
        File file = new File("D:\\software project\\AIMS\\src\\main\\java\\output.csv");
        String UPDATE_GRADES_QUERY = "UPDATE Enrollments " + "SET grades = ? WHERE student_mail = ? " + "AND course_offering_id = (SELECT course_offering_id " + "FROM Course_Offerings " + "WHERE course_code = ? AND semester_name = ? AND faculty_mail = ?)";
        PreparedStatement stmt = conn.prepareStatement(UPDATE_GRADES_QUERY);
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        reader.readNext();
        while ((line = reader.readNext()) != null) {
            String studentMail = line[0];
            String courseCode = line[1];
            String SemesterName = line[2];
            int grades = Integer.parseInt(line[3]);
            stmt.setInt(1, grades);
            stmt.setString(2, studentMail);
            stmt.setString(3, courseCode);
            stmt.setString(4, SemesterName);
            stmt.setString(5, mail);
            stmt.executeUpdate();

        }
        conn.close();
        return true;
    }

    public boolean grade_entry_single(String student_mail, String Faculty_mail, String course_code, String semname, int grade) throws SQLException, ClassNotFoundException {

        makeConnection();
        String UPDATE_GRADES_QUERY = "UPDATE Enrollments " + "SET grades = ? WHERE student_mail = ? " + "AND course_offering_id = (SELECT course_offering_id " + "FROM Course_Offerings " + "WHERE course_code = ? AND semester_name = ? AND faculty_mail = ?)";

        PreparedStatement stmt = conn.prepareStatement(UPDATE_GRADES_QUERY);
        stmt.setInt(1, grade);
        stmt.setString(2, student_mail);
        stmt.setString(3, course_code);
        stmt.setString(4, semname);
        stmt.setString(5, Faculty_mail);
        stmt.executeUpdate();
        conn.close();
        return true;


    }
}
