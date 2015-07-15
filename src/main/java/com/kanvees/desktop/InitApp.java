package com.kanvees.desktop;

import com.kanvees.desktop.model.ItemListWrapper;
import com.kanvees.desktop.model.Note;
import com.kanvees.desktop.model.Task;
import com.kanvees.desktop.view.BasicRootLayoutController;
import com.kanvees.desktop.view.NoteOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class InitApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private BasicRootLayoutController basicRootLayoutController;

    private ObservableList<Note> noteList = FXCollections.observableArrayList();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();


    public InitApp(){
    }

    public ObservableList<Note> getNoteList() {return noteList;}
    public ObservableList<Task> getTaskList() {return taskList;}

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kanvees");
        this.primaryStage.getIcons().add(new Image(String.valueOf(InitApp.class.getResource("/images/k_icon.png"))));

        initRootLayout();

        showNoteOverview();

        saveUserDataWhenAppIsClosed();
    }

    /**
     * Calls the 'Root Layout' layer
     */
    public void initRootLayout(){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InitApp.class.getResource("/BasicRootLayout.fxml"));

            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setMinHeight(450);
            primaryStage.setMinWidth(818);
            primaryStage.setScene(scene);

            BasicRootLayoutController controller = loader.getController();
            controller.setInitApp(this);

            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }

        File file = getFilePathForUser();
        if (file != null){
            loadUserDataFromFile(file);
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

            primaryStage.setTitle("Kanvees - " + file.getName());
        }else{
            prefs.remove("filePath");

            primaryStage.setTitle("Kanvees");
        }
    }

    /**
     * Load user data from a specific file. Current data will be replaced.
     */
    @SuppressWarnings("deprecation")
    public void loadUserDataFromFile(File file){

        try{
            JAXBContext appContext = JAXBContext
                    .newInstance(ItemListWrapper.class);

            Unmarshaller itemUnmarshaller = appContext.createUnmarshaller();

            ItemListWrapper itemListWrapper = (ItemListWrapper) itemUnmarshaller.unmarshal(file);

            noteList.clear();
            try {
                noteList.addAll(itemListWrapper.getNoteList());
            } catch (NullPointerException e) {}

            taskList.clear();
            try{
                taskList.addAll(itemListWrapper.getTaskList());
            } catch (NullPointerException e){}

            setFilePathForUser(file);

        } catch (Exception e) {
            Dialogs.create()
                    .title("Information")
                    .masthead("Could not find last saved file: " + file.getPath())
                    .showInformation();
        }
    }

    /**
     * Saves current user data to a specified file
     */
    @SuppressWarnings("deprecation")
    public void saveUserDataToFile(File file){

        try{
            JAXBContext appContext = JAXBContext
                    .newInstance(ItemListWrapper.class);

            Marshaller itemMarshaller = appContext.createMarshaller();

            itemMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ItemListWrapper itemListWrapper = new ItemListWrapper();
            itemListWrapper.setNoteList(noteList);
            itemListWrapper.setTaskList(taskList);

            itemMarshaller.marshal(itemListWrapper, file);


            setFilePathForUser(file);

        } catch (Exception e) {
            Dialogs.create()
                    .title("Information")
                    .masthead("Could not save data to file: " + file.getPath())
                    .showInformation();
        }
    }

    @SuppressWarnings("deprecation")
    private void saveUserDataWhenAppIsClosed(){
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                Action confirmation = Dialogs.create()
                        .title("Confirm save")
                        .masthead("Would you like to save changes?")
                        .showConfirm();
                if (confirmation == Dialog.ACTION_YES) {
                    File userFile = getFilePathForUser();

                    if (userFile.exists()) {
                        saveUserDataToFile(userFile);
                    }else{
                        FileChooser fileChooser = new FileChooser();

                        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                                "Kanvees files (*.kvd)", "*.kvd");
                        fileChooser.getExtensionFilters().add(extensionFilter);

                        File file = fileChooser.showSaveDialog(getPrimaryStage());

                        if (file != null){
                            if (!file.getPath().endsWith(".kvd")){
                                file  = new File(file.getPath() + ".kvd");
                            }
                            saveUserDataToFile(file);
                        }
                    }
                } else if (confirmation == Dialog.ACTION_NO){
                    System.exit(0);
                } else{
                    we.consume();
                }

            }
        });
    }

}
