package es.uniovi.raul.sies2csv.core;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import es.uniovi.raul.sies2csv.input.sies.Student;
import es.uniovi.raul.sies2csv.output.CsvGenerator;

/**
 * Core class that handles the main logic of the application, after the Main class deals with the command line arguments and file loading.
 */
public class Core {

    public static void run(List<Student> students, Optional<List<String>> teacherGroups, Path outputFile)
            throws FileNotFoundException {

        try (PrintStream out = new PrintStream(outputFile.toFile())) {

            run(students, teacherGroups, out);
            System.out.printf("%n'%s' file created successfully!!%n%n", outputFile);

        }
    }

    /**
     * Runs the core logic of the application.
     */
    public static void run(List<Student> allStudents, Optional<List<String>> teacherGroups, PrintStream output) {

        // Precondition check
        if (teacherGroups.isPresent() && teacherGroups.get().isEmpty())
            throw new IllegalArgumentException("The teacher's groups list is empty. Please check the groups file.");

        reportStudentStatistics(allStudents);

        if (teacherGroups.isPresent()) {
            reportTeacherGroupStatistics(teacherGroups.get(), allStudents);

            CsvGenerator.writeCsv(filterStudentsByGroup(allStudents, teacherGroups.get()), output);

        } else {
            CsvGenerator.writeCsv(allStudents, output);
        }

    }

    // Filters the list of students to include only those whose group is in the teacher's groups list.
    private static List<Student> filterStudentsByGroup(List<Student> allStudents, List<String> teacherGroups) {

        return allStudents.stream()
                .filter(student -> student.group().isPresent() && teacherGroups.contains(student.group().get()))
                .toList();
    }

    private static void reportStudentStatistics(List<Student> allStudents) {

        System.out.println("\n## Students Statistics");

        // 1. Print how many students have been read from the students file
        System.out.printf(">> Total students read: %d%n", allStudents.size());

        // 2. Print how many students doesn't have a group
        long studentsWithoutGroup = allStudents.stream()
                .filter(student -> student.group().isEmpty())
                .count();
        System.out.printf(">> Students without a group: %d (%d%%).%n%n", studentsWithoutGroup,
                Math.round((double) studentsWithoutGroup / allStudents.size() * 100));
    }

    private static void reportTeacherGroupStatistics(List<String> teacherGroups, List<Student> allStudents) {

        System.out.println("## Teacher's Groups Statistics");

        // 1. Print the group found in the students list that are not in the teacher's groups list
        List<String> nontTeacherGroups = allStudents.stream()
                .filter(student -> student.group().isPresent())
                .map(student -> student.group().get())
                .distinct()
                .filter(group -> !teacherGroups.contains(group))
                .sorted()
                .toList();
        System.out.printf(">> Groups that do not belong to the teacher: %s.%n",
                String.join(", ", nontTeacherGroups));

        // 2. Print one line per teacher's group, showing how many students belong to that group
        System.out.println(">> Teacher Groups:");
        teacherGroups.forEach(group -> {
            long count = allStudents.stream()
                    .filter(student -> student.group().isPresent() && student.group().get().equals(group))
                    .count();
            String warning = count == 0 ? " [WARNING] No students in this group!!" : "";
            System.out.printf("\t'%s': %d students. %s%n", group, count, warning);
        });

        // 3. Print the total number of students that belong to the teacher's groups
        var teacherStudents = filterStudentsByGroup(allStudents, teacherGroups);
        System.out.printf(">> Total students in teacher's groups: %d%n", teacherStudents.size());
    }

}
