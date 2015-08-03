package com.kanvees.desktop.view;

import com.kanvees.desktop.InitApp;
import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.model.enums.ColorsEnum;
import com.kanvees.desktop.model.enums.ImportanceEnum;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;

public class NoteOverviewController {

    @FXML
    private TabPane tabPane;

    @FXML
    private AnchorPane taskPane;

    /**
     * Declaring fields for 'Notes'
     */
    @FXML
    private AnchorPane noteAnchorPane;
    @FXML
    private TableView<Note> noteTable;
    @FXML
    private TableColumn<Note, String> noteTitleColumn;
    @FXML
    private TextField noteTitleField;
    @FXML
    private TextArea noteBodyArea;

    /**
     * Declaring fields for Tasks
     */
    @FXML
    private AnchorPane taskAnchorPane;
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> taskTitleColumn;
    @FXML
    private TableColumn<Task, String> taskImportanceColumn;
    @FXML
    private TableColumn<Task, ColorsEnum> taskColorLabelColumn;
    @FXML
    private TextField taskTitleField;
    @FXML
    private TextArea taskDescriptionArea;
    @FXML
    private ChoiceBox importanceChoiceBox;
    @FXML
    private ComboBox colorComboBox;
    @FXML
    private Button closeTaskButton;
    @FXML
    private Button reopenTaskButton;


    private InitApp initApp;

    /**
     * this cunstructor is called before initialize() method
     */
    public NoteOverviewController() {
    }

    @FXML
    private void initialize(){

        noteTitleColumn.setCellValueFactory(cellData -> cellData.getValue().noteTitleProperty());
        taskTitleColumn.setCellValueFactory(cellData -> cellData.getValue().taskTitleProperty());
        taskImportanceColumn.setCellValueFactory(cellData -> cellData.getValue().importanceStringProperty());
        taskImportanceColumn.setStyle("-fx-alignment: center; -fx-text-fill: red; -fx-font-weight: bold");

        //defines that value of taskColorLabelColumn will be taken from Task(model) colorLabel property
        taskColorLabelColumn.setCellValueFactory(new PropertyValueFactory<Task, ColorsEnum>("colorLabel"));
        setColorCellValues();

        showNoteDetails(null);
        showTaskDetails(null);

        noteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showNoteDetails(newValue));
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

