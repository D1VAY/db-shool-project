package com.example.dbschoolproject.courses.dao;

import com.example.dbschoolproject.courses.daoInterface.CourseDao;
import com.example.dbschoolproject.courses.daoInterface.StudentDao;
import com.example.dbschoolproject.courses.domain.Course;
import com.example.dbschoolproject.courses.domain.Student;
import com.example.dbschoolproject.courses.utils.QueryExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostgresSqlStudentDaoTest {
    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor queryExecutor = new QueryExecutor("src/test/resources/db.test.properties");
        QueryExecutor.createDb("coursestest");
        ConnectionFactory connectionFactory = new ConnectionFactory("src/test/resources/db.test.properties");
        QueryExecutor.createTables();
    }
    @AfterEach
    public void dropDatabaseAfterTests() {
        QueryExecutor.dropDataBase("coursestest");
    }
    @Test
    void findAll_shouldFindByCourseName_whenWePutAStudents() {
        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");
        CourseDao courseDao = new PostgresSQLCourseDao("src/test/resources/db.test.properties");

        List<Course> courses = new ArrayList<>(4);
        courses.add(new Course(1, "Math", "Mathematics"));
        courses.add(new Course(2, "Biology", "Anatomy, biophysics, cell and molecular biology, computational biology"));

        List<Student> students = new ArrayList<Student>(4);

        students.add(new Student(1, "John", "Jones"));
        students.add(new Student(2, "Israel", "Adesanya"));
        students.add(new Student(3, "Stipe", "Miocic"));
        students.add(new Student(4, "Kamaru", "Usman"));

        studentDao.saveAll(students);

        courseDao.saveAll(courses);

        studentDao.assignToCourse(1, 1);
        studentDao.assignToCourse(1, 2);
        studentDao.assignToCourse(3, 2);
        studentDao.assignToCourse(4, 2);

        List<Student> savedStudents = studentDao.findAll();
        String expected = "[Student [id=1, group_id=0, firstName=John, lastName=Jones]\n" +
                ", Student [id=2, group_id=0, firstName=Israel, lastName=Adesanya]\n" +
                ", Student [id=3, group_id=0, firstName=Stipe, lastName=Miocic]\n" +
                ", Student [id=4, group_id=0, firstName=Kamaru, lastName=Usman]\n" +
                "]";
        assertEquals(expected, savedStudents.toString());
    }

    @Test
    void saveAll_shouldSaveStudents_whenWePutThem() {
        List<Student> students = new ArrayList<>(4);

        students.add(new Student(1, "John", "Jones"));
        students.add(new Student(2, "Israel", "Adesanya"));
        students.add(new Student(3, "Stipe", "Miocic"));
        students.add(new Student(4, "Kamaru", "Usman"));

        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");

        studentDao.saveAll(students);

        List<Student> savedStudents = studentDao.findAll();

        String expected = "[Student [id=1, group_id=0, firstName=John, lastName=Jones]\n" +
                ", Student [id=2, group_id=0, firstName=Israel, lastName=Adesanya]\n" +
                ", Student [id=3, group_id=0, firstName=Stipe, lastName=Miocic]\n" +
                ", Student [id=4, group_id=0, firstName=Kamaru, lastName=Usman]\n" +
                "]";

        assertEquals(expected, savedStudents.toString());
    }

    @Test
    void delete_shouldDeleteStudent_whenPutAStudentId() {
        List<Student> students = new ArrayList<>(3);

        students.add(new Student(1, "John", "Jones"));
        students.add(new Student(2, "Israel", "Adesanya"));
        students.add(new Student(3, "Stipe", "Miocic"));

        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");

        studentDao.save(students.get(0));
        studentDao.save(students.get(1));
        studentDao.save(students.get(2));

        studentDao.delete(2);

        List<Student> savedStudents = studentDao.findAll();

        String expectedResult = "[Student [id=1, group_id=0, firstName=John, lastName=Jones]\n" +
                ", Student [id=3, group_id=0, firstName=Stipe, lastName=Miocic]\n" +
                "]";

        assertEquals(expectedResult, savedStudents.toString());
    }
}