package test;

import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();
    HistoryManager historyManager = taskManager.getHistoryManager();
    @Test
    void getHistory() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task);
        final String taskUuid = taskManager.createTask(task);
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        taskManager.getTask(taskUuid);
        taskManager.getEpic(epicUuid);
        taskManager.getSubTask(subTask1Uuid);
        final LinkedList<Task> checkList1 = historyManager.getHistory();
        assertEquals(5, checkList1.size(), "Неверное количество задач в истории");
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);
        taskManager.getTask(taskUuid);

        final LinkedList<Task> checkList2 = historyManager.getHistory();
        assertEquals(10, checkList2.size(), "Неверное количество задач в истории");
    }
}