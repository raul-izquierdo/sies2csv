package es.uniovi.raul.sies2csv.cli;

import java.io.*;
import java.util.Optional;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

/** Parses and validates command line arguments. */
public class ArgumentsParser {

    public record ParseResult(Optional<Arguments> arguments, int exitCode) {
    }

    private static final String DEFAULT_SCHEDULE_FILE = "groups.csv";

    /**
     * Parses command line args.
     * Prints usage, version, or errors as needed.
     *
     * @param args the command line arguments
     * @return the parsed arguments, if present, and the appropriate process exit code
     */
    public static ParseResult parse(String[] args) {
        return parse(args, System.out, System.err);
    }

    public static ParseResult parse(String[] args, PrintStream out, PrintStream err) {

        final Arguments arguments = new Arguments();

        final CommandLine picocli = new CommandLine(arguments)
                .setCaseInsensitiveEnumValuesAllowed(true)
                // .setColorScheme(CommandLine.Help.defaultColorScheme(Help.Ansi.ON))
                .setSeparator(" "); // Use space (`-g file`) instead of "=" (`-g=file`);

        try {
            picocli.parseArgs(args);

            if (picocli.isUsageHelpRequested()) {
                picocli.usage(out);
                return new ParseResult(Optional.empty(), 0);
            }

            if (picocli.isVersionHelpRequested()) {
                picocli.printVersionHelp(out);
                return new ParseResult(Optional.empty(), 0);
            }

            arguments.groupsFile = resolveScheduleFile(arguments.groupsFile);

            return new ParseResult(Optional.of(arguments), 0);

        } catch (ParameterException ex) {
            System.err.printf("%n[Error] %s%n", ex.getMessage());
            picocli.usage(err);
            return new ParseResult(Optional.empty(), 1);
        }
    }

    private static String resolveScheduleFile(String scheduleFile) {

        // If provided, use it
        if (scheduleFile != null)
            return scheduleFile;

        // If not provided, check if the default file exists
        File file = new File(DEFAULT_SCHEDULE_FILE);
        if (file.exists() && file.isFile()) {
            System.out.printf(">> Default schedule file '%s' found.%n", DEFAULT_SCHEDULE_FILE);
            return DEFAULT_SCHEDULE_FILE;
        }

        return null; // No schedule file provided and default does not exist
    }

}
