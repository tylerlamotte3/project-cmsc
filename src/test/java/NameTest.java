import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class NameTest {
    @Test
    void createsAndFormatsName() {
        Name name = new Name("Jane", "Doe");
        assertEquals("Jane", name.getFirstName());
        assertEquals("Doe", name.getLastName());
        assertEquals("Doe, Jane", name.fullname());
    }

    @Test
    void matchesWithoutCaseSensitivity() {
        assertTrue(new Name("jAnE", "dOE").match(new Name("Jane", "Doe")));
    }

    @Test
    void sortsByLastThenFirstName() {
        assertTrue(new Name("Amy", "Adams").isLessThan(new Name("Bob", "Brown")));
        assertTrue(new Name("Amy", "Smith").isLessThan(new Name("Bob", "Smith")));
        assertFalse(new Name("Bob", "Smith").isLessThan(new Name("Amy", "Smith")));
    }
}
