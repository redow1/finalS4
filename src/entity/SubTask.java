package entity;

public class SubTask extends AbstractTask {
    String parrentUuid;

    public SubTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }

    public SubTask() {
    }

    public String getParrentUuid() {
        return parrentUuid;
    }

    public void setParrentUuid(String parrentUuid) {
        this.parrentUuid = parrentUuid;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "parrentUuid='" + parrentUuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", uuid='" + uuid + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
