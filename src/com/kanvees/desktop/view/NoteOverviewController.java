package com.kanvees.desktop.view;

import com.kanvees.desktop.InitApp;
import com.kanvees.desktop.model.Note;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.dialog.Dialogs;

public class NoteOverviewController {

    @FXML
    private TableView<Note> noteTable;
    @FXML
    private TableColumn<Note, String> noteTitleColumn;

    @FXML
    private TextField noteTitleField;
    @FXML
    private TextArea noteBodyArea;

//    @FXML
//    private Label noteTitleLabel;
//    @FXML
//    private Label noteBodyLabel;

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
            noteTitleField.setText(note.getNoteTitle());
            noteBodyArea.setText(note.getNoteBody());

        }else{
            noteTitleField.setPromptText("Enter Note Title");
            noteBodyArea.setPromptText("Enter Note Text");
        }
    }

    @FXML
    private void initialize(){

        noteTitleColumn.setCellValueFactory(cellData -> cellData.getValue().noteTitleProperty());

        showNoteDetails(null);

        noteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showNoteDetails(newValue));
    }

    public void setInitApp (InitApp initApp){
        this.initApp = initApp;

        noteTable.setItems(initApp.getNoteList());
    }


    /**
     * Handling 'Done' button (saves changes to selected note)
     */
    @FXML
    private void handleDone(){
        int selectedIndex = noteTable.getSelectionModel().getSelectedIndex();
        Note note = noteTable.getItems().get(selectedIndex);

        note.setNoteTitle(noteTitleField.getText());
        note.setNoteBody(noteBodyArea.getText());
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
}
