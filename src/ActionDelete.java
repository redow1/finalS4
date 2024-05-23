import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskType;

import java.util.HashMap;

public class ActionDelete {
    final TaskManager taskManager;
    public ActionDelete(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void deleteAllTasks() {
        HashMap tasks = taskManager.actionCreateGet.getAllTasks();
        tasks.clear();
        System.out.println("все задачи удалены");
    }
    public void deleteTask(String uuid) {
        HashMap<String, Task> tasks = taskManager.actionCreateGet.getAllTasks();
        System.out.println("задача " + uuid + " " + tasks.get(uuid).getName() + "  удалена");
        tasks.remove(uuid);

    }




}
