package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    String epicUuid;

    public SubTask(String name, String description, TaskType taskType, Duration duration, LocalDateTime startTime, LocalDateTime endTime, String epicUuid) {
        super(name,
                description,
                taskType,
                duration,
                startTime,
                endTime);
        this.epicUuid = epicUuid;
    }

    public SubTask() {
    }

    @Override
    public String getEpicUuid() {
        return epicUuid;
    }

    public void setEpicUuidUuid(String epicUuid) {
        this.epicUuid = epicUuid;
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
