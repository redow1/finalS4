package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void createTask() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task);
        final String uuid = taskManager.createTask(task);
        final Task savedTask = taskManager.getTask(uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final Task savedTask = taskManager.getSubTask(subTask1Uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String uuid = taskManager.createEpic(epic);
        final Task savedTask = taskManager.getEpic(uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");
    }


}