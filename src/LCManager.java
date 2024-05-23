import entity.*;

import java.util.HashMap;

public class LCManager {

    final TaskManager taskManager;

    public LCManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public TaskStatus checkTasksStatus(String uuid) {
        return taskManager.actionCreateGet().getTaskByUuid(uuid).getTaskStatus();
    }

    public void changeTaskStatus(String uuid, TaskStatus newTaskStatus) {
        Task task = taskManager.actionCreateGet().getTaskByUuid(uuid);
        if (!newTaskStatus.equals(checkTasksStatus(uuid))) {
            task.setTaskStatus(newTaskStatus);
            if (task.getTaskType().equals(TaskType.SubTask)) {
                SubTask task1 = (SubTask) taskManager.actionCreateGet().getTaskByUuid(uuid);
                epicLC(task1.getEpicUuidUuid());
            }
        }
    }

    public boolean validateTypeIsEpic(String uuid) {
        Task task = taskManager.actionCreateGet().getTaskByUuid(uuid);
        return task.getTaskType().equals(TaskType.Epic);
    }

    public TaskStatus epicLC(String uuid) {
        if (!validateTypeIsEpic(uuid)) {
            return checkTasksStatus(uuid);

        } else {
            Epic task = (Epic) taskManager.actionCreateGet().getTaskByUuid(uuid);
            boolean statusUnderEpic = false;
            boolean previousStatusUnderEpic = true;

            int counterForCompletedSubTasks = 0;
            int counterForLinkedSubTasks = 0;
            if (task != null) {
                HashMap<String, SubTask> subtasks = taskManager.actionCreateGet().getTaskByType(TaskType.SubTask);
                for (String string : subtasks.keySet()) {
                    if (uuid.equals(subtasks.get(string).getEpicUuidUuid())) {
                        counterForLinkedSubTasks = counterForLinkedSubTasks + 1;
                        if (subtasks.get(string).getTaskStatus().equals(TaskStatus.DONE)) {
                            statusUnderEpic = previousStatusUnderEpic;
                            counterForCompletedSubTasks = counterForCompletedSubTasks + 1;

                        } else {
                            statusUnderEpic = false;
                            previousStatusUnderEpic = false;

                        }
                    }

                }
                if (counterForLinkedSubTasks == 0) {
                    changeTaskStatus(uuid, TaskStatus.NEW);
                    return checkTasksStatus(uuid);
                }
                if (statusUnderEpic) {
                    changeTaskStatus(uuid, TaskStatus.DONE);
                    return checkTasksStatus(uuid);
                } else {
                    changeTaskStatus(uuid, TaskStatus.IN_PROGRESS);
                    System.out.println("Под эпиком " + task.getName() + " " + counterForCompletedSubTasks + " завершенные задачи " + (counterForLinkedSubTasks - counterForCompletedSubTasks) + " незавершенных задач");
                    return checkTasksStatus(uuid);
                }


            }
            return checkTasksStatus(uuid);
        }


    }
}
