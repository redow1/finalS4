package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void createNewTask() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task, Duration.ofDays(1), LocalDateTime.now(), null);
        final String uuid = taskManager.createTask(task);
        final Task savedTask = taskManager.getTask(uuid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

}