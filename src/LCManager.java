import entity.*;

import java.util.ArrayList;
import java.util.HashMap;

public class LCManager {

    final TaskManager taskManager;

    public LCManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public TaskStatus checkTasksStatus(String uuid) {
        return taskManager.getTaskByUuid(uuid).getTaskStatus();
    }

    public TaskStatus changeTaskStatus(String uuid, TaskStatus newTaskStatus) {
        AbstractTask task = taskManager.getTaskByUuid(uuid);
        if (!newTaskStatus.equals(checkTasksStatus(uuid))) {
            task.setTaskStatus(newTaskStatus);
        } else {
            return checkTasksStatus(uuid);
        }
        if (task.getTaskType().equals(TaskType.SubTask)) {
            SubTask task1 = (SubTask) task;
            epicLC(task1.getParrentUuid());
        }
        epicLC(uuid);
        return checkTasksStatus(uuid);
    }

    public boolean validateTypeIsEpic(String uuid) {
        AbstractTask task = taskManager.getTaskByUuid(uuid);
        if (!task.getTaskType().equals(TaskType.Epic)) {
            return false;
        } else {
            return true;
        }
    }

    public TaskStatus epicLC(String uuid) {
        if (!validateTypeIsEpic(uuid)) {
            return checkTasksStatus(uuid);

        } else {
            Epic task = (Epic) taskManager.getTaskByUuid(uuid);
            boolean statusUnderEpic = false;
            int counterForCompletedSubTasks = 0;
            if (task != null && task.getSubTasks().isEmpty()) {
                changeTaskStatus(uuid, TaskStatus.NEW);
                return checkTasksStatus(uuid);
            } else if (task != null) {
                for (SubTask subTask : task.getSubTasks()) {
                    if (subTask.getTaskStatus().equals(TaskStatus.DONE)) {
                        statusUnderEpic = true;
                        counterForCompletedSubTasks = counterForCompletedSubTasks + 1;
                    }
                }
                counterForCompletedSubTasks = 0;
                if (statusUnderEpic) {
                    changeTaskStatus(uuid, TaskStatus.DONE);
                    return checkTasksStatus(uuid);
                } else {
                    changeTaskStatus(uuid, TaskStatus.IN_PROGRESS);
                    System.out.println("Под эпиком " + task.getName() + " " + counterForCompletedSubTasks + " завершенные задачи " + (task.getSubTasks().size() - counterForCompletedSubTasks) + " незавершенных задач");
                    return checkTasksStatus(uuid);
                }

            }
            return checkTasksStatus(uuid);
        }


    }
}
