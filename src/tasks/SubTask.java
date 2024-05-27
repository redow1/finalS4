package tasks;

public class SubTask extends Task {
    String EpicUuid;

    public SubTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }

    public SubTask() {
    }

    public String getEpicUuidUuid() {
        return EpicUuid;
    }

    public void setEpicUuidUuid(String EpicUuid) {
        this.EpicUuid = EpicUuid;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "parrentUuid='" + EpicUuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", uuid='" + uuid + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
