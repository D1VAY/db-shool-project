package com.example.dbschoolproject.courses.application;

import com.example.dbschoolproject.courses.dao.PostgresSQLCourseDao;
import com.example.dbschoolproject.courses.dao.PostgresSQLGroupDao;
import com.example.dbschoolproject.courses.dao.PostgresSqlStudentDao;
import com.example.dbschoolproject.courses.daoInterface.CourseDao;
import com.example.dbschoolproject.courses.daoInterface.GroupDao;
import com.example.dbschoolproject.courses.daoInterface.StudentDao;
import com.example.dbschoolproject.courses.domain.Course;
import com.example.dbschoolproject.courses.domain.Group;
import com.example.dbschoolproject.courses.domain.Student;
import com.example.dbschoolproject.courses.ui.CommandLineInterface;
import com.example.dbschoolproject.courses.utils.DataGenerator;
import com.example.dbschoolproject.courses.utils.QueryExecutor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        initiateDB();
        generateData();
        runUserInterface();
    }

    public static void initiateDB() {
        String nameDb = "courses";
        QueryExecutor.createDb(nameDb);
        QueryExecutor.createTables();
    }

    public static void generateData() {
        DataGenerator dataGenerator = new DataGenerator();
        List<Group> groups = dataGenerator.generateGroup(10);
        List<Course> courses = dataGenerator.generateCourse(10);
        List<Student> students = dataGenerator.generateStudents(200);


        dataGenerator.assignStudentsToGroups(groups, students, 10, 30);
        dataGenerator.assignCoursesToStudents(courses, students, 1, 3);

        GroupDao groupDao = new PostgresSQLGroupDao();
        groupDao.saveAll(groups);

        CourseDao courseDao = new PostgresSQLCourseDao();
        courseDao.saveAll(courses);

        StudentDao studentDao = new PostgresSqlStudentDao();
        studentDao.saveAll(students);
    }

    private static void runUserInterface() {
        CommandLineInterface userInterface = new CommandLineInterface();
        userInterface.runCommandLineInterface();
    }

}
