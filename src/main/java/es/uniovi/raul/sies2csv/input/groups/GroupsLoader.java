package es.uniovi.raul.sies2csv.input.groups;

import static java.lang.String.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.csv.*;

/**
 * Loads the groups that are assigned to the teacher.
 *
 * The csv file is expected to have one group per line, with no header.
 * The group name should be in the first column (column index 0). It may contain additional columns, but they will be ignored. Blank lines are not allowed.
 */
public class GroupsLoader {

    private static final int GROUP_COLUMN = 0;

    public static List<String> loadTeacherGroups(Path filePath) throws IOException, InvalidGroupFormatException {

        try (var reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            return loadTeacherGroups(reader);

        } catch (InvalidGroupFormatException e) {
            throw new InvalidGroupFormatException(format("'%s'. %s", filePath, e.getMessage()));
        }
    }

    public static List<String> loadTeacherGroups(Reader reader) throws IOException, InvalidGroupFormatException {

        try (CSVParser parser = new CSVParser(reader,
                CSVFormat.DEFAULT.builder()
                        .setTrim(true)
                        .setIgnoreSurroundingSpaces(true)
                        .setSkipHeaderRecord(false) // No header, just a list of groups
                        .build())) {

            List<String> groups = new ArrayList<>();

            for (CSVRecord csvRecord : parser)
                groups.add(getValue(csvRecord, GROUP_COLUMN, parser.getCurrentLineNumber()));

            if (groups.isEmpty())
                throw new InvalidGroupFormatException("No groups found in the file. Please check the content.");

            return groups;
        }
    }

    // Used when the column is mandatory. Throws an exception if the value is blank.
    private static String getValue(CSVRecord csvRecord, int column, long currentLineNumber)
            throws InvalidGroupFormatException {

        return findValue(csvRecord, column)
                .orElseThrow(() -> new InvalidGroupFormatException(
                        format("Line #%d: '%s' -> column '%d' (zero based) cannot be blank",
                                currentLineNumber, join(", ", csvRecord), column)));
    }

    // Used with optional columns. If the value is blank, returns an empty Optional.
    private static Optional<String> findValue(CSVRecord csvRecord, int columnNumber) {
        try {
            String value = csvRecord.get(columnNumber);

            if (value == null || value.isBlank())
                return Optional.empty();

            return Optional.of(value);

        } catch (ArrayIndexOutOfBoundsException e) { // column not found
            return Optional.empty();
        }
    }

    /**
     * Exception thrown when the groups file is not in the expected format.
     */
    public static class InvalidGroupFormatException extends Exception {
        public InvalidGroupFormatException(String message) {
            super(message);
        }
    }

}
