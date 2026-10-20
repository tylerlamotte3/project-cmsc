import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PrescriptionListTest {
    private Prescription prescription(String medicine, String date) {
        return Prescription.makePrescription(
                medicine + "," + date + ",1,Dr. Test");
    }

    @Test
    void insertsNewestFirst() {
        PrescriptionList list = new PrescriptionList();
        list.add(prescription("Older", "01-10-2024"));
        list.add(prescription("Newest", "06-20-2026"));
        list.add(prescription("Middle", "03-15-2025"));

        list.init();
        assertEquals("Newest", list.next().getMedicineName());
        assertEquals("Middle", list.next().getMedicineName());
        assertEquals("Older", list.next().getMedicineName());
        assertNull(list.next());
    }

    @Test
    void iterationCanBeResetAndSameDayEntriesAreAllowed() {
        PrescriptionList list = new PrescriptionList();
        list.add(prescription("A", "04-01-2026"));
        list.add(prescription("B", "04-01-2026"));

        assertEquals(2, list.size());

        list.init();
        assertNotNull(list.next());
        list.init();
        assertNotNull(list.next());
    }
}
