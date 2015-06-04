package com.kanvees.desktop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Main model class for all notes
 */
public class Note {

    private final StringProperty noteTitle;
    private final StringProperty noteBody;


    /**
     * Default constuctor
     */
    public Note() {
        this (null, null);
    }

    /**
     * Constructor with initial data
     * @param noteTitle
     * @param noteBody
     */
    public Note(String noteTitle, String noteBody) {

        this.noteTitle = new SimpleStringProperty(noteTitle);
        this.noteBody = new SimpleStringProperty(noteBody);
    }

    /**
     * title getter\setter
     */
    public String getNoteTitle() {
        return noteTitle.get();
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle.set(noteTitle);
    }

    public StringProperty noteTitleProperty() {
        return noteTitle;
    }


    /**
     * body getter\setter
     */
    public String getNoteBody() {
        return noteBody.get();
    }

    public void setNoteBody(String noteBody) {
        this.noteBody.set(noteBody);
    }

    public StringProperty noteBodyProperty() {
        return noteBody;
    }
}
