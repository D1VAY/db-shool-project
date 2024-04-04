package com.example.dbschoolproject.courses.dao;

import com.example.dbschoolproject.courses.daoInterface.GroupDao;
import com.example.dbschoolproject.courses.daoInterface.StudentDao;
import com.example.dbschoolproject.courses.domain.Group;
import com.example.dbschoolproject.courses.domain.Student;
import com.example.dbschoolproject.courses.utils.QueryExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostgresSQLGroupDaoTest {
    @BeforeEach
    void initiateDataBase() {
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
    void saveAll_shouldReturnAllSavedGroups_whenWePutThem() {
        GroupDao groupDao = new PostgresSQLGroupDao("src/test/resources/db.test.properties");
        StudentDao studentDao = new PostgresSqlStudentDao("src/test/resources/db.test.properties");

        List<Student> students = new ArrayList<Student>(4);
        students.add(new Student(1, "John", "Jones"));
        students.add(new Student(2, "Israel", "Adesanya"));
        students.add(new Student(3, "Stipe", "Miocic"));
        students.add(new Student(4, "Kamaru", "Usman"));

        List<Group> groups = new ArrayList<Group>(4);
        groups.add(new Group(1, "AA-11"));
        groups.add(new Group(2, "AA-12"));
        groups.add(new Group(3, "AB-22"));
        groups.add(new Group(4, "BC-45"));

        students.get(0).setGroup_id(1);
        students.get(1).setGroup_id(1);
        students.get(2).setGroup_id(1);
        students.get(3).setGroup_id(1);

        String expectedResult = "[Group [id=2, name=AA-12]\n" +
                ", Group [id=3, name=AB-22]\n" +
                ", Group [id=4, name=BC-45]\n" +
                "]";

        groupDao.saveAll(groups);

        studentDao.saveAll(students);

        List<Group> savedGroups = groupDao.findByGroupId(2);

        assertEquals(expectedResult, savedGroups.toString());
    }

    @Test
    void findByGroupId_shouldReturnTrue_whenAskingId() {
        GroupDao groupDao = new PostgresSQLGroupDao("src/test/resources/db.test.properties");

        List<Group> groups = new ArrayList<>(4);
        groups.add(new Group(0, "qU-11"));
        groups.add(new Group(1, "fp-34"));
        groups.add(new Group(2, "ut-04"));
        groups.add(new Group(3, "lK-62"));

        groupDao.saveAll(groups);

        List<Group> savedGroups = groupDao.findByGroupId(2);

        assertTrue(savedGroups.size() == 4);
        assertTrue(savedGroups.stream()
                .filter(group -> {
                    return group.getId() == 2;
                })
                .count() == 1);

    }

    @Test
    void findAll_shouldReturnAllInformationAboutGroups_whenWePutThem() {
        GroupDao groupDao = new PostgresSQLGroupDao("src/test/resources/db.test.properties");

        List<Group> groups = new ArrayList<>(4);
        groups.add(new Group(0, "qU-11"));
        groups.add(new Group(1, "fp-34"));
        groups.add(new Group(2, "ut-04"));
        groups.add(new Group(3, "lK-62"));

        groupDao.saveAll(groups);

        List<Group> savedGroups = groupDao.findAll();

        String expected = "[Group [id=0, name=qU-11]\n" +
                ", Group [id=1, name=fp-34]\n" +
                ", Group [id=2, name=ut-04]\n" +
                ", Group [id=3, name=lK-62]\n" +
                "]";
        assertEquals(expected, savedGroups.toString());

    }
}