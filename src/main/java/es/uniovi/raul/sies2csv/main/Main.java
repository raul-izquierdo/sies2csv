package es.uniovi.raul.sies2csv.main;

import java.io.IOException;
import java.nio.file.Path;
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

    private static final int OK = 0;
    private static final int ERROR = 1;

    public static void main(String[] args) {

        var parseResult = ArgumentsParser.parse(args);
        Optional<Arguments> argumentsOpt = parseResult.arguments();

        if (argumentsOpt.isEmpty())
            System.exit(parseResult.exitCode());

        int exitCode;
        try {

            loadAndRun(argumentsOpt.get());
            exitCode = OK;

        } catch (Exception e) {
            System.err.printf("%n[Error] %s%n", e);
            exitCode = ERROR;
        }

        System.exit(exitCode);
    }

    private static void loadAndRun(Arguments arguments)
            throws IOException, InvalidStudentFormatException, InvalidGroupFormatException {

        // load...
        List<Student> allStudents = loadStudents(Path.of(arguments.studentsFile));
        var teacherGroupsOpt = findGroup(arguments.groupsFile);

        // ... and run (now you understand the name of the method)
        Core.run(allStudents, teacherGroupsOpt, Path.of(arguments.outputFile));

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
     * If provided, loads the teacher's groups from the specified file. If not provided, returns an empty Optional.
     *
     * @param groupsFileName The path to the groups file.
     * @return An Optional containing the list of teacher's groups, or empty if a file was not provided.
     */
    private static Optional<List<String>> findGroup(String groupsFileName)
            throws IOException, InvalidGroupFormatException {

        if (groupsFileName == null || groupsFileName.isEmpty())
            return Optional.empty();

        var groups = GroupsLoader.loadTeacherGroups(Path.of(groupsFileName));

        // error si hay grupos duplicados
        if (hasDuplicates(groups))
            throw new InvalidGroupFormatException(
                    "[ERROR] The teacher's groups list contains duplicates. Please check the groups file.");

        // error si no hay grupos
        if (groups.isEmpty())
            throw new InvalidGroupFormatException(
                    "[ERROR] The teacher's groups list is empty. Please check the groups file.");

        return Optional.of(groups);
    }

    private static boolean hasDuplicates(List<String> groups) {
        return groups.size() != groups.stream().distinct().count();
    }

}
