
import java.sql.*;

public class student extends user {
    Connection conn = null;

    void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(TestURL, TestUserName, TestPassword);

    }

    public boolean check_prereq(String courseName, String mail) throws SQLException, ClassNotFoundException {
        makeConnection();
        String query = "SELECT prerequisites from courses where course_code =? ";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, courseName);
        ResultSet rs = stmt.executeQuery();
        String Prereq = "";
        while (rs.next()) {
            Prereq = rs.getString(1);
        }
        if (Prereq.equals("")) {
            return false;
        }
        else if(Prereq.equals("xxx")){
            return true;
        }
        String[] array = Prereq.split(",");

        for (String s : array) {
            String query1 = "SELECT grades from enrollments,course_offerings where enrollments.student_mail =? and  course_offerings.course_code=? and enrollments.course_offering_id=course_offerings.course_offering_id";
            PreparedStatement stmt3 = conn.prepareStatement(query1);
            stmt3.setString(1, mail);
            stmt3.setString(2, s);
            ResultSet rs3 = stmt3.executeQuery();
            int check = 0;
            while (rs3.next()) {
                check = 1;
                if (rs3.getInt(1) < 4) {
                    return false;
                }
            }
            if (check == 0) {
//            conn.close();
                return false;
            }
        }
//        conn.close();
        return true;
    }


    public boolean check_creditLimit(String courseName, String mail, String currentSem) throws SQLException, ClassNotFoundException {

        double total_prev_credit = 0;
        double credits_this_sem = 0;
        double course_credit = 0;
        String Prev1;
        String Prev2;
        String Curryear = currentSem.substring(0, 4);
        int Curryear_num = Integer.parseInt(Curryear);
        if (currentSem.charAt(5) == '1') {
            Prev1 = Curryear_num - 1 + "-1";

        } else {
            Prev1 = Curryear_num + "-1";
        }
        Prev2 = Curryear_num - 1 + "-2";

        makeConnection();

        String query = "Select courses.credits from enrollments,courses,course_offerings where student_mail=? and enrollments.course_offering_id=course_offerings.course_offering_id and courses.course_code=course_offerings.course_code and course_offerings.semester_name=? and enrollments.grades>3";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, mail);
        stmt.setString(2, Prev1);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            total_prev_credit += rs.getDouble(1);

        }
        //
        PreparedStatement stmt1 = conn.prepareStatement(query);
        stmt1.setString(1, mail);
        stmt1.setString(2, Prev2);
        ResultSet rs1 = stmt1.executeQuery();
        while (rs1.next()) {
            total_prev_credit += rs1.getDouble(1);

        }
        PreparedStatement stmt2 = conn.prepareStatement(query);
        stmt2.setString(1, mail);
        stmt2.setString(2, currentSem);
        ResultSet rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            credits_this_sem += rs2.getDouble(1);

        }
        String query1 = "Select courses.credits from courses where course_code=?";
        PreparedStatement stmt3 = conn.prepareStatement(query1);
        stmt3.setString(1, courseName);
        ResultSet rs3 = stmt3.executeQuery();
        while (rs3.next()) {
            course_credit += rs3.getDouble(1);
        }
        if (total_prev_credit == 0) {
//            conn.close();
            return course_credit + credits_this_sem <= 18;
        }
//        conn.close();
        return (course_credit + credits_this_sem) <= (1.25 * (total_prev_credit/2));


    }

    public String register_course(String currentSemester, String mail, String courseName) throws SQLException, ClassNotFoundException {

        makeConnection();

        String q1 = "SELECT course_offering_id,cgpa_constraint from course_offerings  where course_offerings.course_code=? and course_offerings.semester_name=?";
        PreparedStatement stmt2 = conn.prepareStatement(q1);
        stmt2.setString(1, courseName);
        stmt2.setString(2, currentSemester);
        ResultSet rs = stmt2.executeQuery();
        double minCGPA=-1;
        while(rs.next()){
             minCGPA = rs.getDouble(2);
        }
        if (minCGPA==-1) {
//            conn.close();
            return "notoffered";
        }
        if (!check_prereq(courseName, mail)) {
//            conn.close();
            return "prereqnotful";
        }
        if (!check_creditLimit(courseName, mail, currentSemester)) {
//            conn.close();
            return "creditlimit";
        }

        double currCGPA = compute_CGPA(mail);
        if (minCGPA > currCGPA) {
//            conn.close();
            return "minCGPA";
        }


        String query = "INSERT INTO  enrollments (student_mail,course_offering_id) VALUES (?,(SELECT course_offering_id from course_offerings  where course_offerings.course_code=? and course_offerings.semester_name=?))";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, mail);
        stmt.setString(2, courseName);
        stmt.setString(3, currentSemester);
        stmt.executeUpdate();

        conn.close();
        return "done";
    }

    public String deregister_course(String currentSemester, String mail, String courseNameD) throws SQLException, ClassNotFoundException {


        makeConnection();

        String q1 = "SELECT enrollments.course_offering_id from enrollments,course_offerings  where enrollments.student_mail=? and course_offerings.course_code=? and course_offerings.semester_name=?";
        PreparedStatement stmt2 = conn.prepareStatement(q1);
        stmt2.setString(1, mail);
        stmt2.setString(2, courseNameD);
        stmt2.setString(3, currentSemester);
        ResultSet rs = stmt2.executeQuery();
        int id;
        if(rs.next()) {
            id = rs.getInt(1);
            String q2 = "DELETE from enrollments  where student_mail=? and course_offering_id=?";
            PreparedStatement stmt3 = conn.prepareStatement(q2);
            stmt3.setString(1, mail);
            stmt3.setInt(2, id);
            stmt3.executeUpdate();
//            conn.close();
            return "done";
        }
//        conn.close();
        return "notexist";

    }

}

