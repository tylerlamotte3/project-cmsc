import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PatientList {
    private static final int MAX_PATIENTS = 1000;

    private final Patient[] patients;
    private int count;

    public PatientList() {
        patients = new Patient[MAX_PATIENTS];
        count = 0;
    }

    
    public boolean add(Patient patient) {
        if (patient == null || count >= patients.length) {
            return false;
        }

        int position = insertionPosition(patient.getIdentity());

        if (position < count
                && patients[position].getIdentity().match(patient.getIdentity())) {
            return false;
        }

        insertAt(position, patient);
        return true;
    }

    public int size() {
        return count;
    }

    public Patient find(PatientIdentity identity) {
        if (identity == null) return null;
        return binarySearch(identity);
    }

    
    public boolean saveToFile(String filename) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Path.of(filename), StandardCharsets.UTF_8)) {

            Iterator iterator = new Iterator();
            Patient patient;
            while ((patient = iterator.next()) != null) {
                writer.write(patient.toCSV());
                writer.newLine();
            }

            return true;
        } catch (IOException | RuntimeException ex) {
            return false;
        }
    }


    public boolean importFromFile(String filename) {
        Patient[] imported = new Patient[MAX_PATIENTS];
        int importedCount = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(filename), StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Patient patient = Patient.makePatient(line);

                    if (importedCount < imported.length) {
                        imported[importedCount++] = patient;
                    }
                } catch (RuntimeException ignored) {
                    // skip
                }
            }

            mergeSort(imported, 0, importedCount);
            mergeImported(imported, importedCount);
            return true;

        } catch (IOException | RuntimeException ex) {
            return false;
        }
    }

    
    public void readPrescriptionsFromFile(String filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(filename), StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                try {
                    addPrescriptionFromCsv(line);
                } catch (RuntimeException ex) {
                    throw new IOException(
                            "Invalid prescription CSV at line " + lineNumber
                                    + ": " + ex.getMessage(), ex);
                }
            }
        }
    }

    public void importPrescriptionsFromFile(String filename) throws IOException {
        readPrescriptionsFromFile(filename);
    }

    private void addPrescriptionFromCsv(String line) {
        String[] fields = CsvUtil.parseLine(line);

        String patientName;
        String dobText;
        String medicine;
        String issueDate;
        String dosage;
        String prescriber;

        if (fields.length == 6) {
            patientName = fields[0];
            dobText = fields[1];
            medicine = fields[2];
            issueDate = fields[3];
            dosage = fields[4];
            prescriber = fields[5];
        } else if (fields.length == 7) {
            patientName = fields[0] + ", " + fields[1];
            dobText = fields[2];
            medicine = fields[3];
            issueDate = fields[4];
            dosage = fields[5];
            prescriber = fields[6];
        } else {
            throw new IllegalArgumentException(
                    "Expected 6 fields or unquoted Last,First form");
        }

        Name name = parsePatientName(patientName);
        PatientIdentity identity =
                new PatientIdentity(name, Patient.parseDate(dobText));

        Patient patient = find(identity);
        if (patient == null) {
            throw new IllegalArgumentException(
                    "No patient found for " + patientName + " born " + dobText);
        }

        String prescriptionCsv =
                CsvUtil.quote(medicine) + ","
                        + CsvUtil.quote(issueDate) + ","
                        + CsvUtil.quote(dosage) + ","
                        + CsvUtil.quote(prescriber);

        patient.getPrescriptions().add(
                Prescription.makePrescription(prescriptionCsv));
    }

    private static Name parsePatientName(String value) {
        String text = value.trim();
        int comma = text.indexOf(',');

        if (comma >= 0) {
            return new Name(
                    text.substring(comma + 1).trim(),
                    text.substring(0, comma).trim());
        }

        String[] parts = text.split("\\s+", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Patient name must contain first and last name");
        }

        return new Name(parts[1], parts[0]);
    }


    private int insertionPosition(PatientIdentity identity) {
        int position = 0;

        while (position < count
                && patients[position].getIdentity().isLessThan(identity)) {
            position++;
        }

        return position;
    }

    private void insertAt(int position, Patient patient) {
        for (int i = count; i > position; i--) {
            patients[i] = patients[i - 1];
        }
        patients[position] = patient;
        count++;
    }


    private Patient binarySearch(PatientIdentity identity) {
        int lower = 0;
        int upper = count - 1;

        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            PatientIdentity current = patients[middle].getIdentity();

            if (current.match(identity)) {
                return patients[middle];
            }

            if (current.isLessThan(identity)) {
                lower = middle + 1;
            } else {
                upper = middle - 1;
            }
        }

        return null;
    }


    private static void mergeSort(Patient[] array, int from, int to) {
        if (to - from <= 1) return;

        int middle = from + (to - from) / 2;
        mergeSort(array, from, middle);
        mergeSort(array, middle, to);
        merge(array, from, middle, to);
    }

    private static void merge(
            Patient[] array, int from, int middle, int to) {

        Patient[] temp = new Patient[to - from];
        int left = from;
        int right = middle;
        int output = 0;

        while (left < middle && right < to) {
            Patient leftPatient = array[left];
            Patient rightPatient = array[right];

            if (leftPatient.getIdentity().isLessThan(rightPatient.getIdentity())
                    || leftPatient.getIdentity().match(rightPatient.getIdentity())) {
                temp[output++] = leftPatient;
                left++;
            } else {
                temp[output++] = rightPatient;
                right++;
            }
        }

        while (left < middle) {
            temp[output++] = array[left++];
        }

        while (right < to) {
            temp[output++] = array[right++];
        }

        for (int i = 0; i < temp.length; i++) {
            array[from + i] = temp[i];
        }
    }

    
    private void mergeImported(Patient[] imported, int importedCount) {
        Patient[] merged = new Patient[MAX_PATIENTS];
        int existingIndex = 0;
        int importedIndex = 0;
        int mergedCount = 0;

        while (existingIndex < count && importedIndex < importedCount
                && mergedCount < merged.length) {

            Patient existing = patients[existingIndex];
            Patient incoming = imported[importedIndex];

            if (existing.getIdentity().match(incoming.getIdentity())) {
                merged[mergedCount++] = existing;
                existingIndex++;
                importedIndex++;
            } else if (existing.getIdentity().isLessThan(incoming.getIdentity())) {
                merged[mergedCount++] = existing;
                existingIndex++;
            } else {
                merged[mergedCount++] = incoming;
                importedIndex++;
            }
        }

        while (existingIndex < count && mergedCount < merged.length) {
            merged[mergedCount++] = patients[existingIndex++];
        }

        while (importedIndex < importedCount && mergedCount < merged.length) {
            Patient incoming = imported[importedIndex++];

            if (mergedCount == 0
                    || !merged[mergedCount - 1].getIdentity().match(
                            incoming.getIdentity())) {
                merged[mergedCount++] = incoming;
            }
        }

        for (int i = 0; i < mergedCount; i++) {
            patients[i] = merged[i];
        }
        for (int i = mergedCount; i < patients.length; i++) {
            patients[i] = null;
        }
        count = mergedCount;
    }

    public class Iterator {
        private int index;

        public Iterator() {
            index = 0;
        }

        public Patient next() {
            if (index >= count) return null;
            return patients[index++];
        }
    }
}
