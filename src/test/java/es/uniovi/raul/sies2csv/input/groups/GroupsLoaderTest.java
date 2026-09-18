package es.uniovi.raul.sies2csv.input.groups;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.uniovi.raul.sies2csv.input.groups.GroupsLoader.InvalidGroupFormatException;

class GroupsLoaderTest {

    @Test
    void loadsSingleGroup() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("GroupA"));

        assertEquals(List.of("GroupA"), groups);
    }

    @Test
    void loadsMultipleGroups() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                GroupA
                GroupB
                GroupC"""));

        assertEquals(List.of("GroupA", "GroupB", "GroupC"), groups);
    }

    @Test
    void ignoresAdditionalColumns() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                GroupA,extra1,extra2
                GroupB,other"""));

        assertEquals(List.of("GroupA", "GroupB"), groups);
    }

    @Test
    void trimsSurroundingSpaces() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("  GroupA  \n GroupB "));

        assertEquals(List.of("GroupA", "GroupB"), groups);
    }

    @Test
    void quotedValueCanContainCommasAndSpaces() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                "Group, A"
                GroupB"""));

        // Note that "Group, A" is in quotes, so the comma is part of the value, not a separator.
        assertEquals(List.of("Group, A", "GroupB"), groups);
    }

    @Test
    void throwsWhenFileIsEmpty() {

        var exception = assertThrows(InvalidGroupFormatException.class,
                () -> GroupsLoader.loadTeacherGroups(new StringReader("")));

        assertTrue(exception.getMessage().contains("No groups found"));
    }

    @Test
    void skipsEmptyLines() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                GroupA

                GroupC"""));

        assertEquals(List.of("GroupA", "GroupC"), groups);
    }

    @Test
    void throwsWhenGroupColumnIsOnlyWhitespace() {

        var exception = assertThrows(InvalidGroupFormatException.class,
                () -> GroupsLoader.loadTeacherGroups(new StringReader("GroupA\n   \nGroupC")));

        assertTrue(exception.getMessage().contains("Line #2"));
        assertTrue(exception.getMessage().contains("cannot be blank"));
    }

    @Test
    void fileWithOnlyEmptyLinesIsTreatedAsNoGroupsFound() {

        var exception = assertThrows(InvalidGroupFormatException.class,
                () -> GroupsLoader.loadTeacherGroups(new StringReader("\n\n\n")));

        assertTrue(exception.getMessage().contains("No groups found"));
    }

    @Test
    void allowsSingleColumnGroupWithTrailingComma() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                GroupA,
                GroupB,"""));

        assertEquals(List.of("GroupA", "GroupB"), groups);
    }

    @Test
    void throwsWhenFirstColumnIsBlankButOthersAreNot() {

        var exception = assertThrows(InvalidGroupFormatException.class,
                () -> GroupsLoader.loadTeacherGroups(new StringReader(",extra")));

        assertTrue(exception.getMessage().contains("cannot be blank"));
    }

    @Test
    void handlesSingleTrailingNewline() throws Exception {

        var groups = GroupsLoader.loadTeacherGroups(new StringReader("""
                GroupA
                """));

        assertEquals(List.of("GroupA"), groups);
    }
}
