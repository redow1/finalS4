package tasks;

import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<SubTask> subTasks = new ArrayList<SubTask>();

    public Epic(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }
    public Epic() {
    }

}
