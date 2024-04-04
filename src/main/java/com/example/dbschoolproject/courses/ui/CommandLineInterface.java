package com.example.dbschoolproject.courses.ui;

import com.example.dbschoolproject.courses.daoInterface.GroupDao;
import com.example.dbschoolproject.courses.dao.PostgresSQLGroupDao;
import com.example.dbschoolproject.courses.dao.PostgresSqlStudentDao;
import com.example.dbschoolproject.courses.daoInterface.StudentDao;
import com.example.dbschoolproject.courses.domain.Student;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Scanner;
import java.util.logging.Logger;

public class CommandLineInterface {

    private static final String ARG_STUDENTS_NUMBER_NAME = "COUNT";
    private static final String ARG_STUDENTS_COURSE_NAME = "COURSE_NAME";
    private static final String ARG_STUDENT_FIRST_NAME = "FIRSTNAME";
    private static final String ARG_STUDENT_LAST_NAME = "LASTNAME";
    private static final String ARG_STUDENT_ID = "STUDENT_ID";
    private static final String ARG_COURSE_ID = "COURSE_ID";
    private static final char ARGS_SEPARATOR = ',';
    private static final String ARGS_TWO_VALUES_FORMAT = "%s%s %s";

    private static final String OPT_FIND_GROUPS_SHORT = "g";
    private static final String OPT_FIND_GROUPS_LONG = "find-groups";
    private static final String OPT_FIND_GROUPS_DESC = "find all groups with less or equals student count";

    private static final String OPT_FIND_STUDENTS_BY_COURSE_NAME_SHORT = "s";
    private static final String OPT_FIND_STUDENTS_BY_COURSE_NAME_LONG = "find-students";
    private static final String OPT_FIND_STUDENTS_BY_COURSE_NAME_DESC = "find all students related to course with given name";

    private static final String OPT_ADD_NEW_STUDENT_SHORT = "n";
    private static final String OPT_ADD_NEW_STUDENT_LONG = "new-student";
    private static final String OPT_ADD_NEW_STUDENT_DESC = "add new student";

    private static final String OPT_DELETE_STUDENT_BY_ID_SHORT = "d";
    private static final String OPT_DELETE_STUDENT_BY_ID_LONG = "delete-student";
    private static final String OPT_DELETE_STUDENT_BY_ID_DESC = "delete student by STUDENT_ID";

    private static final String OPT_ADD_STUDENT_TO_COURSE_SHORT = "a";
    private static final String OPT_ADD_STUDENT_TO_COURSE_LONG = "assign-to-course";
    private static final String OPT_ADD_STUDENT_TO_COURSE_DESC = "add a student to the course";

    private static final String OPT_REMOVE_STUDENT_FROM_COURSE_SHORT = "r";
    private static final String OPT_REMOVE_STUDENT_FROM_COURSE_LONG = "remove-from-course";
    private static final String OPT_REMOVE_STUDENT_FROM_COURSE_DESC = "remove the student from one of his or her courses";

    private static final String OPT_QUIT = "q";
    private static final String OPT_QUIT_LONG = "quit";
    private static final String OPT_QUIT_DESC = "exit from application";

    private static final String CLI_WARNING_WRONG_ID = "Error, wrong IDs entered!";

    private static final Option OPT_FIND_GROUPS = Option.builder(OPT_FIND_GROUPS_SHORT)
            .longOpt(OPT_FIND_GROUPS_LONG)
            .desc(OPT_FIND_GROUPS_DESC)
            .hasArg()
            .argName(ARG_STUDENTS_NUMBER_NAME)
            .build();

    private static final Option OPT_FIND_STUDENTS_BY_COURSE_NAME = Option
            .builder(OPT_FIND_STUDENTS_BY_COURSE_NAME_SHORT)
            .longOpt(OPT_FIND_STUDENTS_BY_COURSE_NAME_LONG)
            .desc(OPT_FIND_STUDENTS_BY_COURSE_NAME_DESC)
            .hasArg()
            .argName(ARG_STUDENTS_COURSE_NAME)
            .build();

    private static final Option OPT_ADD_NEW_STUDENT = Option.builder(OPT_ADD_NEW_STUDENT_SHORT)
            .longOpt(OPT_ADD_NEW_STUDENT_LONG)
            .desc(OPT_ADD_NEW_STUDENT_DESC)
            .hasArg()
            .numberOfArgs(2)
            .valueSeparator(ARGS_SEPARATOR)
            .argName(String.format(ARGS_TWO_VALUES_FORMAT, ARG_STUDENT_FIRST_NAME, ARGS_SEPARATOR,
                    ARG_STUDENT_LAST_NAME))
            .build();

    private static final Option OPT_DELETE_STUDENT_BY_ID = Option.builder(OPT_DELETE_STUDENT_BY_ID_SHORT)
            .longOpt(OPT_DELETE_STUDENT_BY_ID_LONG)
            .desc(OPT_DELETE_STUDENT_BY_ID_DESC)
            .hasArg()
            .argName(ARG_STUDENT_FIRST_NAME)
            .build();

