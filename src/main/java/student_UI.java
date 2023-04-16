
import java.sql.SQLException;
import java.util.Scanner;

public class student_UI {

    Scanner scanner = new Scanner(System.in);
    student Student = new student();

    public int student_ui() {
        System.out.println("To perform Specific task press number");
        System.out.println("1)Register For a course");
        System.out.println("2)Deregister for a course");
        System.out.println("3)view grades");
        System.out.println("4)Check Graduation");
        System.out.println("5)Compute CGPA");
        System.out.println("6)Change Contact Number");
        System.out.println("7)Change Password");
        System.out.println("8)Logout");
        return Integer.parseInt(scanner.nextLine());
    }

    public void register_course(String currentSemester, String mail) throws SQLException, ClassNotFoundException {
        System.out.println("Enter Name of course you want to register");
        String courseName = scanner.nextLine();

        String output = Student.register_course(currentSemester, mail, courseName);
        switch (output) {
            case "notoffered" -> System.out.println("This Course Is not Offered this sem");
            case "prereqnotful" -> System.out.println("Prerequisites criteria not fulfilled");
            case "creditlimit" ->
                    System.out.println("You cannot register for that course as it will exceed you credit limit");
            case "minCGPA" ->
                    System.out.println("You cannot register for that course as it has minCGPA requirement higher than your current CGPA");
            default -> System.out.println("Enrollment Added");
        }

    }

    public void deregister_course(String currentSemester, String mail) throws SQLException, ClassNotFoundException {
        System.out.println("Enter Name of course you want to deregister");
        String courseNameD = scanner.nextLine();

        String output = Student.deregister_course(currentSemester, mail, courseNameD);
        if (output.equals("notexist")) {
            System.out.println("Enrollment doesn't exists");
        } else {
            System.out.println("Enrollment Deleted");
        }
    }
   public void compute_CGPA(String email) throws SQLException, ClassNotFoundException {
       double cgpa = Student.compute_CGPA(email);
       System.out.println("Your current CGPA is  " + cgpa);
   }
    public void changeContactInfo(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Contact Number(Digits only)");
        String NewContact = scanner.nextLine();
        if(Student.changeContactInfo(mail,NewContact)){ System.out.println("Contact Updated");}
        else{System.out.println("Contact Not Updated"); }

    }
    public void changePassword(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Password");
        String NewPassword = scanner.nextLine();
        if(Student.changePassword(mail,NewPassword)){ System.out.println("Password Updated");}
        else{System.out.println("Password Not Updated"); }
    }
    public void view_grades(String mail) throws SQLException, ClassNotFoundException {
        Student.view_grades(mail);
    }
    public void CheckGraduation(String mail) throws SQLException, ClassNotFoundException {
      boolean a=  Student.Graduation_check(mail);
        if(a){
            System.out.println("Eligible for Graduation");
        }
        else  System.out.println("Not Eligible for Graduation currently");
    }

}
