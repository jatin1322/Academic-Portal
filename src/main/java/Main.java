import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        startPage Login = new startPage();
        String email = Login.getMail();
        String password = Login.getPassword();
        String role = Login.login(email, password);
        while (role.equals("invalid")) {
            System.out.println("Wrong ID or password");
            email = Login.getMail();
            password = Login.getPassword();
            role = Login.login(email, password);
        }
        System.out.println("Login Successful");

        // States  :: setup->enrolling->grade_submission->setup

        String currentSemester = Login.getCurrSemOrCurrState(1);
        String currentState = Login.getCurrSemOrCurrState(2);
        System.out.println("Current Semester is " + currentSemester);
        System.out.println("Current State is " + currentState);

        if (role.equals("student")) {
            student_UI ui = new student_UI();
            int option = ui.student_ui();
            while (option != 8) {
                if (option > 8 || option < 1) {
                    System.out.println("Invalid Input ");
                    System.out.println("  ");
                } else {

                    if (option == 1) {
                        if (currentState.equals("EN")) {
                            ui.register_course(currentSemester, email);
                        } else {
                            System.out.println("You can't Enroll as enrolling Window ended ");
                        }
                    } else if (option == 2) {
                        if (currentState.equals("EN")) {
                                  ui.deregister_course(currentSemester,email);
                        } else {
                            System.out.println("You can't Deregister as Deadline to Drop ended ");
                        }

                    } else if (option == 3) {
                        ui.view_grades(email);
                    } else if (option == 4) {
                        ui.CheckGraduation(email);

                    } else if (option == 5) {
                        ui.compute_CGPA(email);

                    } else if (option == 6) {
                        ui.changeContactInfo(email);
                    } else {
                        ui.changePassword(email);
                    }


                }
                option = ui.student_ui();
            }
            System.out.println("Logout From Student Page");


        } else if (role.equals("faculty")) {
            faculty_UI ui = new faculty_UI();
            int option = ui.faculty_ui();
            while (option != 8) {
                if (option > 8 || option < 1) {
                    System.out.println("Invalid Input ");
                    System.out.println("  ");
                } else {

                    if (option == 1) {
                        if (currentState.equals("EN")) {
                         ui.offer_course(email,currentSemester);
                        } else {
                            System.out.println("You cannot add course as per calendar");
                        }
                    } else if (option == 2) {
                        if (currentState.equals("EN")) {
                            ui.remove_course(email,currentSemester);
                        }
                           else {
                            System.out.println("You cannot Remove course as per calendar");
                        }
                    } else if (option == 3) {
                        ui.view_grades();
                    } else if (option == 4) {
                        if (currentState.equals("GS")) ui.grade_entry(email);
                        else {
                            System.out.println("You cannot perform Grade entry as per calendar");
                        }

                    } else if (option == 5) {
                        ui.download_classList(email);
                    } else if (option == 6) ui.changeContactInfo(email);
                    else {
                        ui.changePassword(email);
                    }
                }
                option = ui.faculty_ui();
            }
            System.out.println("Logout From Faculty Page");
        } else {
            admin_UI ui = new admin_UI();
            int option = ui.admin_ui();
            while (option != 10) {
                if (option > 10 || option < 1) {
                    System.out.println("Invalid Input ");
                    System.out.println("  ");
                } else {
                    if (option == 1) {
                        ui.add_course();

                    } else if (option == 2) {
                        ui.remove_course();


                    } else if (option == 3) {
                        ui.add_users();

                    }  else if (option == 4) {

                        ui.change_currSem(currentSemester);
                    } else if (option == 5) ui.view_grades();
                    else if (option == 6) {
                        ui.Transcript();
                    } else if (option == 7) ui.Graduation_check();
                    else if (option == 8) ui.changeContactInfo(email);

                    else {
                        ui.changePassword(email);
                    }
                }
                option = ui.admin_ui();
            }
            System.out.println("Logout From Admin Page");
        }
    }
}


