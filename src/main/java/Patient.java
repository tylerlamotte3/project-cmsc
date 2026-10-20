import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Patient {
    private final PatientIdentity patientIdentity;
    private final PrescriptionList prescriptions;

    public Patient(PatientIdentity identity) {
        if (identity == null) {
            throw new IllegalArgumentException("Patient identity cannot be null");
        }
        patientIdentity = identity;
        prescriptions = new PrescriptionList();
    }

    public PatientIdentity getIdentity() {
        return patientIdentity;
    }

    public PrescriptionList getPrescriptions() {
        return prescriptions;
    }

    public PrescriptionList getPrescriptionList() {
        return prescriptions;
    }

    public String toCSV() {
        Name name = patientIdentity.getName();
        return CsvUtil.quote(name.getLastName()) + ","
                + CsvUtil.quote(name.getFirstName()) + ","
                + formatDate(patientIdentity.getDateOfBirth());
    }

    public static Patient makePatient(String csv) {
        String[] fields = CsvUtil.parseLine(csv);
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                    "Expected last name, first name, and date of birth");
        }

        Name name = new Name(fields[1], fields[0]);
        Date dob = parseDate(fields[2]);
        return new Patient(new PatientIdentity(name, dob));
    }

    static Date parseDate(String value) {
        if (value == null) throw new IllegalArgumentException("Date cannot be null");
        String text = value.trim();

        
        Date date = parseDateWithFormat(text, "MM-dd-yyyy");
        if (date != null) return date;

        throw new IllegalArgumentException("Invalid date: " + value);
    }

    private static Date parseDateWithFormat(String text, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        format.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date date = format.parse(text, position);
        return date != null && position.getIndex() == text.length() ? date : null;
    }

    static String formatDate(Date date) {
        return new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(date);
    }

    @Override
    public String toString() {
        return "Patient: " + patientIdentity;
    }
}
