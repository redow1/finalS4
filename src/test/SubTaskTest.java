package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void subTaskEpicIDCannotBeUpdatedToSubTaskID() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofDays(1), LocalDateTime.now(), null);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, Duration.ofDays(3), LocalDateTime.now(), null, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        taskManager.updateSubTaskParameters(subTask1Uuid,"заказать проудктов", "к родителям",null,null,null,subTask1Uuid);
        assertEquals(subTask.getEpicUuid(), epicUuid, "Эпикайди был обновлен");
    }

}