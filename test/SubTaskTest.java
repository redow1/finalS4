package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskType;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void subTaskEpicIDCannotBeUpdatedToSubTaskID() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String epicUuid = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, epicUuid);
        final String subTask1Uuid = taskManager.createSubTask(subTask);
        taskManager.updateSubTaskParameters(new SubTask("заказать проудктов", "к родителям", subTask1Uuid, subTask1Uuid));
        assertEquals(subTask.getEpicUuidUuid(), epicUuid, "Эпикайди был обновлен");
    }

}