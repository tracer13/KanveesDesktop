package com.kanvees.desktop.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Task {

    private final StringProperty taskTitle;
    private final StringProperty taskBody;
    private final StringProperty colorLabel;
    private final ObjectProperty<LocalDateTime> endTime;
    private final StringProperty importance;
    private final BooleanProperty isRepetitive;

    /**
     * Default constructor
     */
    public Task () {
        this (null, null);
    }

    /**
     * Constructor with initial data
     * @param taskTitle
     * @param taskBody
     */
    public Task(String taskTitle, String taskBody) {
        this.taskTitle = new SimpleStringProperty(taskTitle);
        this.taskBody = new SimpleStringProperty(taskBody);

        this.colorLabel = new SimpleStringProperty("white");
        this.endTime = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.of(2015, 03, 31, 22, 00, 00));
        this.importance = new SimpleStringProperty("Regular");

        this.isRepetitive = new SimpleBooleanProperty(false);
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
    public String getTaskBody() {
        return taskBody.get();
    }

    public StringProperty taskBodyProperty() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody.set(taskBody);
    }


    /**
     * color label getter\setter
     */
    public String getColorLabel() {
        return colorLabel.get();
    }

    public StringProperty colorLabelProperty() {
        return colorLabel;
    }

    public void setColorLabel(String colorLabel) {
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
    public String getImportance() {
        return importance.get();
    }

    public StringProperty importanceProperty() {
        return importance;
    }

    public void setImportance(String importance) {
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
}
