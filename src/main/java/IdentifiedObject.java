/**
 * A value that can be stored in a BinarySearchTree and located by its identity.
 * The identity type supplies the ordering used by the tree.
 */
public interface IdentifiedObject<I extends Comparable<I>> {
    I getIdentity();
}
