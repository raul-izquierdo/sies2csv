package es.uniovi.raul.sies2csv.output;

import java.io.*;
import java.util.List;

import es.uniovi.raul.sies2csv.input.sies.Student;

/**
 * A class for generating CSV files from a list of students.
 */
public class CsvGenerator {

    public static void writeCsv(List<Student> students, String outputFilePath) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(outputFilePath)) {
            writeCsv(students, out);
        }
    }

    public static void writeCsv(List<Student> students, PrintStream out) {
        writeHeader(out);

        students.forEach(student -> writeStudent(student, out));
    }

    private static void writeHeader(PrintStream out) {
        out.println("email,first_name,section");
    }

    private static void writeStudent(Student student, PrintStream out) {
        String email = student.email();
        String name = student.firstName();
        String group = student.group().orElse("");

        // Always add quotes around the name to handle cases where the name contains commas
        name = "\"" + name + "\"";

        out.printf("%s,%s,%s%n", email, name, group);
    }
}
