package com.kanvees.desktop;

import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.NoteListWrapper;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.model.TaskListWrapper;
import com.kanvees.desktop.view.BasicRootLayoutController;
import com.kanvees.desktop.view.NoteOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

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

            BasicRootLayoutController controller = loader.getController();
            controller.setInitApp(this);

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

    /**
     * Returns the file that was last opened.
     */
    public File getFilePathForUser(){
        Preferences prefs = Preferences.userNodeForPackage(InitApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null){
            return new File(filePath);
        }else{
            return null;
        }
    }

    /**
     *Sets the file path of the currently loaded file.
     */
    public void setFilePathForUser(File file){
        Preferences prefs = Preferences.userNodeForPackage(InitApp.class);
        if (file != null){
            prefs.put("filePath", file.getPath());

            primaryStage.setTitle("Kanvees Desktop" + file.getName());
        }else{
            prefs.remove("filePath");

            primaryStage.setTitle("Kanvees Desktop");
        }
    }

    /**
     * Load user data from a specific file. Current data will be replaced.
     */
    @SuppressWarnings("deprecation")
    public void loadUserDataFromFile(File file){

        try{
            JAXBContext noteContext = JAXBContext
                    .newInstance(NoteListWrapper.class);

            Unmarshaller noteUnmarshaller = noteContext.createUnmarshaller();

            NoteListWrapper noteListWrapper = (NoteListWrapper) noteUnmarshaller.unmarshal(file);

            noteList.clear();
            noteList.addAll(noteListWrapper.getNoteList());


            JAXBContext taskContext = JAXBContext
                    .newInstance(TaskListWrapper.class);

            Unmarshaller taskUnmarshaller = taskContext.createUnmarshaller();

            TaskListWrapper taskListWrapper = (TaskListWrapper) taskUnmarshaller.unmarshal(file);

            taskList.clear();
            taskList.addAll(taskListWrapper.getTaskList());

            setFilePathForUser(file);

        } catch (JAXBException e) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not load data from file: " + file.getPath());
            e.printStackTrace();
        }
    }

    /**
     * Saves current user data to a specified file
     */
    @SuppressWarnings("deprecation")
    public void saveUserDataToFile(File file){

        try{
            JAXBContext noteContext = JAXBContext
                    .newInstance(NoteListWrapper.class);

            Marshaller noteMarshaller = noteContext.createMarshaller();

            noteMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            NoteListWrapper noteListWrapper = new NoteListWrapper();
            noteListWrapper.setNoteList(noteList);

            noteMarshaller.marshal(noteListWrapper, file);


            JAXBContext taskContext = JAXBContext
                    .newInstance(TaskListWrapper.class);

            Marshaller taskMarshaller = taskContext.createMarshaller();

            taskMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskListWrapper taskListWrapper = new TaskListWrapper();
            taskListWrapper.setTaskList(taskList);

            taskMarshaller.marshal(taskListWrapper, file);

            setFilePathForUser(file);

        } catch (JAXBException e) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not save data to file: " + file.getPath());
            e.printStackTrace();
        }
    }

}
