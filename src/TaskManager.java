import entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TransferQueue;

public class TaskManager {

    ActionCreateGet actionCreateGet = new ActionCreateGet(this);
    public ActionCreateGet actionCreateGet() {
        return actionCreateGet;
    }
    ActionDelete actionDelete = new ActionDelete(this);
    public ActionDelete actionDelete() {
        return actionDelete;
    }
    LCManager lcManager = new LCManager(this);
    public LCManager lcManager() {
        return lcManager;
    }




    public void printAllTasks() {
        System.out.println(actionCreateGet.getAllTasks());
    }
    public void printTaskWithType(TaskType taskType) {
        System.out.println(actionCreateGet.getTaskByType(taskType));
    }
    public void transformator(String uuid, TaskType newTaskType, String epicUuid) {
        HashMap<String, Task> tasks = actionCreateGet.getAllTasks();
        Task task = null;
        TaskType taskType = tasks.get(uuid).getTaskType();
        Task oldTask = actionCreateGet.getTaskByUuid(uuid);
        if (newTaskType.equals(TaskType.Task)) {
            task = new Task(oldTask.getName(), oldTask.getDescription(), TaskType.Task);
            tasks.put(uuid, task);
            task.setUuid(uuid);
        } else if (newTaskType.equals(TaskType.SubTask)) {
            task = new SubTask(oldTask.getName(), oldTask.getDescription(), TaskType.SubTask);
            task.setUuid(uuid);
            ((SubTask)task).setEpicUuidUuid(epicUuid);
            tasks.put(uuid, task);
            lcManager.epicLC(epicUuid);
        } else if (newTaskType.equals(TaskType.Epic)) {
            task = new Epic(oldTask.getName(), oldTask.getDescription(), TaskType.Epic);
            task.setUuid(uuid);
            task.setTaskStatus(TaskStatus.NEW);
            tasks.put(task.getUuid(), task);
            lcManager.epicLC(uuid);


        } else {
            System.out.println("Такой тип задачи не поддерживается");
            return;
        }
    }
    public void updateTaskParameters(String name, String description, String uuid, String NewEpicUuid) {
        HashMap<String, Task> tasks = actionCreateGet.getAllTasks();
        TaskType initialTaskType = tasks.get(uuid).getTaskType();

        if (!name.isEmpty()) {
            tasks.get(uuid).setName(name);
        }
        if (!description.isEmpty()) {
            tasks.get(uuid).setDescription(description);
        }
        if (!NewEpicUuid.isEmpty() && ((actionCreateGet.getTaskByUuid(uuid).getTaskType().equals(TaskType.SubTask)) || initialTaskType.equals(TaskType.SubTask))) {
            SubTask task = (SubTask) tasks.get(uuid);
            task.setEpicUuidUuid(NewEpicUuid);
            tasks.put(task.getUuid(), task);
            lcManager.epicLC(NewEpicUuid);
        } else {
            System.out.println("Тип задачи не SubTask или new parent id is empty");
        }


    }

}
