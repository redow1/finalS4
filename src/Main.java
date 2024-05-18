import entity.SubTask;
import entity.TaskStatus;
import entity.TaskType;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        LCManager lcManager = new LCManager();
        //TC
        String uuidtestEpic1 = taskManager.createTask("сходить в магазин1","сходить в магазин завтра", TaskType.Epic, null);
        String uuidtestEpic2 = taskManager.createTask("сходить в магазин","сходить в магазин сегодня", TaskType.Epic, null);
        String uuidtestSubTask2 = taskManager.createTask("сходить в магазин","купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask3 = taskManager.createTask("сходить в магазин","купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask1 = taskManager.createTask("сходить в магазин","купить поесть", TaskType.SubTask, uuidtestEpic2);
        String uuidtestTask1 =taskManager.createTask("поспать","часов 8", TaskType.Task, null);
        taskManager.createTask("поспать","часов 8", TaskType.Task, null);
        taskManager.printAllTasks();

        // TC for transformation
        System.out.println("");
        System.out.println(taskManager.getTaskByUuid(uuidtestTask1));
        System.out.println("");
        // Task to subtask
        // taskManager.transformator(uuidtestTask1, TaskType.SubTask, uuidtestEpic1);
        // Task to epic
        taskManager.transformator(uuidtestTask1, TaskType.Epic, uuidtestEpic1);
        // Epic to task
        // taskManager.transformator(uuidtestEpic2, TaskType.Task, uuidtestEpic1);
        // Epic to subtask
        // taskManager.transformator(uuidtestEpic2, TaskType.SubTask, uuidtestEpic1);
        // SubTask to Epic
        // taskManager.transformator(uuidtestSubTask1, TaskType.Epic, uuidtestEpic1);
        // SubTask to Task
        // taskManager.transformator(uuidtestSubTask1, TaskType.Task, uuidtestEpic1);
        System.out.println("");
        System.out.println(taskManager.getTaskByUuid(uuidtestTask1));
        System.out.println("");

        // taskManager.deleteAllTasks();
        // taskManager.deleteTask(uuidtestTrans1);

        // TC for change status

        lcManager.changeTaskStatus(uuidtestTask1, TaskStatus.IN_PROGRESS);
        System.out.println("");
        lcManager.changeTaskStatus(uuidtestSubTask1, TaskStatus.IN_PROGRESS);
        System.out.println("статус эпика 2(ER: In_progress)");
        lcManager.checkTasksStatus(uuidtestEpic2);
        System.out.println("");
        lcManager.changeTaskStatus(uuidtestSubTask2, TaskStatus.DONE);
        lcManager.changeTaskStatus(uuidtestSubTask3, TaskStatus.DONE);
        System.out.println("статус эпика 1(ER: Done)");
        lcManager.checkTasksStatus(uuidtestEpic1);
        System.out.println("");
        System.out.println("статус эпика 1(ER: New)");
        lcManager.checkTasksStatus(uuidtestTask1);

    }
}
