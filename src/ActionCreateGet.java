import entity.*;

import java.util.HashMap;
import java.util.UUID;

public class ActionCreateGet {
    HashMap<String, Task> taskMap = new HashMap<>();
    final TaskManager taskManager;
    public ActionCreateGet(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public String createTask(String name, String description, TaskType taskType) {
        String uuid;
        Task task;
        task = new Task(name, description, taskType);
        task.setUuid(uUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        taskMap.put(task.getUuid(),task);
        return task.getUuid();
    }
    public String createSubTask(String name, String description, TaskType taskType,String epicUuid) {
        String uuid;
        SubTask task;
        task = new SubTask(name, description, taskType);
        task.setUuid(uUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        task.setEpicUuidUuid(epicUuid);
        taskMap.put(task.getUuid(),task);
        taskManager.lcManager.epicLC(epicUuid);
        return task.getUuid();
    }
    public String createEpic(String name, String description, TaskType taskType) {
        String uuid;
        Epic task;
        task = new Epic(name, description, taskType);
        task.setUuid(uUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        taskMap.put(task.getUuid(),task);
        taskManager.lcManager.epicLC(uuid);
        return task.getUuid();
    }

    public Task getTaskByUuid(String uuid) {
        return taskMap.get(uuid);
    }

    public HashMap getAllTasks() {
        return taskMap;
    }
    public HashMap getTaskByType(TaskType taskType) {
        HashMap<String, Task> forPrint = new HashMap<>();
        for (String string : taskMap.keySet()) {
            if (taskType.equals(taskMap.get(string).getTaskType())) {
                forPrint.put(string,taskMap.get(string));
            }
        }
        return forPrint;
    }
    public String uUIDGen() {
        return UUID.randomUUID().toString();
    }
}
