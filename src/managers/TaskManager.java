package managers;

import tasks.*;

import java.util.HashMap;
import java.util.UUID;

public class TaskManager {

    //Storages
    HashMap<String, Task> taskMap = new HashMap<>();
    HashMap<String, SubTask> subTaskMap = new HashMap<>();
    HashMap<String, Epic> epicMap = new HashMap<>();

    //uuid generation

    private String UUIDGen() {
        return UUID.randomUUID().toString();
    }

    // Create (для теста сделал ретерн uuid)

    public String createTask(String name, String description, TaskType taskType) {
        String uuid;
        Task task;
        task = new Task(name, description, taskType);
        task.setUuid(UUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        taskMap.put(task.getUuid(), task);
        return (uuid);
    }

    public String createSubTask(String name, String description, TaskType taskType, String epicUuid) {
        String uuid;
        SubTask task;
        task = new SubTask(name, description, taskType);
        task.setUuid(UUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        task.setEpicUuidUuid(epicUuid);
        subTaskMap.put(task.getUuid(), task);
        updateEpicStatus(epicUuid);
        return (uuid);
    }

    public String createEpic(String name, String description, TaskType taskType) {
        String uuid;
        Epic task;
        task = new Epic(name, description, taskType);
        task.setUuid(UUIDGen());
        uuid = task.getUuid();
        task.setTaskStatus(TaskStatus.NEW);
        epicMap.put(task.getUuid(), task);
        updateEpicStatus(uuid);
        return (uuid);
    }

    // Get
    public HashMap<String, Task> getTasks() {
        return taskMap;
    }

    public HashMap<String, SubTask> getSubTasks() {
        return subTaskMap;
    }

    public HashMap<String, Epic> getEpics() {
        return epicMap;
    }

    public HashMap<String, SubTask> getEpicSubtasks(String EpicUuid) {
        HashMap<String, SubTask> forPrint = new HashMap<>();
        for (String string : subTaskMap.keySet()) {
            if (EpicUuid.equals(subTaskMap.get(string).getEpicUuidUuid())) {
                forPrint.put(string, subTaskMap.get(string));
            }
        }
        return forPrint;
    }

    public Task getTask(String uuid) {
        return taskMap.get(uuid);
    }

    public SubTask getSubTask(String uuid) {
        return subTaskMap.get(uuid);
    }

    public Epic getEpic(String uuid) {
        return epicMap.get(uuid);
    }

    // Update

    public void updateTaskParameters(String name, String description, String uuid) {
        HashMap<String, Task> tasks = getTasks();
        if (name != null) {
            tasks.get(uuid).setName(name);
        }
        if (!description.isEmpty()) {
            tasks.get(uuid).setDescription(description);
        }
    }

    public void updateSubTaskParameters(String name, String description, String uuid, String NewEpicUuid) {
        HashMap<String, SubTask> tasks = getSubTasks();
        if (name != null) {
            tasks.get(uuid).setName(name);
        }
        if (NewEpicUuid != null) {
            tasks.get(uuid).setDescription(description);
        }
        if (NewEpicUuid != null) {
            SubTask task = tasks.get(uuid);
            String oldEpicUuid = task.getEpicUuidUuid();
            task.setEpicUuidUuid(NewEpicUuid);
            tasks.put(task.getUuid(), task);
            updateEpicStatus(oldEpicUuid);
            updateEpicStatus(NewEpicUuid);
        }
    }

    public void updateEpicParameters(String name, String description, String uuid) {
        HashMap<String, Epic> tasks = getEpics();
        if (name != null) {
            tasks.get(uuid).setName(name);
        }
        if (description != null) {
            tasks.get(uuid).setDescription(description);
        }
    }

    // Delete
    public void deleteTasks() {
        taskMap.clear();
        System.out.println("все Tasks удалены");
    }

    public void deleteSubTasks() {
        subTaskMap.clear();
        System.out.println("все SubTasks удалены");
    }

    public void deleteEpics() {
        epicMap.clear();
        System.out.println("все Epics удалены");
    }

    public void deleteTask(String uuid) {
        System.out.println("задача " + uuid + " " + taskMap.get(uuid).getName() + "  удалена");
        taskMap.remove(uuid);
    }

    public void deleteSubTask(String uuid) {
        System.out.println("задача " + uuid + " " + subTaskMap.get(uuid).getName() + "  удалена");
        subTaskMap.remove(uuid);
    }

    public void deleteEpic(String uuid) {
        System.out.println("задача " + uuid + " " + epicMap.get(uuid).getName() + "  удалена");
        epicMap.remove(uuid);
    }

    // Check Status
    public TaskStatus checkTaskStatus(String uuid) {
        return getTask(uuid).getTaskStatus();
    }

    public TaskStatus checkSubTaskStatus(String uuid) {
        return getSubTask(uuid).getTaskStatus();
    }

    public TaskStatus checkEpicStatus(String uuid) {
        return getEpic(uuid).getTaskStatus();
    }

    // Update Status
    public void updateTaskStatus(String uuid, TaskStatus newStatus) {
        taskMap.get(uuid).setTaskStatus(newStatus);
    }

    public void updateSubTaskStatus(String uuid, TaskStatus newStatus) {
        subTaskMap.get(uuid).setTaskStatus(newStatus);
        updateEpicStatus(subTaskMap.get(uuid).getEpicUuidUuid());
    }

    public void updateEpicStatus(String uuid) {
        Epic epic = getEpic(uuid);
        int counterForCompletedSubTasks = 0;
        int counterForLinkedSubTasks = 0;
        boolean statusUnderEpic = false;
        boolean previousStatusUnderEpic = true;
        if (epic != null) {
            HashMap<String, SubTask> subtasks = getEpicSubtasks(uuid);
            for (String string : subtasks.keySet()) {
                counterForLinkedSubTasks = counterForLinkedSubTasks + 1;
                if (subtasks.get(string).getTaskStatus().equals(TaskStatus.DONE)) {
                    statusUnderEpic = previousStatusUnderEpic;
                    counterForCompletedSubTasks = counterForCompletedSubTasks + 1;
                } else {
                    statusUnderEpic = false;
                    previousStatusUnderEpic = false;
                }
            }
            if (statusUnderEpic) {
                epicMap.get(uuid).setTaskStatus(TaskStatus.DONE);
                System.out.println("Под эпиком " + epic.getName() + " все задачи завершены");
            } else {
                epicMap.get(uuid).setTaskStatus(TaskStatus.IN_PROGRESS);


            }
            if (counterForLinkedSubTasks == 0) {
                epicMap.get(uuid).setTaskStatus(TaskStatus.NEW);
                System.out.println("Под эпиком " + epic.getName() + " " + counterForCompletedSubTasks + " завершенные задачи " + (counterForLinkedSubTasks - counterForCompletedSubTasks) + " незавершенных задач");
            }
        }
    }
}
