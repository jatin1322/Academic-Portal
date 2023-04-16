Name Jatin                                                                  Entry No. 2020CSB1090
Assumptions:
1. Database Contain Info Regarding Program_core,Engineering_Elective,Open_elective Courses, Also Database Contain information of all the students who have passed BTP in one table.
2. Three are States  EN - Enrolling, ST- Setup, GS - Grades Submission.


Database:
CREATE TABLE users (
                      email VARCHAR(50) UNIQUE NOT NULL,
                      password VARCHAR(50) NOT NULL,
                      name VARCHAR(50) ,
                      user_type VARCHAR(50) NOT NULL,
                      contact_info VARCHAR(50)
);


CREATE TABLE courses (
                        course_code VARCHAR(10) NOT NULL,
                        course_description VARCHAR(255),
                        credit_structure VARCHAR(20),
                        prerequisites VARCHAR(255),
                        credits real NOT NULL
);


CREATE TABLE course_Offerings (
                                 course_offering_id SERIAL PRIMARY KEY,
                                 course_code VARCHAR(10) ,
                                 faculty_mail VARCHAR(50),
                                 semester_name VARCHAR(20),
                                 CGPA_constraint real Default 0.00
);
CREATE TABLE enrollments (
                            student_mail VARCHAR(50),
                            course_offering_id INTEGER REFERENCES course_Offerings(course_offering_id),
                            grades INTEGER Default 0
);
CREATE TABLE currsem(
                       semester_name  VARCHAR(10) PRIMARY KEY,
                       curr_state  VARCHAR(2)
);




CREATE TABLE program_core (
                             course_code VARCHAR(6) PRIMARY KEY,
                             credits real NOT NULL
);


CREATE TABLE engineering_electives (
                                      course_code VARCHAR(6) PRIMARY KEY,
                                      credits real NOT NULL
);


CREATE TABLE open_electives (
                               course_code VARCHAR(6) PRIMARY KEY,
                               credits real NOT NULL
);


CREATE TABLE btp_students (
   student_mail VARCHAR(50) PRIMARY KEY
);


* The "users" table has a one-to-many relationship with the "course_offerings" table, as a user (faculty) can teach multiple course offerings.
* The "courses" table has a one-to-many relationship with the "course_offerings" table, as a course can have multiple offerings.
* The "course_offerings" table has a one-to-many relationship with the "enrollments" table, as a course offering can have multiple student enrollments.
* The "course_offerings" table has a many-to-one relationship with the "users" table, as a course offering can be taught by a single user (faculty).
* The "enrollments" table has a many-to-one relationship with the "course_offerings" table, as a single enrollment belongs to a single course offering.
* The "currsem" table has a one-to-one relationship with the "course_offerings" table, as a semester can have multiple course offerings but a single course offering can belong to only one semester.
* The "program_core", "engineering_electives", and "open_electives" tables are all standalone tables that do not have any relationships with other tables in the database.
* The "btp_students" table has a one-to-one relationship with the "users" table, as a B.Tech. project (BTP) student must be a registered user in the system.


Everything else is Shown on UI.
How to run and Compile : 
gradle clean build