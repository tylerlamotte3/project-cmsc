public class Main {
    public static void main(String[] args) {
        PatientList patients = new PatientList();

        patients.importFromFile("patients.csv");

        PatientList.Iterator iterator = patients.new Iterator();
        Patient patient;

        while ((patient = iterator.next()) != null) {
            System.out.println(patient.toCSV());
        }

        if (patients.saveToFile("patients-out.csv")) {
            System.out.println("Patient list saved.");
        }

        // Prescription import can be enabled when a matching patient file
        // has been loaded:
        //
        // patients.readPrescriptionsFromFile("prescriptions1000.csv");
    }
}
