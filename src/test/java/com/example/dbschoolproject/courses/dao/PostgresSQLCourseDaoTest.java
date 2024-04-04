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

import static org.junit.jupiter.api.Assertions.*;

class PostgresSQLCourseDaoTest {
    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor queryExecutor = new QueryExecutor("src/test/resources/db.test.properties");
        QueryExecutor.createDb("coursestest");
        ConnectionFactory connectionFactory = new ConnectionFactory("src/test/resources/db.test.properties");
        QueryExecutor.createTables();
    }
    @AfterEach
    public void dropDataBaseAfterTest() {
        QueryExecutor.dropDataBase("coursestest");
    }

    @Test
    void saveAll_shouldReturnAllSavedCourses_whenWePutThem() {
        CourseDao courseDao = new PostgresSQLCourseDao("src/test/resources/db.test.properties");

        List<Course> courses = new ArrayList<Course>(4);
        courses.add(new Course(0, "Math", "Mathematics"));
        courses.add(new Course(1, "Biology", "Anatomy, biophysics, cell and molecular biology, computational biology"));
        courses.add(new Course(2, "Literature", "Main subjects of courses in literature include: cultural and literature, "
                + "topics in literary research, reading fiction, poetry, modern drama, classical literature, women's literature. "));
        courses.add(new Course(3, "Physics",
                "Challenge because many a concept in Physics are challenging and can strain your cognitive tissues"));

        courseDao.saveAll(courses);

        List<Course> savedCourses = courseDao.findAll();

        String expectedResult = "[Course [id=0, name=Math, description=Mathematics, students=[]]\n" +
                ", Course [id=1, name=Biology, description=Anatomy, biophysics, cell and molecular biology, computational biology, students=[]]\n" +
                ", Course [id=2, name=Literature, description=Main subjects of courses in literature include: cultural and literature, topics in literary research, reading fiction, poetry, modern drama, classical literature, women's literature. , students=[]]\n" +
                ", Course [id=3, name=Physics, description=Challenge because many a concept in Physics are challenging and can strain your cognitive tissues, students=[]]\n" +
                "]";
        assertEquals(expectedResult, savedCourses.toString());
    }

    @Test
    void findByStudentId_shouldReturnTrue_whenAskingId() {
        CourseDao courseDao = new PostgresSQLCourseDao("src/test/resources/db.test.properties");
        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");

        List<Student> students = new ArrayList<>(1);
        Student student = new Student(0, "Tom", "Tomson");
        students.add(student);

        List<Course> courses = new ArrayList<>(4);
        courses.add(new Course(0, "Math", "Mathematics"));
        courses.add(new Course(1, "Biology", "Anatomy, biophysics, cell and molecular biology, computational biology"));
        courses.add(new Course(2, "Literature", "Main subjects of courses in literature include: "
                + "cultural and literature, topics in literary research, reading fiction, poetry, modern drama, classical literature, women's literature. "));
        courses.add(new Course(3, "Physics",
                "Challenge because many a concept in Physics are challenging and can strain your cognitive tissues"));

        courses.get(0).assignStudent(student);
        courses.get(2).assignStudent(student);

        courseDao.saveAll(courses);
        studentDao.saveAll(students);

        List<Course> saveCourses = courseDao.findByStudentId(student.getId());

        assertTrue(saveCourses.size() == 2);
        assertTrue(saveCourses.stream()
                .filter(course -> {
                    return course.getId() == 0;
                })
                .count() == 1);
        assertTrue(saveCourses.stream()
                .filter(course -> {
                    return course.getId() == 2;
                })
                .count() == 1);
    }

    @Test
    void findAll_shouldReturnAllInformationAboutCourse_whenWePutThem() {
        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");
        CourseDao courseDao = new PostgresSQLCourseDao("src/test/resources/db.test.properties");

        List<Course> courses = new ArrayList<>(2);
        courses.add(new Course(1, "Math", "Mathematics"));
        courses.add(new Course(2, "Biology", "Anatomy, biophysics, cell and molecular biology, computational biology"));

        List<Student> students = new ArrayList<>(4);
        students.add(new Student(1, "John", "Jones"));
        students.add(new Student(2, "Israel", "Adesanya"));
        students.add(new Student(3, "Stipe", "Miocic"));
        students.add(new Student(4, "Kamaru", "Usman"));

        studentDao.saveAll(students);
        courseDao.saveAll(courses);

        List<Course> savedCourse = courseDao.findAll();
        String expected = "[Course [id=1, name=Math, description=Mathematics, students=[]]\n" +
                ", Course [id=2, name=Biology, description=Anatomy, biophysics, cell and molecular biology, computational biology, students=[]]\n" +
                "]";
        assertEquals(expected, savedCourse.toString());
    }
}