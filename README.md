# CMSC 230 Assignments 1–4

This project implements the CMSC 230 course-note requirements through Project 4:

1. **Patient / PatientIdentity / Name**
   - immutable patient identity
   - case-insensitive name matching and ordering
   - identity ordering by name, then date of birth
   - CSV factory and formatting

2. **PatientList**
   - fixed basic Java array with capacity 1000
   - `add` returns `boolean`
   - sorted insertion using `PatientIdentity`
   - iterative binary search
   - public inner iterator whose `next()` returns `null` at the end
   - no Java collection classes are used by the project implementation

3. **File Load / Save / Merge / Sort**
   - `saveToFile(String)`
   - `importFromFile(String)`
   - invalid patient rows are skipped while valid rows continue to import
   - imported records are sorted with recursive mergesort and merged into the existing ordered list
   - `Patient.toCSV()`

4. **Prescription List**
   - `Prescription`
   - `PrescriptionList` implemented as a private-node linked list
   - newest prescriptions first
   - `init()` / `next()` iteration
   - prescription CSV import into the matching patient
   - multiple prescription files can be imported without replacing earlier prescriptions

## Date compatibility

The course notes use `MM-dd-yyyy` in their CSV examples. The supplied project data also contains `yyyy-MM-dd` dates, so the import code accepts both formats while `Patient.toCSV()` and `Prescription.toString()` use the course-note `MM-dd-yyyy` representation.

## Build

Java 17+:

```bash
javac -d out src/main/java/*.java
```

JUnit 5 tests are under `src/test/java`. The implementation was also smoke-tested by compiling the main sources and exercising patient sorting/search, patient-file import, prescription import, and newest-first prescription ordering.

## Project 5 – Binary Search Tree

Project 5 adds a reusable `BinarySearchTree<T, I>` collection in its own source file,
plus the `IdentifiedObject<I>` interface that lets the tree obtain each stored object's
identity. The identity type implements `Comparable`, so the tree is not tied to the
Patient class. `Patient` implements `IdentifiedObject<PatientIdentity>`, and
`PatientIdentity` implements `Comparable<PatientIdentity>` using the same name/date
ordering already used by the earlier assignments.

The tree supports `add`, `find`, `size`, `isEmpty`, `inOrder`, and `printInOrder`.
It uses recursive insertion, recursive lookup, and recursive in-order traversal. An
object with an identity already in the tree is ignored, as specified in the course notes.
JUnit tests cover an empty tree, root/left/right lookup, unsuccessful lookup, duplicate
identities, sorted traversal, and null input.

Project 5 intentionally leaves `PatientList` using its existing array implementation;
integrating the tree into `PatientList` is the next project.
