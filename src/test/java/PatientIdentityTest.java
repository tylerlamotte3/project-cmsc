import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PatientIdentityTest {
    private Date date(String value) throws Exception {
        return new SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(value);
    }

    @Test
    void matchesNameAndDateOfBirth() throws Exception {
        PatientIdentity a =
                new PatientIdentity(new Name("Jane", "Doe"), date("04-18-2001"));
        PatientIdentity b =
                new PatientIdentity(new Name("jane", "DOE"), date("04-18-2001"));
        PatientIdentity c =
                new PatientIdentity(new Name("Jane", "Doe"), date("04-19-2001"));

        assertTrue(a.match(b));
        assertFalse(a.match(c));
    }

    @Test
    void sortsByNameThenDate() throws Exception {
        PatientIdentity earlier =
                new PatientIdentity(new Name("Jane", "Doe"), date("04-18-2001"));
        PatientIdentity later =
                new PatientIdentity(new Name("Jane", "Doe"), date("04-19-2001"));

        assertTrue(earlier.isLessThan(later));
        assertFalse(later.isLessThan(earlier));
    }
}
