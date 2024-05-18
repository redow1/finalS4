import entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TaskManager {

    LCManager lcManager = new LCManager();
    HashMap<String, AbstractTask> taskMap = new HashMap<>();

    HashMap<TaskType, ArrayList<AbstractTask>> typesToTasks = new HashMap<>();

    public String createTask(String name, String description, TaskType taskType, String uuid) {
        AbstractTask task;
        ArrayList<AbstractTask> list;
        list = typesToTasks.getOrDefault(taskType, new ArrayList<>());
        if (taskType.equals(TaskType.Task)) {
            task = new Task(name, description, taskType);
        } else if (taskType.equals(TaskType.SubTask)) {
            task = new SubTask(name, description, taskType);
            linkSubTaskAndEpic(uuid, (SubTask) task);
        } else if (taskType.equals(TaskType.Epic)) {
            task = new Epic(name, description, taskType);
        } else {
            System.out.println("Такой тип задачи не поддерживается");
            return ("");
        }
        task.setUuid(uUIDGen());
        list.add(task);
        typesToTasks.put(taskType, list);
        taskMap.put(task.getUuid(), task);

        (taskMap.get(uuid)).setTaskStatus(TaskStatus.NEW);
        if (lcManager.validateTypeIsEpic(uuid)) {
            lcManager.epicLC(uuid);
        }


        return task.getUuid();
    }

    public void printAllTasks() {
        System.out.println(taskMap);
    }

    public void deleteAllTasks() {
        taskMap.clear();
        typesToTasks.clear();
        System.out.println("все задачи удалены");
    }

    public void deleteTask(String uuid) {
        AbstractTask task = getTaskByUuid(uuid);
        typesToTasks.get(typesToTasks.get(task.getTaskType())).remove(uuid);
        taskMap.remove(task);
        if (lcManager.validateTypeIsEpic(uuid)) {
            lcManager.epicLC(uuid);
        }
        System.out.println("все задачи удалены");

    }

    public AbstractTask getTaskByUuid(String uuid) {
        return taskMap.get(uuid);
    }

    public ArrayList getListofTasksbyType(TaskType taskType) {
        return typesToTasks.get(taskType);
    }

    public void updateTaskParameters(String name, String description, String uuid, String newparentUuid) {
        TaskType initialTaskType = taskMap.get(uuid).getTaskType();

        if (!name.isEmpty()) {
            taskMap.get(uuid).setName(name);
        }
        if (!description.isEmpty()) {
            taskMap.get(uuid).setDescription(description);
        }
        if (!newparentUuid.isEmpty() && ((getTaskByUuid(uuid).getTaskType().equals(TaskType.SubTask)) || initialTaskType.equals(TaskType.SubTask))) {
            linkSubTaskAndEpic(newparentUuid, (SubTask) getTaskByUuid(uuid));
        } else {
            System.out.println("Тип задачи не SubTask или new parent id is empty");
        }


    }

    private void linkSubTaskAndEpic(String parrentUuid, SubTask subTask) {
        subTask.setParrentUuid(parrentUuid);
        ((Epic) taskMap.get(parrentUuid)).getSubTasks().add(subTask);
        lcManager.epicLC(parrentUuid);

    }

    public String uUIDGen() {
        return UUID.randomUUID().toString();
    }

    public void transformator(String uuid, TaskType newTaskType, String epicuuid) {
        AbstractTask task = null;
        ArrayList<AbstractTask> list;
        TaskType taskType = taskMap.get(uuid).getTaskType();
        list = typesToTasks.getOrDefault(taskType, new ArrayList<>());
        typesToTasks.get(taskType).remove(uuid);

        AbstractTask oldTask = getTaskByUuid(uuid);
        if (newTaskType.equals(TaskType.Task)) {
            task = new Task(oldTask.getName(), oldTask.getDescription(), TaskType.Task);
        } else if (newTaskType.equals(TaskType.SubTask)) {
            task = new SubTask(oldTask.getName(), oldTask.getDescription(), TaskType.SubTask);

            linkSubTaskAndEpic(epicuuid, (SubTask) task);
            lcManager.epicLC(epicuuid);

        } else if (newTaskType.equals(TaskType.Epic)) {
            task = new Epic(oldTask.getName(), oldTask.getDescription(), TaskType.SubTask);
            list.add(task);
            typesToTasks.put(taskType, list);
            lcManager.epicLC(uuid);
        } else {
            System.out.println("Такой тип задачи не поддерживается");
            return;
        }
        task.setUuid(oldTask.getUuid());
        taskMap.put(task.getUuid(), task);
    }


}
