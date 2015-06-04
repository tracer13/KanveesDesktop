package com.kanvees.desktop.model;

import com.kanvees.desktop.model.enums.ColorsEnum;
import com.kanvees.desktop.model.enums.ImportanceEnum;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Task {

    private final StringProperty taskTitle;
    private final StringProperty taskDescription;
    private final ObjectProperty<ColorsEnum> colorLabel;
    private final ObjectProperty<LocalDateTime> endTime;
    private final ObjectProperty<ImportanceEnum> importance;
    private final BooleanProperty isRepetitive;

    private final StringProperty importanceString;

    /**
     * Default constructor
     */
    public Task () {
        this (null, null);
    }

    /**
     * Constructor with initial data
     * @param taskTitle
     * @param taskDescription
     */
    public Task(String taskTitle, String taskDescription) {
        this.taskTitle = new SimpleStringProperty(taskTitle);
        this.taskDescription = new SimpleStringProperty(taskDescription);

        this.colorLabel = new SimpleObjectProperty<ColorsEnum>(ColorsEnum.TRANSPARENT);
        this.endTime = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.of(2015, 03, 31, 22, 00, 00));
        this.importance = new SimpleObjectProperty<ImportanceEnum>(ImportanceEnum.REGULAR);

        this.isRepetitive = new SimpleBooleanProperty(false);

        this.importanceString = new SimpleStringProperty("");
    }

    /**
     * title getter\setter
     */
    public String getTaskTitle() {
        return taskTitle.get();
    }

    public StringProperty taskTitleProperty() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle.set(taskTitle);
    }


    /**
     * body getter\setter
     */
    public String getTaskDescription() {
        return taskDescription.get();
    }

    public StringProperty taskDescriptionProperty() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription.set(taskDescription);
    }


    /**
     * color label getter\setter
     */
    public ColorsEnum getColorLabel() {
        return colorLabel.get();
    }

    public ObjectProperty<ColorsEnum> colorLabelProperty() {
        return colorLabel;
    }

    public void setColorLabel(ColorsEnum colorLabel) {
        this.colorLabel.set(colorLabel);
    }


    /**
     * end time getter\setter
     */
    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public ObjectProperty<LocalDateTime> endTimeProperty() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    /**
     * importance getter\setter
     */
    public ImportanceEnum getImportance() {
        return importance.get();
    }

    public ObjectProperty<ImportanceEnum> importanceProperty() {
        return importance;
    }

    public void setImportance(ImportanceEnum importance) {
        this.importance.set(importance);
    }

    /**
     * isRepetitive bool getter\setter
     */
    public boolean getIsRepetitive() {
        return isRepetitive.get();
    }

    public BooleanProperty isRepetitiveProperty() {
        return isRepetitive;
    }

    public void setIsRepetitive(boolean isRepetitive) {
        this.isRepetitive.set(isRepetitive);
    }


    public  String getImportanceString() {
        return importanceString.get();
    }

    public StringProperty importanceStringProperty() {
        return importanceString;
    }

    public void setImportanceString(String importanceString) {
        this.importanceString.set(getImportance().getStringValue());
    }
}