    private static final Option OPT_ADD_STUDENT_TO_COURSE = Option.builder(OPT_ADD_STUDENT_TO_COURSE_SHORT)
            .longOpt(OPT_ADD_STUDENT_TO_COURSE_LONG)
            .desc(OPT_ADD_STUDENT_TO_COURSE_DESC)
            .hasArg()
            .numberOfArgs(2)
            .valueSeparator(ARGS_SEPARATOR)
            .argName(String.format(ARGS_TWO_VALUES_FORMAT, ARG_STUDENT_ID, ARGS_SEPARATOR, ARG_COURSE_ID))
            .build();

    private static final Option OPT_REMOVE_STUDENT_FROM_COURSE = Option.builder(OPT_REMOVE_STUDENT_FROM_COURSE_SHORT)
            .longOpt(OPT_REMOVE_STUDENT_FROM_COURSE_LONG)
            .desc(OPT_REMOVE_STUDENT_FROM_COURSE_DESC)
            .hasArg()
            .numberOfArgs(2)
            .valueSeparator(ARGS_SEPARATOR)
            .argName(String.format(ARGS_TWO_VALUES_FORMAT, ARG_STUDENT_ID, ARGS_SEPARATOR, ARG_COURSE_ID))
            .build();

    private static final Option quitFromCLI = Option.builder(OPT_QUIT)
            .longOpt(OPT_QUIT_LONG)
            .desc(OPT_QUIT_DESC)
            .build();

    private Scanner scanner;
    private Options options;
    private GroupDao groupDao;
    private StudentDao studentDao;

    private final static Logger logger = Logger.getLogger(String.valueOf(CommandLineInterface.class));

    public CommandLineInterface() {

        this.scanner = new Scanner(System.in);
        this.options = createOptions();
        this.groupDao = new PostgresSQLGroupDao();
        this.studentDao = new PostgresSqlStudentDao();
    }


    public void runCommandLineInterface() {

        boolean isExit = false;

        printHelp();

        do {
            String arg = scanner.nextLine().trim();
            isExit = executeCommand(new String[] { arg });
        } while (!isExit);

        scanner.close();

    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("courses API", options);
    }

    private void addNewStudent(String firstName, String lastName) {

        Student newStudent = new Student(0, firstName, lastName);
        studentDao.save(newStudent);
    }

    private void deleteStudentById(int studentId) {
        studentDao.delete(studentId);
    }

    private void addStudentToCourse(int studentId, int courseId) {

        if (studentId > 0 && courseId > 0) {
            studentDao.assignToCourse(studentId, courseId);
        } else {
            logger.warning(CLI_WARNING_WRONG_ID);
        }
    }

    private void removeStudentCourse(int studentId, int courseId) {

        if (studentId > 0 && courseId > 0) {
            studentDao.deleteFromCourse(studentId, courseId);
        } else {
            logger.warning(CLI_WARNING_WRONG_ID);
        }

    }

    public boolean executeCommand(String[] args) {

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(OPT_QUIT)) {
                return true;
            } else if (line.hasOption(OPT_ADD_NEW_STUDENT_SHORT)) {
                String[] studentName = line.getOptionValues(OPT_ADD_NEW_STUDENT_SHORT);
                addNewStudent(studentName[0], studentName[1]);
            } else if (line.hasOption(OPT_DELETE_STUDENT_BY_ID_SHORT)) {
                deleteStudentById(parseSingleIntValue(line, OPT_DELETE_STUDENT_BY_ID_SHORT));
            } else if (line.hasOption(OPT_ADD_STUDENT_TO_COURSE_SHORT)) {
                int[] ids = parseIntValues(line, OPT_ADD_STUDENT_TO_COURSE_SHORT);
                addStudentToCourse(ids[0], ids[1]);
            } else if (line.hasOption(OPT_REMOVE_STUDENT_FROM_COURSE_SHORT)) {
                int[] ids = parseIntValues(line, OPT_ADD_STUDENT_TO_COURSE_SHORT);
                removeStudentCourse(ids[0], ids[1]);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private static int parseSingleIntValue(CommandLine line, String optionName)
            throws NumberFormatException, ParseException {
        return Integer.parseInt(line.getOptionValue(optionName).toString().trim());
    }

    private static int[] parseIntValues(CommandLine line, String optionName)
            throws NumberFormatException, ParseException {

        String[] stringValues = line.getOptionValues(OPT_ADD_STUDENT_TO_COURSE_SHORT);
        int[] intValues = new int[stringValues.length];

        for (int i = 0; i < stringValues.length; i++) {
            intValues[i] = Integer.parseInt(stringValues[i].trim());
        }

        return intValues;
    }

    private static String parseSingleStringValue(CommandLine line, String optionName)
            throws NumberFormatException, ParseException {
        return line.getOptionValue(optionName).toString().trim();
    }

    private static Options createOptions() {

        Options options = new Options();

        options.addOption(OPT_FIND_GROUPS);
        options.addOption(OPT_FIND_STUDENTS_BY_COURSE_NAME);
        options.addOption(OPT_ADD_NEW_STUDENT);
        options.addOption(OPT_DELETE_STUDENT_BY_ID);
        options.addOption(OPT_ADD_STUDENT_TO_COURSE);
        options.addOption(OPT_REMOVE_STUDENT_FROM_COURSE);
        options.addOption(quitFromCLI);

        return options;
    }

}