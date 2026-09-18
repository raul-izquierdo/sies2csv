package es.uniovi.raul.sies2csv.input.sies;

class SiesExcelLoaderTest {
    /*
    private static final Path NO_SIES_FILE = Path.of("src/test/java/es/uniovi/raul/sies2csv/sies/NoSiesFile.xls");
    private static final Path SIES_FILE = Path.of("src/test/java/es/uniovi/raul/sies2csv/sies/testFile.xls");
    
    @Test
    void rejectsNotSiesExcel() {
        var issuesTracker = new IssuesCounter();
        assertThrows(InvalidStudentFormatException.class,
                () -> SiesExcelLoader.loadStudents(NO_SIES_FILE, issuesTracker));
    }
    
    @Test
    void loadsValidSiesFile() throws InvalidStudentFormatException, IOException {
        var issuesTracker = new IssuesCounter();
        List<Student> students = SiesExcelLoader.loadStudents(SIES_FILE, issuesTracker);
    
        assertEquals(3, students.size());
    
        assertStudent(students.get(0), 11, "ap1 ap1, name1", "student1@server.com", Optional.of("01"));
        assertStudent(students.get(1), 14, "ap4 ap4, name4", "student4@server.com", Optional.empty());
        assertStudent(students.get(2), 16, "ap5 ap5, name5", "student5@server.com", Optional.of("Inglés-05"));
    }
    
    private void assertStudent(Student student, int row, String name, String email, Optional<String> group) {
        assertEquals(row, student.row());
        assertEquals(name, student.firstName());
        assertEquals(email, student.email());
        assertEquals(group, student.group());
    }
    */

}
