package com.kanvees.desktop.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "noteList")
public class NoteListWrapper {

    private List<Note> noteList;

    @XmlElement(name = "note")
    public List<Note> getNoteList(){
        return noteList;
    }

    public void setNoteList(List<Note> noteList){
        this.noteList = noteList;
    }

}
