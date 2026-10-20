import java.util.Date;

public class Prescription {
    private final String medicineName;
    private final Date dateOfIssue;
    private final int dosage;
    private final String prescriber;

    public Prescription(String medicineName, Date dateOfIssue, int dosage, String prescriber) {
        if (medicineName == null || medicineName.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be empty");
        }
        if (dateOfIssue == null) {
            throw new IllegalArgumentException("Issue date cannot be null");
        }
        if (prescriber == null || prescriber.trim().isEmpty()) {
            throw new IllegalArgumentException("Prescriber cannot be empty");
        }

        this.medicineName = medicineName.trim();
        this.dateOfIssue = new Date(dateOfIssue.getTime());
        this.dosage = dosage;
        this.prescriber = prescriber.trim();
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getName() {
        return medicineName;
    }

    public Date getDateOfIssue() {
        return new Date(dateOfIssue.getTime());
    }

    public int getDosage() {
        return dosage;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public static Prescription makePrescription(String csv) {
        String[] fields = CsvUtil.parseLine(csv);
        if (fields.length != 4) {
            throw new IllegalArgumentException(
                    "Expected medicine, date, dosage, and prescriber");
        }

        int dosage;
        try {
            dosage = Integer.parseInt(fields[2].trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Dosage must be an integer", ex);
        }

        return new Prescription(
                fields[0],
                Patient.parseDate(fields[1]),
                dosage,
                fields[3]);
    }

    @Override
    public String toString() {
        return medicineName + ", "
                + Patient.formatDate(dateOfIssue) + ", "
                + dosage + ", "
                + prescriber;
    }
}
