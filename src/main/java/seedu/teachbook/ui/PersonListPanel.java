package seedu.teachbook.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.teachbook.commons.core.LogsCenter;
import seedu.teachbook.model.student.Student;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Student> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Student> studentList, boolean showClass) {
        super(FXML);
        personListView.setItems(studentList);
        personListView.setCellFactory(listView -> new PersonListViewCell(showClass));
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Student> {
        private boolean showClass;

        public PersonListViewCell() {
            this.showClass = false;
        }

        public PersonListViewCell(boolean showClass) {
            this.showClass = showClass;
        }

        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);

            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(student, getIndex() + 1, showClass).getRoot());
            }
        }
    }

}
