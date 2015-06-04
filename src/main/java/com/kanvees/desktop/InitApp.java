package com.kanvees.desktop;

import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.model.enums.ColorsEnum;
import com.kanvees.desktop.view.NoteOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class InitApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Note> noteList = FXCollections.observableArrayList();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();


    public InitApp(){
        noteList.add(new Note ("First Note", "Some test note text"));
        noteList.add(new Note ("Second Note", "Another text to test app"));

        taskList.add(new Task ("First Task", "first task description"));
        taskList.add(new Task ("Second Task", "second task description"));
    }

    public ObservableList<Note> getNoteList() {return noteList;}
    public ObservableList<Task> getTaskList() {return taskList;}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kanvees Desktop");

        initRootLayout();

        showNoteOverview();
    }


    /**
     * Calls the 'Root Layout' layer
     */
    public void initRootLayout() {

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InitApp.class.getResource("/BasicRootLayout.fxml"));

            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);

            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Calls the 'Note Overview' layer
     */
    public void showNoteOverview() {

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InitApp.class.getResource("/NoteOverview.fxml"));

            AnchorPane noteOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(noteOverview);

            NoteOverviewController controller = loader.getController();
            controller.setInitApp(this);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
