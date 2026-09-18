package es.uniovi.raul.sies2csv.input.sies;

import java.util.*;

/**
 * Represents a student with their associated data extracted from the SIES excel file.
 *
 * The email is required because it is used as a unique identifier for the student.
 * The firstName is also required as a student without a firstName will be difficult to find in the assignments (the email is not sufficient for this purpose).
 *
 * The group is optional, as some students may not be assigned to any group. They will be allowed to recieve assignments, but they will not be able to see the solutions of the assignments, since they won't be part of any team.
 *
 * @param row the row in the excel file where the student data is located
 * @param email the student's email address
 * @param firstName the student's firstName
 * @param group the student's group
 */

public record Student(int row, String email, String firstName, Optional<String> group) {

    public Student {
        if (row < 1)
            throw new IllegalArgumentException("row must be greater than zero");

        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(firstName, "firstName cannot be null");
        Objects.requireNonNull(group, "group cannot be null");
    }

}
