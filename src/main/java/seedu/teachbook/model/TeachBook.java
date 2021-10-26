package seedu.teachbook.model;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.core.index.DefaultIndices.INDEX_LIST_ALL;
import static seedu.teachbook.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.teachbook.model.gradeobject.GradingSystem.NOT_GRADED;

import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.teachbook.commons.core.index.GeneralIndex;
import seedu.teachbook.model.classobject.Class;
import seedu.teachbook.model.classobject.ClassName;
import seedu.teachbook.model.classobject.ClassNameDescriptor;
import seedu.teachbook.model.classobject.UniqueClassList;
import seedu.teachbook.model.classobject.exceptions.NoClassWithNameException;
import seedu.teachbook.model.gradeobject.Grade;
import seedu.teachbook.model.gradeobject.GradingSystem;
import seedu.teachbook.model.student.Student;
import seedu.teachbook.model.student.UniqueStudentList;

/**
 * Wraps all data at the teachbook level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class TeachBook implements ReadOnlyTeachBook {

    private final UniqueClassList classes;
    private UniqueStudentList students; // different from AB3: this variable is for "list all" command only!
    private GradingSystem gradingSystem;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */ {
        students = new UniqueStudentList();
        classes = new UniqueClassList();
        gradingSystem = new GradingSystem();
    }

    public TeachBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public TeachBook(ReadOnlyTeachBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    public void resetGradingSystem(GeneralIndex classIndex) {
        this.gradingSystem = new GradingSystem();
        getClassList().forEach(studentClass -> studentClass.getStudentsOfThisClass().forEach(student -> {
            Student editedStudent = new Student(student.getName(), student.getPhone(),
                    student.getStudentClass(), student.getEmail(), student.getAddress(),
                    student.getRemark(), student.getTags(), student.getAttendance(), NOT_GRADED);
            setStudent(classIndex, student, editedStudent);
        }));
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyTeachBook newData) {
        requireNonNull(newData);
        ObservableList<Class> copy = FXCollections.observableArrayList();
        for (Class c : newData.getClassList()) {
            Class newClass = new Class(c.getClassName());
            for (Student s : c.getStudentsOfThisClass()) {
                Student studentToAdd = new Student(s.getName(), s.getPhone(), newClass, s.getEmail(),
                        s.getAddress(), s.getRemark(), s.getTags(), s.getAttendance(), s.getGrade());
                newClass.addStudent(studentToAdd);
            }
            copy.add(newClass);
        }
        setClasses(copy);
        setGradingSystem(newData.getGradingSystem());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the teachbook book.
     */
    public boolean hasStudent(GeneralIndex classIndex, Student student) {
        if (classIndex.equals(INDEX_LIST_ALL)) {
            return students.contains(student);
        } else {
            assert getClassAtIndex(classIndex).equals(student.getStudentClass());
        }
        return student.getStudentClass().containsStudent(student);
    }

    /**
     * Adds a student to the teachbook book.
     * The student must not already exist in the teachbook book.
     */
    public void addStudent(GeneralIndex classIndex, Student studentToAdd) {
        requireAllNonNull(classIndex, studentToAdd);
        assert getClassAtIndex(classIndex).equals(studentToAdd.getStudentClass());
        studentToAdd.getStudentClass().addStudent(studentToAdd);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the teachbook book.
     * The student identity of {@code editedPerson} must not be the same as
     * another existing student in the teachbook book.
     */
    public void setStudent(GeneralIndex classIndex, Student target, Student editedStudent) {
        requireAllNonNull(classIndex, target, editedStudent);
        if (classIndex.equals(INDEX_LIST_ALL)) {
            students.setStudent(target, editedStudent); // edit twice
        }
        target.getStudentClass().setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the teachbook book.
     */
    public void removeStudent(GeneralIndex classIndex, Student key) {
        if (classIndex.equals(INDEX_LIST_ALL)) {
            students.remove(key); // delete twice
        } else {
            assert getClassAtIndex(classIndex).equals(key.getStudentClass());
        }
        key.getStudentClass().removeStudent(key);
    }

    public void setClassForStudent(GeneralIndex classIndex, Student student) {
        student.setStudentClass(getClassAtIndex(classIndex));
    }

    //// util methods
    @Override
    public String toString() {
        return classes.asUnmodifiableObservableList().size() + " classes";
    }

    @Override
    public ObservableList<Student> getStudentList() {
        students = new UniqueStudentList();
        for (Class studentClass : classes) {
            for (Student student : studentClass.getStudentsOfThisClass()) {
                students.add(student);
            }
        }
        return students.asUnmodifiableObservableList();
    }

    public int getNumOfClasses() {
        return classes.size();
    }

    public ObservableList<Student> getStudentListOfClass(GeneralIndex classIndex) {
        return classes.getClassAtIndex(classIndex).getStudentsOfThisClass();
    }

    public GeneralIndex getIndexOfClass(ClassNameDescriptor className) throws NoClassWithNameException {
        requireNonNull(className);
        return classes.locateClass(className);
    }

    public Class getClassAtIndex(GeneralIndex classIndex) {
        return classes.getClassAtIndex(classIndex);
    }

    public GradingSystem getGradingSystem() {
        return gradingSystem;
    }

    public void setGradingSystem(GradingSystem newGradingSystem) {
        gradingSystem = newGradingSystem;
    }

    public boolean hasExistingGradingSystem() {
        return gradingSystem.isInUse();
    }

    public boolean isValidGrade(Grade grade) {
        return gradingSystem.isValidGrade(grade);
    }

    public void reorderStudents(GeneralIndex classIndex, Comparator<? super Student> comparator) {
        if (classIndex.equals(INDEX_LIST_ALL)) {
            students.sort(comparator);
        } else {
            getClassAtIndex(classIndex).reorderStudents(comparator);
        }
    }

    public boolean isEmpty() {
        return classes.isEmpty() && !gradingSystem.isInUse();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TeachBook // instanceof handles nulls
                && classes.equals(((TeachBook) other).classes)
                && students.equals(((TeachBook) other).students)
                && gradingSystem.equals(((TeachBook) other).gradingSystem));
    }

    @Override
    public int hashCode() {
        return students.hashCode() + classes.hashCode();
    }

    public boolean hasClass(Class classObj) {
        requireNonNull(classObj);
        return classes.contains(classObj);
    }

    public void setClass(Class target, Class editedClass) { // for editClass command
        requireNonNull(editedClass);
        classes.setClass(target, editedClass);
    }

    public void addClass(Class toAdd) {
        classes.add(toAdd);
    }

    public void removeClass(Class target) {
        classes.remove(target);
    }

    public void setClasses(List<Class> classes) {
        this.classes.setClasses(classes);
    }

    public void setClassName(GeneralIndex classIndex, ClassName updatedClassName) {
        Class target = getClassAtIndex(classIndex);
        Class editedClass = new Class(updatedClassName);
        UniqueStudentList newStudentList = target.getUniqueStudentListOfThisClass();
        newStudentList.forEach(student -> student.setStudentClass(editedClass));
        editedClass.setStudentsOfThisClass(newStudentList);
        setClass(target, editedClass);
    }

    @Override
    public ObservableList<Class> getClassList() {
        return classes.asUnmodifiableObservableList();
    }

}
