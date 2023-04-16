import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class adminTest {
    Connection TestConnection = null;
    Statement TestStatement = null;
    void makeConnection() throws ClassNotFoundException, SQLException {
        String TestURL = "jdbc:postgresql://localhost:5432/final";
        String TestUserName = "postgres";
        String TestPassword = "1234";
        Class.forName("org.postgresql.Driver");
        TestConnection = DriverManager.getConnection(TestURL, TestUserName, TestPassword);
        TestStatement = TestConnection.createStatement();
    }

    @Test
    void add_course() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        boolean r1 = Admin.add_course("CS101", "Discrete Maths", " ", " ", 4);
        assertTrue(r1);
        String s1 = "DELETE from courses where course_code= 'CS101'";
        makeConnection();
        TestStatement.executeUpdate(s1);
        TestStatement.close();
        TestConnection.close();
    }

    @Test
    void remove_course() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        String s1 = "INSERT INTO courses VALUES ('CS201',' ','2-2-2',' ',5)";
        makeConnection();
        TestStatement.executeUpdate(s1);
        TestStatement.close();
        TestConnection.close();
        boolean r1 = Admin.remove_course("CS201");
        assertTrue(r1);
    }

    @Test
    void check_user() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        makeConnection();
        String s1 = "INSERT INTO users VALUES ('2020csb1090','iitrpr','student','student','232232')";
        TestStatement.executeUpdate(s1);
        boolean r1 = Admin.check_user("2020csb1090");
        assertFalse(r1);
        String s2 = "DELETE From users where email='2020csb1090';";
        TestStatement.executeUpdate(s2);
        boolean r2 = Admin.check_user("2020csb1090");
        assertTrue(r2);
        TestStatement.close();
        TestConnection.close();
    }

    @Test
    void add_users() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        boolean r1 = Admin.add_users("2020csb1090", "Maths", " ", 1, "1221");
        assertTrue(r1);
        boolean r2 = Admin.add_users("2020csb1091", "Maths", " ", 2, "1221");
        assertTrue(r2);
        boolean r3 = Admin.add_users("2020csb1092", "Maths", " ", 3, "1221");
        assertTrue(r3);
        String s1 = "DELETE from users where email= '2020csb1090'";
        String s2 = "DELETE from users where email= '2020csb1091'";
        String s3 = "DELETE from users where email= '2020csb1092'";

        makeConnection();
        TestStatement.executeUpdate(s1);
        TestStatement.executeUpdate(s2);
        TestStatement.executeUpdate(s3);
        TestStatement.close();
        TestConnection.close();

    }

    @Test
    void change_currState() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        String s1 = "INSERT INTO currsem values('2023-1','EN')";
        makeConnection();
        TestStatement.executeUpdate(s1);
        boolean r1= Admin.change_currState("2023-1","GS");
        assertTrue(r1);
        String s2 = "DELETE from currsem where semester_name='2023-1';";
        TestStatement.executeUpdate(s2);
        TestStatement.close();
        TestConnection.close();

    }

    @Test
    void change_currSem() throws SQLException, ClassNotFoundException {
        admin Admin = new admin();
        String s1 = "INSERT INTO currsem values('2023-1','EN')";
        makeConnection();
        TestStatement.executeUpdate(s1);
        boolean r1= Admin.change_currSem("2023-1","2023-2");
        assertTrue(r1);
        String s2 = "DELETE from currsem where semester_name='2023-2';";
        TestStatement.executeUpdate(s2);
        TestStatement.close();
        TestConnection.close();
    }

    @Test
    void transcript() throws SQLException, IOException, ClassNotFoundException {
        admin Admin = new admin();
        faculty Faculty = new faculty();
        student Student = new student();
        Admin.add_course("CS101", " ", " ", "", 5);
        Faculty.offer_course("nitin", "2023-1", "CS101", 0);
        Student.register_course("2023-1", "2020csb1090@iitrpr.ac.in", "CS101");
        Faculty.grade_entry_single("2020csb1090@iitrpr.ac.in", "nitin", "CS101", "2023-1", 10);


        Boolean a = Admin.Transcript("2020csb1090@iitrpr.ac.in");

        assertEquals(true, a);
        makeConnection();
        String q = "TRUNCATE TABLE btp_students,course_offerings,courses,currsem,engineering_electives,enrollments, open_electives,program_core,users;";
        TestStatement.executeUpdate(q);
        TestStatement.close();
        TestConnection.close();
    }
}