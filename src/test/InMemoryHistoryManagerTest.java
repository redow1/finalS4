package test;

import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = taskManager.getHistoryManager();

    @Test
    void getHistory() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task);
        final String taskUuid = taskManager.createTask(task);
        Task task2 = new Task("поспать2", "часов 9", TaskType.Task);
        final String taskUuid2 = taskManager.createTask(task2);
        Task task3 = new Task("поспать3", "часов 10", TaskType.Task);
        final String taskUuid3 = taskManager.createTask(task3);
        Task task4 = new Task("поспать4", "часов 11", TaskType.Task);
        final String taskUuid4 = taskManager.createTask(task4);
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить", TaskType.SubTask, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid2);
        taskManager.getTask(taskUuid3);
        taskManager.getTask(taskUuid3);
        taskManager.getTask(taskUuid2);
        taskManager.getTask(taskUuid4);
        taskManager.getEpic(epicUuid);
        taskManager.getEpic(epicUuid);
        taskManager.getEpic(epicUuid);
        taskManager.getSubTask(subTask1Uuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.deleteEpic(epicUuid);
        final List<Task> checkList2 = historyManager.getHistory();
        assertEquals(4, checkList2.size(), "Неверное количество задач в истории");
    }

}