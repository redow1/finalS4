package tasks;

public class SubTask extends Task {
    String epicUuid;

    public SubTask(String name, String description, TaskType taskType, String epicUuid) {
        super(name, description, taskType);
        this.epicUuid = epicUuid;
    }
    public SubTask(String name, String description, String uuid, String epicUuid) {
        super(name, description, uuid);
        this.epicUuid = epicUuid;
    }

    public SubTask() {
    }

    public String getEpicUuidUuid() {
        return epicUuid;
    }

    public void setEpicUuidUuid(String EpicUuid) {
        this.epicUuid = EpicUuid;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "parrentUuid='" + epicUuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", uuid='" + uuid + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
