package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_COLUMN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.teachbook.commons.util.ExcelUtil;
import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.student.Student;
import seedu.teachbook.model.tag.Tag;

/**
 * Switches to another class identified using its name.
 */
public class PrintCommand extends Command {

    public static final String COMMAND_WORD = "print";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Prints the currently displayed student list as an Excel file.\n"
            + "Parameters: "
            + "[" + PREFIX_COLUMN + "class] "
            + "[" + PREFIX_COLUMN + "phone] "
            + "[" + PREFIX_COLUMN + "email] "
            + "[" + PREFIX_COLUMN + "address] "
            + "[" + PREFIX_COLUMN + "tags] "
            + "[" + PREFIX_COLUMN + "remark] "
            + "[" + PREFIX_COLUMN + "attendance] "
            + "[" + PREFIX_COLUMN + "grade] "
            + "[" + PREFIX_COLUMN + "COLUMN_TITLE_1] "
            + "[" + PREFIX_COLUMN + "COLUMN_TITLE_2]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COLUMN + "class "
            + PREFIX_COLUMN + "phone "
            + PREFIX_COLUMN + "address "
            + PREFIX_COLUMN + "attendance "
            + PREFIX_COLUMN + "grade "
            + PREFIX_COLUMN + "Sign Here";

    public static final String MESSAGE_SUCCESS = "Excel file generated!";
    public static final String MESSAGE_EXCEL_OPEN = "Print failed! An Excel file with conflicting name is opened, "
            + "please close it before using print command!";

    private final List<String> columnList;

    public PrintCommand(List<String> columnList) {
        this.columnList = columnList;
    }

    private static List<String> generateColumn(String columnName, List<Student> studentList) {
        List<String> result = new ArrayList<>();

        switch (columnName.toLowerCase()) {
        case "class":
            result.add("Class");
            for (Student student : studentList) {
                result.add(student.getStudentClass().getClassName().nameOfClass);
            }
            break;

        case "phone":
            result.add("Phone Number");
            for (Student student : studentList) {
                result.add(student.getPhone().value);
            }
            break;

        case "email":
            result.add("Email");
            for (Student student : studentList) {
                result.add(student.getEmail().value);
            }
            break;

        case "address":
            result.add("Address");
            for (Student student : studentList) {
                result.add(student.getAddress().value);
            }
            break;

        case "tags":
            result.add("Tags");
            for (Student student : studentList) {
                result.add(student.getTags().stream().map(Tag::toString).collect(Collectors.joining(" ")));
            }
            break;

        case "remark":
            result.add("Remark");
            for (Student student : studentList) {
                result.add(student.getRemark().value);
            }
            break;

        case "attendance":
            result.add("Attendance");
            for (Student student : studentList) {
                result.add(student.getAttendanceString());
            }
            break;

        case "grade":
            result.add("Grade");
            for (Student student : studentList) {
                result.add(student.getGrade().value);
            }
            break;

        default:
            result.add(columnName);
            for (int i = 0; i < studentList.size(); i++) {
                result.add("");
            }
            break;
        }

        return result;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> studentList = model.getFilteredStudentList();
        List<List<String>> toPrint = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        studentNames.add("Name");
        for (Student s : studentList) {
            studentNames.add(s.getName().fullName);
        }

        toPrint.add(studentNames);
        for (String columnName : columnList) {
            toPrint.add(generateColumn(columnName, studentList));
        }

        try {
            ExcelUtil.toExcel(toPrint);
        } catch (RuntimeException ex) {
            throw new CommandException(MESSAGE_EXCEL_OPEN);
        }

        return new CommandResult(MESSAGE_SUCCESS, false, false, false, false, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PrintCommand // instanceof handles nulls
                && columnList.equals(((PrintCommand) other).columnList));
    }

}
