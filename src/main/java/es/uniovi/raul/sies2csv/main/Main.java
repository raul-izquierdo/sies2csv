package es.uniovi.raul.sies2csv.main;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import es.uniovi.raul.sies2csv.cli.*;
import es.uniovi.raul.sies2csv.core.Core;
import es.uniovi.raul.sies2csv.input.groups.GroupsLoader;
import es.uniovi.raul.sies2csv.input.groups.GroupsLoader.InvalidGroupFormatException;
import es.uniovi.raul.sies2csv.input.sies.*;
import es.uniovi.raul.sies2csv.input.sies.SiesExcelLoader.InvalidStudentFormatException;

/**
 * Entry point for the app.
 */
public class Main {

    public static void main(String[] args) {

        Optional<Arguments> argumentsOpt = ArgumentsParser.parse(args);

        if (argumentsOpt.isEmpty())
            System.exit(Core.ERROR);

        int exitCode;
        try {

            exitCode = loadAndRun(argumentsOpt.get());

        } catch (Exception e) {
            System.err.printf("%n[Error] %s%n", e.getMessage());
            exitCode = Core.ERROR;
        }

        System.exit(exitCode);
    }

    private static int loadAndRun(Arguments arguments)
            throws IOException, InvalidStudentFormatException, InvalidGroupFormatException {

        // load...
        List<Student> allStudents = loadStudents(Path.of(arguments.studentsFile));
        var teacherGroupsOpt = loadTeacherGroups(Path.of(arguments.groupsFile));

        // ... and run (now you understand the name of the method)
        return Core.run(allStudents, teacherGroupsOpt, Path.of(arguments.outputFile));

    }

    private static List<Student> loadStudents(Path studentsFile) throws InvalidStudentFormatException, IOException {

        var issuesTracker = new IssuesCounter();
        List<Student> allStudents = SiesExcelLoader.loadStudents(studentsFile, issuesTracker);

        if (issuesTracker.hasIssues()) {
            System.out.println(">> Issues found when loading students:");
            System.out.printf("    %d students are missing email addresses.%n", issuesTracker.getMissingEmailCount());
            System.out.printf("    %d students are missing names.%n", issuesTracker.getMissingNameCount());
        }
        return allStudents;
    }

    /**
     * Loads the teacher's groups from a file.
     *
     * @param groupsFile The path to the groups file.
     * @return An Optional containing the list of teacher's groups, or empty if the file is not found.
     */
    private static Optional<List<String>> loadTeacherGroups(Path groupsFile)
            throws IOException, InvalidGroupFormatException {

        Optional<List<String>> teacherGroupsOpt;
        try {
            var groups = GroupsLoader.loadTeacherGroups(groupsFile);

            // error si hay grupos duplicados
            if (hasDuplicates(groups))
                throw new InvalidGroupFormatException(
                        "[ERROR] The teacher's groups list contains duplicates. Please check the groups file.");

            // error si no hay grupos
            if (groups.isEmpty())
                throw new InvalidGroupFormatException(
                        "[ERROR] The teacher's groups list is empty. Please check the groups file.");

            teacherGroupsOpt = Optional.of(groups);

        } catch (NoSuchFileException e) {
            teacherGroupsOpt = Optional.empty();
        }
        return teacherGroupsOpt;
    }

    private static boolean hasDuplicates(List<String> groups) {
        return groups.size() != groups.stream().distinct().count();
    }

}
