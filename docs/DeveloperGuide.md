---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2122S1-CS2103T-W10-2/tp/tree/master/docs/diagrams) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `StudentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Student` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `TeachBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a student).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `TeachBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `TeachBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the teach book data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `TeachBook`, which `Student` references. This allows `TeachBook` to only require one `Tag` object per unique tag, instead of each `Student` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-W10-2/tp/blob/master/src/main/java/seedu/teachbook/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both teach book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `TeachBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.teachbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Integration of class feature

#### Design considerations

**Aspect: Structure of the new model component**

To integrate the new class feature into the existing AB3 product, we decided that each student object should have a reference to its class, and there should only be one class object for the same class. We considered and compared a few designs of the rest of the model component:

* **Alternative 1 (current choice):** `TeachBook` maintains a list of all classes. Each class maintains a list of all students in that class. A unique student list containing all students in all classes is generated every time when users execute the `list all` command.
  * Pros
    * When adding/deleting students, we only need to add/delete ONCE using a class's student list at most times.
    * The filtered student list inside `ModelManager` can take the student list of the currently selected class as its source directly.
  * Cons
    * When `list all`, we need to construct the unique student list by iterating through each class's student list, which can degrade the performance of the `list all` command.

* **Alternative 2:** `TeachBook` maintains a list of all classes. Each class maintains a list of all students in that class. `TeachBook` also maintains a unique student list containing all students in all classes.
  * Pros
    * The filtered student list inside `ModelManager` can still take the student list of the currently selected class as its source directly.
    * When `list all`, the filtered student list can also take the maintained unique student list as its source directly.
  * Cons
    * When adding/deleting students, we always need to add/delete TWICE. This means we need to modify both a class's student list and the unique student list.
    * If we simply add/delete students of the unique student list without maintaining a specific order of all the students, the list can look messy when `list all`. We may still need to do a sorting by class when `list all`, which actually also degrades the performance of `list all`.

* **Alternative 3:** `TeachBook` only maintains a unique student list containing all students in all classes.
  * Pros
    * Easiest to implement and most components can be reused from AB3.
    * When adding/deleting students, we only need to add/delete ONCE using the unique student list.
  * Cons
    * Similar to _Alternative 2_, there is still the need to maintain the order of students in the unique student list.
    * We always need a predicate to screen out students of the currently selected class. Since users may interact with a specific class at most times, this can degrade the performance of most commands.

### Delete class feature

#### Implementation

The delete class feature allows users to delete a class and all students in the class from the TeachBook. This feature is facilitated by `DeleteClassCommand` and `DeleteClassCommandParser`.

Given below is an example usage scenario and how the delete class mechanism behaves.

The following object diagram shows an example initial state of the TeachBook:

<img src="images/DeleteClassObjectDiagram0.png" width="520" />

The following sequence diagram shows interactions within the `Logic` component and part of the `Model` component for the `deleteClass B` command:

![Interactions Inside the Logic and Model Components for the `deleteClass B` Command](images/DeleteClassSequenceDiagram.png)

The following object diagram shows the updated TeachBook:

<img src="images/DeleteClassObjectDiagram1.png" width="280" />

#### Design considerations

_{To be updated later}_

### Synchronization of Student List in Model and UI

To ensure synchronization throughout the program, `ModelManager` maintains a `filteredStudents` observable, which is 
observed by `MainWindow`. `filteredStudents` contains the list of students to be displayed in the UI.

![UiAndModel](images/UiAndModel.png)

`SelectCommand` and `ListCommand` with the `all` option i.e., `list all` are the only two commands that will modify the
`filteredStudents` entirely, i.e., a new observable is created and replaces the existing observable, via 
`ModelManager#updateSourceOfFilteredStudentList()`. However, this change is not observed by `MainWindow` as `MainWindow`
only observes changes within the observable, i.e., the previous `filteredStudents` observable. To mitigate this issue,
the `updateStudentListPanel` flag, in the `commandResult` returned after the execution of both `SelectCommand` and 
`ListCommand` with the `all` option, is set to `true`. The flag then triggers the `MainWindow` to retrieve the new 
`filteredStudents` observable via the `MainWindow#updateStudentListPannel()` and start observing changes in the new 
observable. Thereafter, the student list in the UI is again in sync with the student list in the Model.

Below is the sequence diagram of the execution of the `SelectCommand`.

![SelectSequenceDiagram](images/SelectSequenceDiagram.png)

Below is the sequence diagram of the execution of the `ListCommand` with the `all` option.

![ListAllSequenceDiagram](images/ListAllSequenceDiagram.png)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedTeachBook`. It extends `TeachBook` with an undo/redo history, stored internally as an `teachBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedTeachBook#commit()` — Saves the current teach book state in its history.
* `VersionedTeachBook#undo()` — Restores the previous teach book state from its history.
* `VersionedTeachBook#redo()` — Restores a previously undone teach book state from its history.

These operations are exposed in the `Model` interface as `Model#commitTeachBook()`, `Model#undoTeachBook()` and `Model#redoTeachBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedTeachBook` will be initialized with the initial teach book state, and the `currentStatePointer` pointing to that single teach book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th student in the teach book. The `delete` command calls `Model#commitTeachBook()`, causing the modified state of the teach book after the `delete 5` command executes to be saved in the `teachBookStateList`, and the `currentStatePointer` is shifted to the newly inserted teach book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David ...` to add a new student. The `add` command also calls `Model#commitTeachBook()`, causing another modified teach book state to be saved into the `teachBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitTeachBook()`, so the teach book state will not be saved into the `teachBookStateList`.

</div>

