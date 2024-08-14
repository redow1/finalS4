package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public  class Task {
    protected String name;
    protected String description;
    protected TaskType taskType;
    protected String uuid;
    protected TaskStatus taskStatus;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, TaskType taskType, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.taskType = taskType;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    public Task() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getEpicUuid() {
        return null;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", uuid='" + uuid + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
