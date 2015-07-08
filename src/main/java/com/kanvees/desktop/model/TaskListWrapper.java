package com.kanvees.desktop.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "taskList")
public class TaskListWrapper {

    private List<Task> taskList;

    @XmlElement(name = "task")
    public List<Task> getTaskList(){
        return taskList;
    }

    public void setTaskList(List<Task> taskList){
        this.taskList = taskList;
    }

}
