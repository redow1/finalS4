package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void createTask() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task, Duration.ofDays(1), LocalDateTime.now(), null);
        final String uuid = taskManager.createTask(task);
        final Task savedTask = taskManager.getTask(uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofDays(1), LocalDateTime.now(), null);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, Duration.ofDays(3), LocalDateTime.now(), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final Task savedTask = taskManager.getSubTask(subTask1Uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofDays(1), LocalDateTime.now(), null);
        final String uuid = taskManager.createEpic(epic);
        final Task savedTask = taskManager.getEpic(uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getTaskByPrio() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task, Duration.ofMinutes(1), LocalDateTime.now().plusHours(3), null);
        final String uuid = taskManager.createTask(task);
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(3), null);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить", TaskType.SubTask, Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(15), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        Epic epic2 = new Epic("сходить в магазин3", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(20), null);
        final String uuid2 = taskManager.createEpic(epic2);
        List<Task> prioritizedTasksEtalon = new ArrayList<>();
        prioritizedTasksEtalon.add(epic2);
        prioritizedTasksEtalon.add(subTask);
        prioritizedTasksEtalon.add(task);
        taskManager.getPrioritizedTasks();
        assertEquals(taskManager.getPrioritizedTasks(),prioritizedTasksEtalon);
    }
}