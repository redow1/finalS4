package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<SubTask> subTasks = new ArrayList<SubTask>();
    LocalDateTime epicEndTime;

    public Epic(String name, String description, TaskType taskType, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(name,
                description,
                taskType,
                duration,
                startTime,
                endTime);
    }

    public Epic() {
    }

}
