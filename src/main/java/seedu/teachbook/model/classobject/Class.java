package seedu.teachbook.model.classobject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.teachbook.model.student.Student;
import seedu.teachbook.model.student.UniqueStudentList;

public class Class {

    private final ClassName className;
    private UniqueStudentList studentsOfThisClass;
    private Set<Student> classListSet = new HashSet<>();

    public Class(ClassName className) {
        this.className = className;
        this.studentsOfThisClass = new UniqueStudentList();
    }

    public UniqueStudentList getStudentsOfThisClass() {
        return studentsOfThisClass;
    }

    public Set<Student> getClassListSet() {
        return classListSet;
    }
    public void setStudentsOfThisClass(List<Student> students) {
        this.studentsOfThisClass.setStudents(students);
        classListSet.addAll(students);
    }

    public ClassName getClassName() {
        return className;
    }

    public void setStudent(Student target, Student editedStudent) {
        studentsOfThisClass.setStudent(target, editedStudent);
        classListSet.remove(target);
        classListSet.add(editedStudent);
    }

    public void addStudent(Student student) {
        this.studentsOfThisClass.add(student);
        classListSet.add(student);
    }

    public void removeStudent(Student student) {
        this.studentsOfThisClass.remove(student);
        classListSet.remove(student);
    }

    public boolean hasStudent(Student student) {
        return studentsOfThisClass.contains(student);
    }

    public boolean isSameClass(Class otherClass) {
        if (otherClass == this) {
            return true;
        }

        return otherClass != null
                && otherClass.getClassName().equals(getClassName());
    }

    public int getClassSize() {
        return this.studentsOfThisClass.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Class)) {
            return false;
        }

        Class otherClass = (Class) other;
        return otherClass.getClassName().equals(getClassName());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClassName());
        return builder.toString();
    }

}
