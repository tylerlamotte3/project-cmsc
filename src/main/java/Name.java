public class Name {
    private final String firstName;
    private final String lastName;

    public Name(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("Names cannot be null");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();

        if (this.firstName.isEmpty() || this.lastName.isEmpty()) {
            throw new IllegalArgumentException("Names cannot be empty");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String fullname() {
        return lastName + ", " + firstName;
    }

    public boolean match(Name other) {
        return other != null
                && firstName.equalsIgnoreCase(other.firstName)
                && lastName.equalsIgnoreCase(other.lastName);
    }

    public boolean isLessThan(Name other) {
        if (other == null) throw new IllegalArgumentException("Other name cannot be null");

        int lastComparison = lastName.compareToIgnoreCase(other.lastName);
        if (lastComparison != 0) {
            return lastComparison < 0;
        }

        return firstName.compareToIgnoreCase(other.firstName) < 0;
    }

    @Override
    public String toString() {
        return fullname();
    }
}