        tabChangeListener();
    }

    /**
     * shows details of an existing or new note
     */
    private void showNoteDetails(Note note){

        if (note != null){
            taskAnchorPane.setVisible(false);
            noteAnchorPane.setVisible(true);
            noteTitleField.setText(note.getNoteTitle());
            noteBodyArea.setText(note.getNoteBody());

        }else{
            noteTitleField.setPromptText("Enter Note Title");
            noteBodyArea.setPromptText("Enter Note Text");
        }

        checkEmptyNotes();
    }

    /**
     * shows details of an existing or new task
     */
    private void showTaskDetails(Task task){

        importanceChoiceBox.getItems().setAll(ImportanceEnum.values());
        setColorFillInComboBox();

        if (task != null){
            noteAnchorPane.setVisible(false);
            taskAnchorPane.setVisible(true);
            taskTitleField.setText(task.getTaskTitle());
            taskDescriptionArea.setText(task.getTaskDescription());
            importanceChoiceBox.setValue(task.getImportance());
            task.setImportanceString(task.getImportance().getStringValue());
            colorComboBox.setValue(task.getColorLabel());
        }else{
            taskTitleField.setPromptText("Enter task title");
            taskDescriptionArea.setPromptText("Enter task description");
            importanceChoiceBox.setValue(ImportanceEnum.REGULAR);
            colorComboBox.setValue(ColorsEnum.TRANSPARENT);

        }

        checkEmptyTasks();
        checkIfTaskIsDone();
    }

    public void setInitApp (InitApp initApp){
        this.initApp = initApp;

        noteTable.setItems(initApp.getNoteList());
        taskTable.setItems(initApp.getTaskList());
    }

    /**
     * changes tabPane listener. When switching between tabs, content is cleared.
     */
    @SuppressWarnings("deprecation")
    private void tabChangeListener(){
        tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
                    selectionRemover();
                    taskAnchorPane.setVisible(false);
                } else if (tabPane.getSelectionModel().getSelectedIndex() == 1) {
                    selectionRemover();
                    noteAnchorPane.setVisible(false);
                } else if (tabPane.getSelectionModel().getSelectedIndex() == 2) {
                    Dialogs.create()
                            .title("Notification")
                            .masthead("This function is currently under development")
                            .showInformation();
                    tabPane.getSelectionModel().selectFirst();
                }
            }
        });
    }

    /**
     * removes selection from currently unused tabs
     */
    private void selectionRemover(){
        if(tabPane.getSelectionModel().getSelectedIndex()==0 && !taskTable.getSelectionModel().isEmpty()){
            taskTable.getSelectionModel().clearSelection();
        }else if(tabPane.getSelectionModel().getSelectedIndex()==1 && !noteTable.getSelectionModel().isEmpty()){
            noteTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * Performs check if there are null empty tasks (title and description null or empty)
     * is used in showTaskDetails()
     */
    private void checkEmptyTasks(){
        for (int i = 0; i <= taskTable.getItems().size()-1; i++){
            if (taskTable.getSelectionModel().getSelectedIndex() != i &&
                    (taskTable.getItems().get(i).getTaskTitle() == null || taskTable.getItems().get(i).getTaskTitle().isEmpty()) &&
                    (taskTable.getItems().get(i).getTaskDescription() == null || taskTable.getItems().get(i).getTaskDescription().isEmpty())){

                taskTable.getItems().remove(i);
            }
        }
    }


    /**
     * Performs check if there are null or empty notes (title and description null or empty)
     * is used in showNoteDetails()
     */
    private void checkEmptyNotes(){
        for (int i = 0; i <= noteTable.getItems().size()-1; i++) {
            if (noteTable.getSelectionModel().getSelectedIndex() != i &&
                    (noteTable.getItems().get(i).getNoteTitle() == null || noteTable.getItems().get(i).getNoteTitle().isEmpty()) &&
                    (noteTable.getItems().get(i).getNoteBody() == null || noteTable.getItems().get(i).getNoteBody().isEmpty())) {

                noteTable.getItems().remove(i);
            }
        }
    }

    /**
     * Sets values for colorComboBox pop-up and button
     */
    private void setColorFillInComboBox() {

        colorComboBox.getItems().setAll(ColorsEnum.values());
        Callback<ListView<ColorsEnum>, ListCell<ColorsEnum>> factory = new Callback<ListView<ColorsEnum>, ListCell<ColorsEnum>>() {
            @Override
            public ListCell<ColorsEnum> call(ListView<ColorsEnum> param) {
                return new ListCell<ColorsEnum>(){
                    private final Rectangle rectangle;
                    {
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        rectangle = new Rectangle(35, 15);
                    }
                    @Override
                    protected void updateItem(ColorsEnum item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        }else {
                            rectangle.setFill(Color.web(item.toString()));
                            setGraphic(rectangle);
                        }
                    }
                };
            }
        };
        colorComboBox.setCellFactory(factory);
        colorComboBox.setButtonCell(factory.call(null));
    }


    /**
     * method redefines setCellFactory() for taskColorLabelColumn. Is used in initialize together with setCellValueFactory
     */
    private void setColorCellValues() {
        taskColorLabelColumn.setCellFactory(new Callback<TableColumn<Task, ColorsEnum>, TableCell<Task, ColorsEnum>>() {
            @Override
            public TableCell<Task, ColorsEnum> call(TableColumn<Task, ColorsEnum> param) {
                return new TableCell<Task, ColorsEnum>() {
                    Rectangle rectangle = new Rectangle(7,18);
                    @Override
                    public void updateItem(ColorsEnum item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty){
                            setGraphic(null);
                        } else {
                            rectangle.setFill(Color.web(item.toString()));
                            setGraphic(rectangle);
                        }
                    }
                };
            }
        });
    }


    /**
     * performs a check indicating if task is done or not, disables and enables layers
     */
    private void checkIfTaskIsDone() {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Task task = taskTable.getItems().get(selectedIndex);
            if (task.getIsClosed()) {
                taskPane.setDisable(true);
                closeTaskButton.setVisible(false);
                reopenTaskButton.setVisible(true);
            }else{
                taskPane.setDisable(false);
                closeTaskButton.setVisible(true);
                reopenTaskButton.setVisible(false);
            }
        }
    }

    /**
     * saves current task (is used in handleCloseTask() instead of handleSaveTask() method)
     */
    private void taskSaver(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        Task task = taskTable.getItems().get(selectedIndex);
        task.setTaskTitle(taskTitleField.getText());
        task.setTaskDescription(taskDescriptionArea.getText());
        task.setImportance((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem());
        task.setImportanceString(((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem()).getStringValue());
        task.setColorLabel((ColorsEnum) colorComboBox.getSelectionModel().getSelectedItem());
    }

    /**
     * Handling 'Save' button (saves changes to selected NOTE)
     */
    @FXML
    @SuppressWarnings("deprecation")
    private void handleSaveNote(){
        int selectedIndex = noteTable.getSelectionModel().getSelectedIndex();
        Note note = noteTable.getItems().get(selectedIndex);

        if (noteTitleField.getText()==null || noteTitleField.getText().isEmpty()){
            Dialogs.create()
                    .title("Information")
                    .masthead("Please fill in note title")
                    .showInformation();
        }else{
            note.setNoteTitle(noteTitleField.getText());
            note.setNoteBody(noteBodyArea.getText());
            Dialogs.create()
                    .title("Information")
                    .masthead("Note has been saved!")
                    .showInformation();
        }
    }

    /**
     * Handling 'Save' button (saves changes to selected TASK)
     */
    @FXML
    @SuppressWarnings("deprecation")
    private void handleSaveTask(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        Task task = taskTable.getItems().get(selectedIndex);

        if (taskTitleField.getText()==null || taskTitleField.getText().isEmpty()){
            Dialogs.create()
                    .title("Information")
                    .masthead("Please fill in task title")
                    .showInformation();
        }else {
            task.setTaskTitle(taskTitleField.getText());
            task.setTaskDescription(taskDescriptionArea.getText());
            task.setImportance((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem());
            task.setImportanceString(((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem()).getStringValue());
            task.setColorLabel((ColorsEnum) colorComboBox.getSelectionModel().getSelectedItem());
            Dialogs.create()
                    .title("Information")
                    .masthead("Task has been saved!")
                    .showInformation();
        }
    }

    /**
     * Handling 'Delete' button (delete selected note or task)
     */
    @SuppressWarnings("deprecation")
    @FXML
    private void handleDelete(){
        if (tabPane.getSelectionModel().isSelected(0)){
            int selectedIndex = noteTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >=0){
                noteTable.getItems().remove(selectedIndex);
                noteAnchorPane.setVisible(false);
                noteTable.getSelectionModel().clearSelection();
                Dialogs.create()
                        .title("Information")
                        .masthead("Note has been removed")
                        .showInformation();
            }else{
                Dialogs.create()
                        .title("Information")
                        .masthead("Note is not selected")
                        .message("Please select a note to delete")
                        .showInformation();
            }
        } else if (tabPane.getSelectionModel().isSelected(1)){
            int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >=0){
                taskTable.getItems().remove(selectedIndex);
                taskAnchorPane.setVisible(false);
                taskTable.getSelectionModel().clearSelection();
                Dialogs.create()
                        .title("Information")
                        .masthead("Task has been removed")
                        .showInformation();
            }else{
                Dialogs.create()
                        .title("Information")
                        .masthead("Task is not selected")
                        .message("Please select a task to delete")
                        .showInformation();
            }
        }
    }

    /**
     * Handling 'Create New...'->'SimpleNote' button
     */
    @FXML
    private void handleCreateSimpleNote(){
        Note tempNote = new Note();
        noteTable.getItems().add(tempNote);
        tabPane.getSelectionModel().select(0);
        int tempNoteIndex = noteTable.getItems().size()-1;
        noteTable.getSelectionModel().select(tempNoteIndex);
    }

    /**
     * Handling 'Create New...'->'Task' button
     *
     */
    @SuppressWarnings("deprecation")
    @FXML
    private void handleCreateTask(){
        Task tempTask = new Task();
        taskTable.getItems().add(tempTask);
        tabPane.getSelectionModel().select(1);
        int taskIndex = taskTable.getItems().size()-1;
        taskTable.getSelectionModel().select(taskIndex);
    }

    /**
     * Handling 'Create New...'->'To-Do' button
     * !!!Not developed yet
     */
    @SuppressWarnings("deprecation")
    @FXML
    private void handleCreateToDo(){
        tabPane.getSelectionModel().select(2);
        Dialogs.create()
                .title("Notification")
                .masthead("This function is currently under development")
                .showInformation();
    }

    /**
     * handling 'Close Task' button
     */
    @FXML
    private void handleCloseTask() {
        taskSaver();

        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        Task task = taskTable.getItems().get(selectedIndex);

        task.setIsClosed(true);
        taskPane.setDisable(true);
        closeTaskButton.setVisible(false);
        reopenTaskButton.setVisible(true);

    }

    /**
     * handling 'Reopen Task' button
     */
    @FXML
    private void handleReopenTask() {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        Task task = taskTable.getItems().get(selectedIndex);

        task.setIsClosed(false);
        taskPane.setDisable(false);
        closeTaskButton.setVisible(true);
        reopenTaskButton.setVisible(false);
    }

}