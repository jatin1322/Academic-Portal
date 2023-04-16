import java.sql.*;


public class user {
    Connection conn = null;

    private void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(TestURL, TestUserName, TestPassword);

    }


    public boolean view_grades(String mail) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query1 = "SELECT course_code,semester_name,grades from course_offerings,enrollments where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id ";
        PreparedStatement stmt2 = conn.prepareStatement(query1);
        stmt2.setString(1, mail);
        ResultSet rs2 = stmt2.executeQuery();
        System.out.println("All Grades in All enrollments");
        System.out.println("Course Code\tSemester\tGrade");
        while (rs2.next()) {
            System.out.print(rs2.getString(1));
            System.out.print("\t");
            System.out.print(rs2.getString(2));
            System.out.print("\t");
            System.out.print(rs2.getString(3));
            System.out.println("\t");

        }
        conn.close();
        return true;
    }


    public boolean changeContactInfo(String mail, String NewContact) throws SQLException, ClassNotFoundException {

        makeConnection();
        String query = "UPDATE users SET contact_info=?  where email=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, NewContact);
        stmt.setString(2, mail);
        stmt.executeUpdate();
        conn.close();
        return true;
    }

    public boolean changePassword(String mail, String NewPassword) throws SQLException, ClassNotFoundException {

        makeConnection();

        String query = "UPDATE users SET password=?  where email=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, NewPassword);
        stmt.setString(2, mail);
        stmt.executeUpdate();
        conn.close();
        return true;
    }

    public boolean Graduation_check(String mail) throws SQLException, ClassNotFoundException {

        makeConnection();
        int Programcredits = 0;
        String query1 = "select program_core.credits from program_core,(SELECT course_code from course_offerings,enrollments where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id and grades>3) as completed_courses where program_core.course_code=completed_courses.course_code";
        PreparedStatement stmt2 = conn.prepareStatement(query1);
        stmt2.setString(1, mail);
        ResultSet rs2 = stmt2.executeQuery();

        while (rs2.next()) {
            Programcredits += rs2.getInt(1);
        }
        int engineeringCredits = 0;
        String query2 = "select engineering_electives.credits from engineering_electives,(SELECT course_code from course_offerings,enrollments where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id and grades>3) as completed_courses where engineering_electives.course_code=completed_courses.course_code";
        PreparedStatement stmt3 = conn.prepareStatement(query2);
        stmt3.setString(1, mail);
        ResultSet rs3 = stmt3.executeQuery();

        while (rs3.next()) {
            engineeringCredits += rs3.getInt(1);
        }
        int openCredits = 0;
        String query3 = "select open_electives.credits from open_electives,(SELECT course_code from course_offerings,enrollments where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id and grades>3) as completed_courses where open_electives.course_code=completed_courses.course_code";
        PreparedStatement stmt4 = conn.prepareStatement(query3);
        stmt4.setString(1, mail);
        ResultSet rs4 = stmt4.executeQuery();

        while (rs4.next()) {
            openCredits += rs4.getInt(1);
        }
        boolean completedBTP = false;
        String query4 = "select student_mail from btp_students where student_mail=?";
        PreparedStatement stmt5 = conn.prepareStatement(query4);
        stmt5.setString(1, mail);
        ResultSet rs5 = stmt5.executeQuery();
        if (rs5.next()) {
            completedBTP = true;
        }
        conn.close();
        return Programcredits >= 60 && engineeringCredits >= 30 && openCredits >= 30 && completedBTP;
    }

    public double compute_CGPA(String mail) throws SQLException, ClassNotFoundException {
        double total_credits = 0;
        double creditXgrades = 0;

        makeConnection();

        String query = "SELECT grades,credits from enrollments,course_offerings,courses where grades > 3 and student_mail='" + mail + "' and enrollments.course_offering_id=course_offerings.course_offering_id and courses.course_code=course_offerings.course_code;";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int a = rs.getInt("grades");
            double b = rs.getDouble("credits");
            creditXgrades += a * b;
            total_credits += b;

        }
        if (total_credits == 0) {
            conn.close();
            return 0;
        }

        conn.close();
        return (creditXgrades) / total_credits;

    }


    public String check_catalogue(String coursecode) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "SELECT Course_code from courses where course_code= ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, coursecode);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            conn.close();
            return "notincatalog";
        }
        conn.close();
        return "present";
    }

}





