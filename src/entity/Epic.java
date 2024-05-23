package entity;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    ArrayList<SubTask> subTasks = new ArrayList<SubTask>();

    public Epic(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }
    public Epic() {
    }

}
