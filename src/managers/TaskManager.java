package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface TaskManager {

    public HistoryManager getHistoryManager();

    default String uuidgen() {
        return UUID.randomUUID().toString();
    }

    Map groupMapsForSave(Map taskMap, Map subTaskMap, Map epicMap);

    String createTask(Task task);

    String createSubTask(SubTask subTask);

    String createEpic(Epic epic);

    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getEpicSubtasks(String epicUuid);

    Task getTask(String uuid);

    SubTask getSubTask(String uuid);

    Epic getEpic(String uuid);

    void updateTaskParameters(String uuid, String newName, String newDescription);

    void updateSubTaskParameters(String uuid, String newName, String newDescription, String newEpicUuid);

    void updateEpicParameters(String uuid, String newName, String newDescription);

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpics();

    void deleteTask(String uuid);

    void deleteSubTask(String uuid);

    void deleteEpic(String uuid);

    TaskStatus checkTaskStatus(String uuid);

    TaskStatus checkSubTaskStatus(String uuid);

    TaskStatus checkEpicStatus(String uuid);

    void updateTaskStatus(String uuid, TaskStatus newStatus);

    void updateSubTaskStatus(String uuid, TaskStatus newStatus);

    void updateEpicStatus(String uuid);
}