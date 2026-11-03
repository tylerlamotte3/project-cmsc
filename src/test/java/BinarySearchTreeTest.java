import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private Patient patient(String first, String last, long dob) {
        return new Patient(new PatientIdentity(new Name(first, last), new Date(dob)));
    }

    @Test
    void emptyTreeHasNoValues() {
        BinarySearchTree<Patient, PatientIdentity> tree = new BinarySearchTree<>();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertNull(tree.find(null));
        assertTrue(tree.inOrder().isEmpty());
    }

    @Test
    void addAndFindWorksForRootLeftAndRight() {
        BinarySearchTree<Patient, PatientIdentity> tree = new BinarySearchTree<>();
        Patient middle = patient("Mia", "Middle", 1000);
        Patient left = patient("Amy", "Able", 1000);
        Patient right = patient("Zoe", "Zulu", 1000);
        tree.add(middle);
        tree.add(left);
        tree.add(right);
        assertSame(middle, tree.find(middle.getIdentity()));
        assertSame(left, tree.find(left.getIdentity()));
        assertSame(right, tree.find(right.getIdentity()));
        assertNull(tree.find(patient("No", "Person", 1000).getIdentity()));
        assertEquals(3, tree.size());
    }

    @Test
    void duplicateIdentityIsNotAddedTwice() {
        BinarySearchTree<Patient, PatientIdentity> tree = new BinarySearchTree<>();
        Patient first = patient("Sam", "Same", 1000);
        Patient duplicate = patient("Sam", "Same", 1000);
        tree.add(first);
        tree.add(duplicate);
        assertEquals(1, tree.size());
        assertSame(first, tree.find(duplicate.getIdentity()));
    }

    @Test
    void inOrderReturnsSortedValues() {
        BinarySearchTree<Patient, PatientIdentity> tree = new BinarySearchTree<>();
        Patient p3 = patient("Mia", "Middle", 3000);
        Patient p1 = patient("Amy", "Able", 1000);
        Patient p2 = patient("Lee", "Lane", 2000);
        tree.add(p3);
        tree.add(p1);
        tree.add(p2);
        List<Patient> ordered = tree.inOrder();
        assertEquals(List.of(p1, p2, p3), ordered);
    }

    @Test
    void rejectsNullItems() {
        BinarySearchTree<Patient, PatientIdentity> tree = new BinarySearchTree<>();
        assertThrows(NullPointerException.class, () -> tree.add(null));
    }
}
