package seedu.teachbook.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.teachbook.commons.exceptions.IllegalValueException;
import seedu.teachbook.model.ReadOnlyTeachBook;
import seedu.teachbook.model.TeachBook;
import seedu.teachbook.model.classobject.Class;

/**
 * An Immutable TeachBook that is serializable to JSON format.
 */
@JsonRootName(value = "teachbook")
class JsonSerializableTeachBook {

    public static final String MESSAGE_DUPLICATE_CLASS = "TeachBook contains duplicate classes.";

    private final List<JsonAdaptedClass> classes = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableTeachBook} with the given students
     * .
     */
    @JsonCreator
    public JsonSerializableTeachBook(@JsonProperty("classes") List<JsonAdaptedClass> classes) {
        this.classes.addAll(classes);
    }

    /**
     * Converts a given {@code ReadOnlyTeachBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableTeachBook}.
     */
    public JsonSerializableTeachBook(ReadOnlyTeachBook source) {
        classes.addAll(source.getClassList().stream().map(JsonAdaptedClass::new).collect(Collectors.toList()));
    }

    /**
     * Converts this teachbook book into the model's {@code TeachBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public TeachBook toModelType() throws IllegalValueException {
        TeachBook teachBook = new TeachBook();

        for (JsonAdaptedClass jsonAdaptedClass : classes) {
            Class classObj = jsonAdaptedClass.toModelType();
            if (teachBook.hasClass(classObj)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLASS);
            }
            teachBook.addClass(classObj);
        }
        return teachBook;
    }

}