Step 4. The user now decides that adding the student was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoTeachBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous teach book state, and restores the teach book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial TeachBook state, then there are no previous TeachBook states to restore. The `undo` command uses `Model#canUndoTeachBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoTeachBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the teach book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `teachBookStateList.size() - 1`, pointing to the latest teach book state, then there are no undone TeachBook states to restore. The `redo` command uses `Model#canRedoTeachBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the teach book, such as `list`, will usually not call `Model#commitTeachBook()`, `Model#undoTeachBook()` or `Model#redoTeachBook()`. Thus, the `teachBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitTeachBook()`. Since the `currentStatePointer` is not pointing at the end of the `teachBookStateList`, all teach book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David ...` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire teach book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the student being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* a primary school teacher teaching mathematics
* has a need to manage 4 class worth of student's contacts which she is teaching
* prefer desktop apps over other types
* not proficient in IT but can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* wants to keep her work life and personal life separate
* often face problem of mixing up students with the same name when she adds it into her phone contact list
* have to also add emergency contact into her phone contact list which makes it flooded, which is hard to find or link it with the correct student
* would prefer to have an app that can also store other emergency information (e.g. blood type and allergies) all in one place

**Value proposition**: Manage contacts faster than a typical mouse/GUI driven app. Allows teachers to have a dedicated teach book to keep their work life separated from their personal life. Allows teacher to find students and their emergency information accurately and easily.


### User stories

Priorities: High (must have) - `* * *` , Medium (nice to have) - `* *` , Low (unlikely to have) - `*`

| Priority | As a ...                                                  | I want to ...                                                                                   | So that I can ...                                                                        |
| -------- | --------------------------------------------------------- | ----------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| `* * *`  | new user                                                  | see usage instructions                                                                          | refer to instructions when I forget how to use the App                                   |
| `* * *`  | teacher                                                   | add students                                                                                    |                                                                                          |
| `* *`    | teacher                                                   | modify contacts                                                                                 | change information easily rather than creating a new contact to replace the previous one |
| `* *`    | teacher with students whom require special attention      | add important information about my students such as diet, allergy and existing health condition | I can quickly react to any medical emergency                                             |
| `* *`    | teacher who likes to keep work and personal life separate | have separate personal contacts with school contacts                                            | my contact list won't be overpopulated                                                   |
| `* *`    | teacher                                                   | easily contact students' parents                                                                | the parents can address to any matters as soon as possible                               |
| `* * *`  | teacher                                                   | delete students with specific ID                                                                | remove specific students who are no longer in my class                                   |
| `* * *`  | teacher                                                   | find a student by name                                                                          | locate details of students without having to go through the entire list                  |
| `* *`    | teacher                                                   | sort students by name                                                                           | locate a student easily                                                                  |
| `* *`    | teacher                                                   | Add all students from a class at once                                                           | quickly add the information of the students in each class.                               |
| `* *`    | teacher                                                   | delete all students from a class at once                                                        | quickly clean up the TeachBook when I no longer teach s class                            |
| `* *`    | teacher                                                   | filter all students by tag                                                                      | easily locate all students with the same tag (probably having something in common)       |
| `*`      | teacher who wants to remember students I have taught      | remove all students I no longer teach but keep a record of the list in another file             | start over with a clean slate and can retrieve records I need in the future              |
| `* *`    | teacher                                                   | undo the most recent command                                                                    | easily revert everything to the previous state                                           |
| `*`      | teacher                                                   | remove all students from the contact                                                            | clear my contact in one go                                                               |
| `* *`    | teacher                                                   | separate personal contacts with school contacts                                                 | prevent my contact list from overpopulating                                              |
| `* *`    | teacher                                                   | assign a class role to a student                                                                | identify students through their class role                                               |
| `* *`    | teacher                                                   | assign multiple class roles to a student                                                        | need not to assign class roles to student one at a time                                  |
| `*`      | teacher                                                   | view the list of all students                                                                   | have an overview of all my students                                                      |
| `*`      | teacher                                                   | view the information of a student                                                               | take a closer look at a student's information                                            |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `TeachBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC?? - Delete a Student / Students**

MSS:

1. User <ins>list all the students (UC??)</ins>.
2. User requests to delete a specific student / specific students in the list by giving ID(s).
3. TeachBook deletes the student(s).

    Use case ends.

Extensions:

* 1a. The list is empty.

  Use case ends.

* 2a. The given ID(s) is/are invalid.

    * 2a1. For all valid ID(s), TeachBook deletes the student(s).
    * 2a2. For all invalid ID(s), TeachBook shows an error message.

      Use case resumes at step 1.

<br>

**Use case: UC?? - List All the Students**

MSS:

1. User requests to list students.
2. TeachBook shows a list of all the students.

   Use case ends.

**Use case: UC?? - Tag a Student**

MSS:

1. Teacher assigns a class role to a student.
2. TeachBook displays the student with the corresponding class role.

   Use case ends.

Extensions:

* 1a. Student does not exist.
   * 1a1. TeachBook displays error.
  
     Use case ends.

*{More to be added}*

### Non-Functional Requirements

1. The app should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. The TeachBook should be able to hold up to 1000 students without a noticeable sluggishness (being able to respond to any command within 3 seconds) in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The source code should be open source.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Class number**: A letter A, B, C, ...
* **Student number**: A positive integer 1, 2, 3, ...
* **ID**: A serial number assigned to a student when he/she is added to the TeachBook. **ID** is made up of a student's _class number_ and his/her _student number_ in the class,
e.g. if a student is from class A and has student number 2, then the student’s ID would be A2.
* **Currently selected class**:

*{More to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases ... }_

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. Multiple students in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No student is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases ... }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases ... }_
