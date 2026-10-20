import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class PatientListPrescriptionImportTest {
    @Test
    void importsPrescriptionAndMaintainsNewestFirst() throws Exception {
        PatientList patients = new PatientList();
        Patient patient = Patient.makePatient("Doe,Jane,04-18-2001");
        assertTrue(patients.add(patient));

        Path file = Files.createTempFile("prescriptions", ".csv");
        try {
            Files.writeString(file,
                    "\"Doe, Jane\",04-18-2001,Older,02-01-2025,200,Dr. Jones\n"
                    + "\"Doe, Jane\",04-18-2001,Newest,10-12-2026,500,Dr. Smith\n");

            patients.readPrescriptionsFromFile(file.toString());

            assertEquals(2, patient.getPrescriptions().size());
            patient.getPrescriptions().init();
            assertEquals("Newest", patient.getPrescriptions().next().getMedicineName());
            assertEquals("Older", patient.getPrescriptions().next().getMedicineName());
            assertNull(patient.getPrescriptions().next());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void acceptsSuppliedUnquotedLastFirstIsoFormat() throws Exception {
        PatientList patients = new PatientList();
        Patient patient = Patient.makePatient("Smith,Maria,1953-06-16");
        assertTrue(patients.add(patient));

        Path file = Files.createTempFile("prescriptions", ".csv");
        try {
            Files.writeString(file,
                    "Smith,Maria,1953-06-16,heptapone,2023-02-11,50,Agrawal\n");

            patients.readPrescriptionsFromFile(file.toString());

            assertEquals(1, patient.getPrescriptions().size());
            patient.getPrescriptions().init();
            assertEquals("heptapone", patient.getPrescriptions().next().getMedicineName());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void unknownPatientIsRejected() throws Exception {
        PatientList patients = new PatientList();
        Path file = Files.createTempFile("prescriptions-unknown", ".csv");
        try {
            Files.writeString(file,
                    "Unknown,Person,01-01-2000,Medicine,01-01-2026,1,Dr. Test\n");
            assertThrows(java.io.IOException.class,
                    () -> patients.readPrescriptionsFromFile(file.toString()));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
