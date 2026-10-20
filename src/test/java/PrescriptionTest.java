import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PrescriptionTest {
    @Test
    void factoryCreatesPrescription() {
        Prescription prescription =
                Prescription.makePrescription(
                        "Amoxicillin,10-12-2026,500,Dr. Smith");

        assertEquals("Amoxicillin", prescription.getMedicineName());
        assertEquals(500, prescription.getDosage());
        assertEquals("Dr. Smith", prescription.getPrescriber());
    }
}
