package seedu.teachbook.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.teachbook.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.teachbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.teachbook.testutil.Assert.assertThrows;
import static seedu.teachbook.testutil.TypicalPersons.ALICE;
//import static seedu.teachbook.testutil.TypicalPersons.getTypicalTeachBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.teachbook.model.classobject.Class;
import seedu.teachbook.model.student.Student;
import seedu.teachbook.model.student.exceptions.DuplicateStudentException;
import seedu.teachbook.testutil.PersonBuilder;

public class TeachBookTest {

    private final TeachBook teachBook = new TeachBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), teachBook.getStudentList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> teachBook.resetData(null));
    }

    // TODO: rewrite getTypicalTeachBook() to produce a TeachBook with only contains uniqueClassList
    // Current implementation: getTypicalTeachBook() produces a TeachBook with both uniqueClassList
    // and uniqueStudentList
//    @Test
//    public void resetData_withValidReadOnlyTeachBook_replacesData() {
//        TeachBook newData = getTypicalTeachBook();
//        teachBook.resetData(newData);
//        assertEquals(newData, teachBook);
//    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Student editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Student> newStudents = Arrays.asList(ALICE, editedAlice);
        TeachBookStub newData = new TeachBookStub(newStudents);

        assertThrows(DuplicateStudentException.class, () -> teachBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> teachBook.hasStudent(null));
    }

    @Test
    public void hasPerson_personNotInTeachBook_returnsFalse() {
        assertFalse(teachBook.hasStudent(ALICE));
    }

    @Test
    public void hasPerson_personInTeachBook_returnsTrue() {
        teachBook.addStudent(ALICE);
        assertTrue(teachBook.hasStudent(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInTeachBook_returnsTrue() {
        teachBook.addStudent(ALICE);
        Student editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(teachBook.hasStudent(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> teachBook.getStudentList().remove(0));
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class TeachBookStub implements ReadOnlyTeachBook {
        private final ObservableList<Student> students = FXCollections.observableArrayList();
        private final ObservableList<Class> classes = FXCollections.observableArrayList();

        TeachBookStub(Collection<Student> students) {
            this.students.setAll(students);
            this.classes.setAll(classes); // TODO: fix this. what's this test doing?
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }

        @Override
        public ObservableList<Class> getClassList() {
            return classes;
        }
    }

}
