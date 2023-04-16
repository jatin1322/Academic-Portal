import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class faculty_UI {
    Scanner scanner = new Scanner(System.in);
    faculty Faculty = new faculty();

    public int faculty_ui() {
        System.out.println("To perform Specific task press number");
        System.out.println("1) Offer a course");
        System.out.println("2) Deregister for a course"); // done
        System.out.println("3) view grades");
        System.out.println("4) Grade Entry");
        System.out.println("5) Download class list Into CSV file");
        System.out.println("6) Change Contact Number");
        System.out.println("7) Change Password");
        System.out.println("8) Logout");
        return Integer.parseInt(scanner.nextLine());
    }

    public void offer_course(String email, String currentSemester) throws SQLException, ClassNotFoundException {
        System.out.println("Enter Course Code Which you want to offer");
        String coursecode = scanner.nextLine();
        String output1 = Faculty.check_catalogue(coursecode);
        if (output1.equals("notincatalog")) {
            System.out.println("Course code you entered in not in course catalog");
            return;
        }
        System.out.println("Enter Min CGPA Required for your course");
        float minCPGA = Float.parseFloat(scanner.nextLine());
        String output = Faculty.offer_course(email, currentSemester, coursecode, minCPGA);
        if (output.equals("notdone")) {
            System.out.println("Course Not offered");
        } else {
            System.out.println("Course Offered");
        }
    }


    public void remove_course(String mail, String currentSemester) throws SQLException, ClassNotFoundException {
        System.out.println("Enter Course Code Which you want to remove");
        String courseName = scanner.nextLine();
        String b = Faculty.remove_enrollments(mail, currentSemester, courseName);
        if (b.equals("coursenotexist")) {
            System.out.println("Course Not exists");
        }
        else  System.out.println("Enrollments Deleted for this course");

        String a = Faculty.remove_course(mail, currentSemester, courseName);
        if (a.equals("done")) {
            System.out.println("Course Removed");
        }


    }



    public void view_grades() throws SQLException, ClassNotFoundException {
        System.out.println("Enter Mail ID of student");
        String mail = scanner.nextLine();
        Faculty.view_grades(mail);
    }

    public void grade_entry(String mail) throws SQLException, ClassNotFoundException, IOException {
        System.out.println(
                "Make sure you have added grades in output.csv file, if not first generate it using download option");

        boolean a = Faculty.grade_entry(mail);
        if (a) System.out.println("Grade Successfully Updated");
        else System.out.println("Grade Not Successfully Updated");


    }

    public void download_classList(String mail) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Enter CourseName Whose Class List you want to download");
        String courseName = scanner.nextLine();
        System.out.println("Semester Name");
        String semesterName = scanner.nextLine();
        boolean output = Faculty.download_classList(mail, courseName, semesterName);
        if (output) System.out.println("output.csv Generated");
        else System.out.println("File Not Generated");

    }

    public void changeContactInfo(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Contact Number(Digits only)");
        String NewContact = scanner.nextLine();
        if (Faculty.changeContactInfo(mail, NewContact)) {
            System.out.println("Contact Updated");
        } else {
            System.out.println("Contact Not Updated");
        }

    }

    public void changePassword(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Password");
        String NewPassword = scanner.nextLine();
        if (Faculty.changePassword(mail, NewPassword)) {
            System.out.println("Password Updated");
        } else {
            System.out.println("Password Not Updated");
        }
    }


}
