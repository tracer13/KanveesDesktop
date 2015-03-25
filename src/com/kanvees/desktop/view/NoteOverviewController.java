package com.kanvees.desktop.view;

import com.kanvees.desktop.InitApp;
import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.model.enums.ImportanceEnum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    private TextField taskTitleField;
    @FXML
    private TextArea taskDescriptionArea;
    @FXML
    private  ChoiceBox importanceChoiceBox;

//    private ImportanceEnum importanceEnum;


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
    }

    private void showTaskDetails(Task task){

        importanceChoiceBox.getItems().setAll(ImportanceEnum.values());

        if (task != null){
            noteAnchorPane.setVisible(false);
            taskAnchorPane.setVisible(true);
            taskTitleField.setText(task.getTaskTitle());
            taskDescriptionArea.setText(task.getTaskDescription());
            importanceChoiceBox.setValue(task.getImportance());
        }else{
            importanceChoiceBox.setValue(ImportanceEnum.REGULAR);
        }
    }

    @FXML
    private void initialize(){

        noteTitleColumn.setCellValueFactory(cellData -> cellData.getValue().noteTitleProperty());
        taskTitleColumn.setCellValueFactory(cellData -> cellData.getValue().taskTitleProperty());

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
    }

    /**
     * Handling 'Delete' button (delete selected note)
     */
    @SuppressWarnings("deprecation")
    @FXML
    private void handleDelete(){
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
    }

    /**
     * Handling 'Create New...'->'SimpleNote' button
     */
    @FXML
    private void handleCreateSimpleNote(){
        Note tempNote = new Note();
        noteTable.getItems().add(tempNote);
        tabPane.getSelectionModel().select(0);
        int noteIndex = noteTable.getItems().size()-1;
        noteTable.getSelectionModel().select(noteIndex);
    }

    /**
     * Handling 'Create New...'->'Task' button
     * !!!Not developed yet
     */
    @SuppressWarnings("deprecation")
    @FXML
    private void handleCreateTask(){
        tabPane.getSelectionModel().select(1);
        Dialogs.create()
                .title("Notification")
                .masthead("This function is currently under development")
                .showInformation();
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





}
