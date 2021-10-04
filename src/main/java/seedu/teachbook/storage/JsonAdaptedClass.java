package seedu.teachbook.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.teachbook.commons.exceptions.IllegalValueException;
import seedu.teachbook.model.classobject.Class;
import seedu.teachbook.model.classobject.ClassName;
import seedu.teachbook.model.person.Name;
import seedu.teachbook.model.person.Student;

/**
 * Jackson-friendly version of {@link Class}.
 */
class JsonAdaptedClass {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Class's %s field is missing!";

    private final String className;
    private final List<JsonAdaptedPerson> classList = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedClass} with the given class details.
     */
    @JsonCreator
    public JsonAdaptedClass(@JsonProperty("class_name") String name) {
        this.className = name;
        if (classList != null) {
            this.classList.addAll(classList);
        }
    }

    /**
     * Converts a given {@code Class} into this class for Jackson use.
     */
    public JsonAdaptedClass(Class source) {
        className = source.getClassName().fullName;
        classList.addAll(source.getClassListSet().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted class object into the model's {@code Class} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted class.
     */
    public Class toModelType() throws IllegalValueException {

        if (className == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(className)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final ClassName modelName = new ClassName(className);

        final List<Student> studentList = new ArrayList<>();
        for (JsonAdaptedPerson student : classList) {
            studentList.add(student.toModelType());
        }
        Class model = new Class(modelName);
        model.setStudentsOfThisClass(studentList);
        return model;
    }

}
