import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class PatientListTest {
    private Patient patient(String csv) {
        return Patient.makePatient(csv);
    }

    @Test
    void addKeepsSortedOrderAndFindUsesIdentity() {
        PatientList list = new PatientList();
        Patient smith = patient("Smith,John,01-01-2000");
        Patient adams = patient("Adams,Bob,04-20-1999");
        Patient doe = patient("Doe,Jane,06-15-2001");

        assertTrue(list.add(smith));
        assertTrue(list.add(adams));
        assertTrue(list.add(doe));

        PatientList.Iterator iterator = list.new Iterator();
        assertEquals("Adams", iterator.next().getIdentity().getName().getLastName());
        assertEquals("Doe", iterator.next().getIdentity().getName().getLastName());
        assertEquals("Smith", iterator.next().getIdentity().getName().getLastName());
        assertNull(iterator.next());

        assertSame(doe, list.find(
                Patient.makePatient("doe,jane,06-15-2001").getIdentity()));
        assertNull(list.find(
                Patient.makePatient("Unknown,Person,06-15-2001").getIdentity()));
    }

    @Test
    void duplicateIdentityIsNotInserted() {
        PatientList list = new PatientList();
        assertTrue(list.add(patient("Doe,Jane,04-18-2001")));
        assertFalse(list.add(patient("doe,jane,04-18-2001")));
        assertEquals(1, list.size());
    }

    @Test
    void importSkipsInvalidRowsAndSortsWithMergesort() throws Exception {
        Path file = Files.createTempFile("patients", ".csv");
        try {
            Files.writeString(file,
                    "Smith,John,01-01-2000\n"
                    + "bad,row\n"
                    + "Adams,Bob,04-20-1999\n"
                    + "Doe,Jane,06-15-2001\n");

            PatientList list = new PatientList();
            assertTrue(list.importFromFile(file.toString()));
            assertEquals(3, list.size());

            PatientList.Iterator iterator = list.new Iterator();
            assertEquals("Adams", iterator.next().getIdentity().getName().getLastName());
            assertEquals("Doe", iterator.next().getIdentity().getName().getLastName());
            assertEquals("Smith", iterator.next().getIdentity().getName().getLastName());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void saveWritesCsv() throws Exception {
        Path file = Files.createTempFile("patients-save", ".csv");
        try {
            PatientList list = new PatientList();
            list.add(patient("Doe,Jane,04-18-2001"));
            assertTrue(list.saveToFile(file.toString()));
            assertEquals("Doe,Jane,04-18-2001\n", Files.readString(file));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
