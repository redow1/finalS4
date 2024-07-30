package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface TaskManager {
    public HistoryManager getHistoryManager();
    default String UUIDGen() {
        return UUID.randomUUID().toString();
    }

    String createTask(Task task);

    String createSubTask(SubTask subTask);

    String createEpic(Epic epic);

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    ArrayList<SubTask> getEpicSubtasks(String EpicUuid);

    Task getTask(String uuid);

    SubTask getSubTask(String uuid);

    Epic getEpic(String uuid);

    void updateTaskParameters(Task task);

    void updateSubTaskParameters(SubTask subTask);

    void updateEpicParameters(Epic epic);

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
