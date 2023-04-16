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
