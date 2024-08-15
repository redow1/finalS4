package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void allNewStratus() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().plusDays(4), null);
        final String epicUuid = taskManager.createEpic(epic);
        TaskStatus epicStatusBefore = TaskStatus.IN_PROGRESS;
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить2", TaskType.SubTask, Duration.ofHours(1), LocalDateTime.now(), null, epicUuid);
        SubTask subTask1 = new SubTask("сходить в магазин3", "купить попить3", TaskType.SubTask, Duration.ofMinutes(3), LocalDateTime.now().plusHours(3), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final String subTask2Uuid = taskManager.createSubTask(subTask1);
        TaskStatus epicStatusAfter = epic.getTaskStatus();
        assertEquals(epicStatusBefore, epicStatusAfter, "Эпик статус != In Progress");
    }

    @Test
    void allDoneStratus() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().plusDays(4), null);
        final String epicUuid = taskManager.createEpic(epic);
        TaskStatus epicStatusBefore = TaskStatus.DONE;
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить2", TaskType.SubTask, Duration.ofHours(1), LocalDateTime.now(), null, epicUuid);
        SubTask subTask1 = new SubTask("сходить в магазин3", "купить попить3", TaskType.SubTask, Duration.ofMinutes(3), LocalDateTime.now().plusHours(3), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final String subTask2Uuid = taskManager.createSubTask(subTask1);
        taskManager.updateSubTaskStatus(subTask1Uuid, TaskStatus.DONE);
        taskManager.updateSubTaskStatus(subTask2Uuid, TaskStatus.DONE);
        TaskStatus epicStatusAfter = epic.getTaskStatus();
        assertEquals(epicStatusBefore, epicStatusAfter, "Эпик статус != Done");
    }

    @Test
    void newDoneStratus() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().plusDays(4), null);
        final String epicUuid = taskManager.createEpic(epic);
        TaskStatus epicStatusBefore = TaskStatus.IN_PROGRESS;
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить2", TaskType.SubTask, Duration.ofHours(1), LocalDateTime.now(), null, epicUuid);
        SubTask subTask1 = new SubTask("сходить в магазин3", "купить попить3", TaskType.SubTask, Duration.ofMinutes(3), LocalDateTime.now().plusHours(3), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final String subTask2Uuid = taskManager.createSubTask(subTask1);
        taskManager.updateSubTaskStatus(subTask2Uuid, TaskStatus.DONE);
        TaskStatus epicStatusAfter = epic.getTaskStatus();
        assertEquals(epicStatusBefore, epicStatusAfter, "Эпик статус != In Progress");
    }

    @Test
    void allInProgressStratus() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().plusDays(4), null);
        final String epicUuid = taskManager.createEpic(epic);
        TaskStatus epicStatusBefore = TaskStatus.IN_PROGRESS;
        SubTask subTask = new SubTask("сходить в магазин2", "купить попить2", TaskType.SubTask, Duration.ofHours(1), LocalDateTime.now(), null, epicUuid);
        SubTask subTask1 = new SubTask("сходить в магазин3", "купить попить3", TaskType.SubTask, Duration.ofMinutes(3), LocalDateTime.now().plusHours(3), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        final String subTask2Uuid = taskManager.createSubTask(subTask1);
        taskManager.updateSubTaskStatus(subTask1Uuid, TaskStatus.IN_PROGRESS);
        taskManager.updateSubTaskStatus(subTask2Uuid, TaskStatus.IN_PROGRESS);
        TaskStatus epicStatusAfter = epic.getTaskStatus();
        assertEquals(epicStatusBefore, epicStatusAfter, "Эпик статус != In Progress");
    }

}