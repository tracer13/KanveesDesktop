package com.kanvees.desktop.view;

import com.kanvees.desktop.InitApp;
import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.model.enums.ColorsEnum;
import com.kanvees.desktop.model.enums.ImportanceEnum;
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
    private  ChoiceBox importanceChoiceBox;
    @FXML
    private ComboBox colorComboBox;

    private InitApp initApp;

    /**
     * this cunstructor is called before initialize() method
     */
    public NoteOverviewController() {
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
    }

    public void setInitApp (InitApp initApp){
        this.initApp = initApp;

        noteTable.setItems(initApp.getNoteList());
        taskTable.setItems(initApp.getTaskList());
    }


    /**
     * Handling 'Save' button (saves changes to selected NOTE)
     */
    @FXML
    private void handleSaveNote(){
        int selectedIndex = noteTable.getSelectionModel().getSelectedIndex();
        Note note = noteTable.getItems().get(selectedIndex);

        note.setNoteTitle(noteTitleField.getText());
        note.setNoteBody(noteBodyArea.getText());
    }

    /**
     * Handling 'Save' button (saves changes to selected TASK)
     */
    @FXML
    private void handleSaveTask(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        Task task = taskTable.getItems().get(selectedIndex);

        task.setTaskTitle(taskTitleField.getText());
        task.setTaskDescription(taskDescriptionArea.getText());
        task.setImportance((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem());
        task.setImportanceString(((ImportanceEnum) importanceChoiceBox.getSelectionModel().getSelectedItem()).getStringValue());
        task.setColorLabel((ColorsEnum) colorComboBox.getSelectionModel().getSelectedItem());
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
            }else{
                Dialogs.create()
                        .title("Error")
                        .masthead("Note is not selected")
                        .message("Please select a note to delete")
                        .showWarning();
            }
        } else if (tabPane.getSelectionModel().isSelected(1)){
            int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >=0){
                taskTable.getItems().remove(selectedIndex);
            }else{
                Dialogs.create()
                        .title("Error")
                        .masthead("Task is not selected")
                        .message("Please select a task to delete")
                        .showWarning();
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
                        if (empty){
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
}