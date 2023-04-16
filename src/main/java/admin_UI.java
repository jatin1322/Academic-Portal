import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class admin_UI {
    Scanner scanner = new Scanner(System.in);
    admin Admin=new admin();
    public int admin_ui(){
        System.out.println("To perform Specific task press number");
        System.out.println("1)Create a course"); // done
        System.out.println("2)Remove Course"); // done
        System.out.println("3)Add User"); // done
        System.out.println("4)Change Current Semester Or current State");// done
        System.out.println("5)View Grades"); // done
        System.out.println("6)Generate Transcript");
        System.out.println("7)Check Graduation");  //done
        System.out.println("8)Change Contact Number"); // done
        System.out.println("9)Change Password"); // done
        System.out.println("10)Logout"); // done
        return Integer.parseInt(scanner.nextLine());
    }
    public void add_course() throws SQLException, ClassNotFoundException {
        System.out.println("Enter course code");
        String courseCode = scanner.nextLine();
        System.out.println("Enter course Description(optional)");
        String courseDescription = scanner.nextLine();
        System.out.println("Enter course Credit structure(L-T-P)");
        String creditStructure = scanner.nextLine();
        System.out.println("Enter number of prerequisites");
        int coursePrereqNumber = Integer.parseInt(scanner.nextLine());
        String preRequisite = "xxx";
        if (coursePrereqNumber != 0) {
            preRequisite = "";
            for (int i = 0; i < coursePrereqNumber; i++) {
                System.out.println("Enter Name of " + (i + 1) + " prerequisites");
                String temp = scanner.nextLine();
                preRequisite += temp;
                if (i != coursePrereqNumber - 1)
                    preRequisite += ",";
            }
        }

        System.out.println("Enter number of Credits");
        float credits = Float.parseFloat(scanner.nextLine());
        boolean output=Admin.add_course(courseCode,courseDescription,creditStructure,preRequisite,credits);
        if(output) System.out.println("Course Added Successfully");
        else System.out.println("Course Not Added");

    }

    public void remove_course() throws SQLException, ClassNotFoundException {
        System.out.println("Enter course code");
        String courseCode = scanner.nextLine();
        String output1=Admin.check_catalogue(courseCode);
        if (output1.equals("notincatalog")) {
            System.out.println("Course code you entered in not in course catalog");
            return ;
        }
        boolean output=Admin.remove_course(courseCode);
        if(output) System.out.println("Course Removed Successfully");
        else System.out.println("Course Not Removed");
    }
    public void add_users() throws SQLException, ClassNotFoundException {
        System.out.println("Press 1 for student, 2 for faculty, 3 for admin");
        int role = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter mail");
        String mail = scanner.nextLine();
        if(!Admin.check_user(mail)){
            System.out.println("Email Already registered");
            return;
        }
        System.out.println("Enter Password, Default is \"iitropar\"");
        String password = scanner.nextLine();
        System.out.println("Enter name(optional)");
        String name = scanner.nextLine();
        System.out.println("Enter contact number");
        String contact = scanner.nextLine();
        boolean output=Admin.add_users(mail,password,name,role,contact);
        if(output){
            System.out.println("User Added");

        }
        else{
            System.out.println("User Not Added");
        }
    }
    public void change_currSem(String currentSemester) throws SQLException, ClassNotFoundException {
        System.out.print("Press 1 for changing Current Semester, Press 2 for changing State Of this semester ");
        int choose = Integer.parseInt(scanner.nextLine());
         boolean output;
        if(choose==1){
            System.out.print("Enter Name of New current Semester: ");
            String  newe = scanner.nextLine();
             output=Admin.change_currSem(currentSemester,newe);



        }
        else{
            System.out.print("Enter Name of New state of current Semester: ");
            System.out.print("Enter Either ST(setup) or EN(enrolling) or GS(Grade Submission) : ");
            String  newe = scanner.nextLine();
            output=Admin.change_currState(currentSemester,newe);
        }
        if(output)   System.out.println("Action Done");
        else  System.out.println("Action  Not Done");


    }

    public void view_grades() throws SQLException, ClassNotFoundException {
        System.out.println("Enter mail of student");
        String mail = scanner.nextLine();
        Admin.view_grades(mail);
    }
    public void Transcript() throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Enter mail of student");
        String mail = scanner.nextLine();
        Admin.Transcript(mail);
    }
    public void Graduation_check() throws SQLException, ClassNotFoundException {
        System.out.println("Enter mail of student");
        String mail = scanner.nextLine();
       if(Admin.Graduation_check(mail)) System.out.println("Eligible");
       else System.out.println("not eligible");
    }
    public void changeContactInfo(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Contact Number(Digits only)");
        String NewContact = scanner.nextLine();
        if(Admin.changeContactInfo(mail,NewContact)){ System.out.println("Contact Updated");}
        else{System.out.println("Contact Not Updated"); }

    }
    public void changePassword(String mail) throws SQLException, ClassNotFoundException {
        System.out.print("Enter New Password");
        String NewPassword = scanner.nextLine();
        if(Admin.changePassword(mail,NewPassword)){ System.out.println("Password Updated");}
        else{System.out.println("Password Not Updated"); }
    }


}
