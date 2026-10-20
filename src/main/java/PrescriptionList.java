import java.util.Date;

public class PrescriptionList {
    private ListRecord head;
    private ListRecord iterationPosition;

    public PrescriptionList() {
        head = null;
        iterationPosition = null;
    }

    public void add(Prescription prescription) {
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }

        ListRecord newRecord = new ListRecord(prescription);

        if (head == null || comesBefore(prescription, head.data)) {
            newRecord.next = head;
            head = newRecord;
            return;
        }

        ListRecord before = head;
        while (before.next != null && !comesBefore(prescription, before.next.data)) {
            before = before.next;
        }

        newRecord.next = before.next;
        before.next = newRecord;
    }

    
    private static boolean comesBefore(Prescription first, Prescription second) {
        Date firstDate = first.getDateOfIssue();
        Date secondDate = second.getDateOfIssue();
        return firstDate.compareTo(secondDate) > 0;
    }

    public void init() {
        iterationPosition = head;
    }

    public Prescription next() {
        if (iterationPosition == null) return null;

        Prescription result = iterationPosition.data;
        iterationPosition = iterationPosition.next;
        return result;
    }

    public int size() {
        int count = 0;
        ListRecord current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    private static class ListRecord {
        private final Prescription data;
        private ListRecord next;

        private ListRecord(Prescription prescription) {
            data = prescription;
            next = null;
        }
    }
}
