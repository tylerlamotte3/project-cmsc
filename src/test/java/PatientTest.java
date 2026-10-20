import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PatientTest {
    @Test
    void patientKeepsIdentity() throws Exception {
        PatientIdentity identity =
                new PatientIdentity(
                        new Name("Jane", "Doe"),
                        new SimpleDateFormat("MM-dd-yyyy", Locale.US).parse("04-18-2001"));

        Patient patient = new Patient(identity);

        assertSame(identity, patient.getIdentity());
        assertEquals("Doe,Jane,04-18-2001", patient.toCSV());
        assertNotNull(patient.getPrescriptions());
    }

    @Test
    void factoryCreatesPatientFromCsv() {
        Patient patient = Patient.makePatient("Doe,Jane,04-18-2001");
        assertEquals("Doe", patient.getIdentity().getName().getLastName());
        assertEquals("Jane", patient.getIdentity().getName().getFirstName());
    }
}
