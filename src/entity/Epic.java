package entity;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends AbstractTask{
    ArrayList<SubTask> subTasks = new ArrayList<SubTask>();
    int counterDoneTaks;
    public Epic(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }
    public Epic() {
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public int getCounterDoneTaks() {
        return counterDoneTaks;
    }

    public void setCounterDoneTaks(int counterDoneTaks) {
        this.counterDoneTaks = counterDoneTaks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", counterDoneTaks=" + counterDoneTaks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", uuid='" + uuid + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
