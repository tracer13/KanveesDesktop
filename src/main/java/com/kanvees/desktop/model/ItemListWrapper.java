package com.kanvees.desktop.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "itemList")
public class ItemListWrapper {

    private List<Note> noteList;
    private List<Task> taskList;

    @XmlElement(name = "note")
    public List<Note> getNoteList(){return noteList;}

    @XmlElement(name = "task")
    public List<Task> getTaskList() {return taskList;}

    public void setNoteList(List<Note> noteList){this.noteList = noteList;}

    public void setTaskList(List<Task> taskList){this.taskList = taskList;}

}
