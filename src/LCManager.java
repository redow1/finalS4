import entity.*;

public class LCManager {

    final TaskManager taskManager = new TaskManager();

    public TaskStatus checkTasksStatus(String uuid) {
        return taskManager.getTaskByUuid(uuid).getTaskStatus();
    }

    public TaskStatus changeTaskStatus(String uuid, TaskStatus newTaskStatus) {

        if (!newTaskStatus.equals(checkTasksStatus(uuid))) {
            taskManager.getTaskByUuid(uuid).setTaskStatus(newTaskStatus);
        } else {
            System.out.println("Статус задачи нельзя обновить на такой же");
            return checkTasksStatus(uuid);
        }
        epicLC(uuid);
        return checkTasksStatus(uuid);
    }

    public boolean validateTypeIsEpic(String uuid) {
        AbstractTask task = taskManager.getTaskByUuid(uuid);
        if (!task.getTaskType().equals(TaskType.Epic)) {
            System.out.println("Тип задачи не Epic");
            return false;
        } else {
            return true;
        }
    }

    public TaskStatus epicLC(String uuid) {
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
                System.out.println("Под эпиком " + counterForCompletedSubTasks + " завершенные задачи " + (task.getSubTasks().size() - counterForCompletedSubTasks) + " незавершенных задач");
                return checkTasksStatus(uuid);
            }

        }
        return checkTasksStatus(uuid);
    }
}
