import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A generic binary search tree. Values are ordered by the identity returned
 * from IdentifiedObject.getIdentity(). Duplicate identities are ignored.
 */
public class BinarySearchTree<T extends IdentifiedObject<I>, I extends Comparable<I>> {
    private static class TreeNode<T> {
        private final T data;
        private TreeNode<T> left;
        private TreeNode<T> right;

        private TreeNode(T data) { this.data = data; }
    }

    private TreeNode<T> root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /** Adds an object unless an object with the same identity is already present. */
    public void add(T item) {
        Objects.requireNonNull(item, "Cannot add a null item");
        I identity = Objects.requireNonNull(item.getIdentity(), "Item identity cannot be null");
        root = addNode(root, item, identity);
    }

    private TreeNode<T> addNode(TreeNode<T> current, T item, I identity) {
        if (current == null) {
            size++;
            return new TreeNode<>(item);
        }
        int comparison = identity.compareTo(current.data.getIdentity());
        if (comparison < 0) {
            current.left = addNode(current.left, item, identity);
        } else if (comparison > 0) {
            current.right = addNode(current.right, item, identity);
        }
        return current;
    }

    /** Returns the object with the supplied identity, or null if not found. */
    public T find(I identity) {
        if (identity == null) return null;
        return findNode(root, identity);
    }

    private T findNode(TreeNode<T> current, I identity) {
        if (current == null) return null;
        int comparison = identity.compareTo(current.data.getIdentity());
        if (comparison == 0) return current.data;
        return comparison < 0
                ? findNode(current.left, identity)
                : findNode(current.right, identity);
    }

    public int size() { return size; }

    public boolean isEmpty() { return root == null; }

    /** Returns the tree's values in ascending identity order. */
    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(TreeNode<T> node, List<T> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.data);
        inOrder(node.right, result);
    }

    /** Prints the values in ascending identity order, one per line. */
    public void printInOrder() {
        for (T item : inOrder()) System.out.println(item);
    }
}
