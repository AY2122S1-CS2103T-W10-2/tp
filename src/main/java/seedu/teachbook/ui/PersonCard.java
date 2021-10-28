package seedu.teachbook.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.teachbook.model.student.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;
    private boolean showClass;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;
    @FXML
    private Label grade;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label className;
    @FXML
    private VBox classNameBox;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Student student, int displayedIndex, boolean showClass) {
        super(FXML);
        this.student = student;
        this.showClass = showClass;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);

        String phoneContent = student.getPhone().value;
        phone.setText("Phone:     " + phoneContent); // TODO: use aesthetic separate labels

        String addressContent = student.getAddress().value;
        address.setText("Address:  " + addressContent);

        String emailContent = student.getEmail().value;
        email.setText("Email:       " + emailContent);

        checkBox.setMouseTransparent(true);
        checkBox.setSelected(student.isPresent());

        String remarkContent = student.getRemark().value;
        if (remarkContent.equals("")) {
            remark.setMinHeight(0.0);
            remark.setPrefHeight(0.0);
        } else {
            remark.setText(remarkContent);
        }

        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        String gradeContent = student.getGrade().value;
        if (gradeContent.equals("")) {
            grade.setMinHeight(0.0);
            grade.setPrefHeight(0.0);
        } else {
            grade.setText(gradeContent);
        }

        // TODO: Set the condition to check if it is a list command
        if (showClass) {
            className.setText(student.getStudentClass().toString());
        } else {
            className.setMinHeight(0.0);
            className.setPrefHeight(0.0);
            classNameBox.setMinHeight(0.0);
            classNameBox.setPrefHeight(0.0);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }

}
