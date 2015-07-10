package com.kanvees.desktop.view;

import com.kanvees.desktop.InitApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class BasicRootLayoutController {

    private InitApp initApp;

    public void setInitApp(InitApp initApp){
        this.initApp = initApp;
    }

    /**
     * Handling 'File' -> 'New...' button
     */
    @FXML
    private void handleNew(){

        initApp.getNoteList().clear();
        initApp.getTaskList().clear();

        initApp.setFilePathForUser(null);
    }

    /**
     * Handling 'File' -> 'Open' button
     */
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Kanvees files (*.kvd)", "*.kvd");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(initApp.getPrimaryStage());

        if (file != null){
            initApp.loadUserDataFromFile(file);
        }
    }

    /**
     * Handling 'File' -> 'Save' button.
     * If there is no open file 'Save as...' dialog is shown
     */
    @FXML
    private void handleSave(){

        File userFile = initApp.getFilePathForUser();

        if (userFile != null){
            initApp.saveUserDataToFile(userFile);
        }else{
            handleSaveAs();
        }
    }

    /**
     * Handling 'File' -> 'Save As...' button
     */
    @FXML
    public void handleSaveAs(){

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Kanvees files (*.kvd)", "*.kvd");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(initApp.getPrimaryStage());

        if (file != null){
            if (!file.getPath().endsWith(".kvd")){
                file  = new File(file.getPath() + ".kvd");
            }
            initApp.saveUserDataToFile(file);
        }
    }

    /**
     * Handling 'Exit' button
     */
    @FXML
    private void handleExit(){
        handleSave();
        System.exit(0);
    }
}
