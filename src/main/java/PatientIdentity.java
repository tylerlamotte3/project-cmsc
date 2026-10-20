import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientIdentity {
    private final Name name;
    private final Date dateOfBirth;

    public PatientIdentity(Name name, Date dateOfBirth) {
        if (name == null || dateOfBirth == null) {
            throw new IllegalArgumentException("Name and date of birth are required");
        }
        this.name = name;
        this.dateOfBirth = new Date(dateOfBirth.getTime());
    }

    public Name getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth.getTime());
    }

    public boolean match(PatientIdentity other) {
        return other != null
                && name.match(other.name)
                && dateOfBirth.equals(other.dateOfBirth);
    }

    public boolean isLessThan(PatientIdentity other) {
        if (other == null) throw new IllegalArgumentException("Other identity cannot be null");

        if (name.isLessThan(other.name)) return true;
        if (!name.match(other.name)) return false;

        return dateOfBirth.compareTo(other.dateOfBirth) < 0;
    }

    @Override
    public String toString() {
        return name.toString() + " " +
                new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(dateOfBirth);
    }
}
