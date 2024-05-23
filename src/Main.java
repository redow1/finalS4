import entity.TaskStatus;
import entity.TaskType;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        //TC
        System.out.println("TD creation:");
        String uuidtestEpic1 = taskManager.actionCreateGet().createEpic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        String uuidtestEpic2 = taskManager.actionCreateGet().createEpic("сходить в магазин", "сходить в магазин сегодня", TaskType.Epic);
        String uuidtestSubTask2 = taskManager.actionCreateGet().createSubTask("сходить в магазин1", "купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask3 = taskManager.actionCreateGet().createSubTask("сходить в магазин2", "купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask1 = taskManager.actionCreateGet().createSubTask("сходить в магазин3", "купить поесть", TaskType.SubTask, uuidtestEpic2);
        String uuidtestTask1 = taskManager.actionCreateGet().createTask("поспать1", "часов 8", TaskType.Task);
        String uuidtestTask2 = taskManager.actionCreateGet().createTask("поспать2", "часов 8", TaskType.Task);
        taskManager.printAllTasks();

        // TC for transformation
        System.out.println("");
        System.out.println("1");
        System.out.println(taskManager.actionCreateGet().getTaskByUuid(uuidtestEpic1));
        System.out.println("");

        System.out.println("2");
        taskManager.lcManager.changeTaskStatus(uuidtestTask1, TaskStatus.IN_PROGRESS);
        System.out.println("before transformation");
        System.out.println(taskManager.actionCreateGet().getTaskByUuid(uuidtestSubTask1));
        // Task to subtask
        // taskManager.transformator(uuidtestTask1, TaskType.SubTask, uuidtestEpic1);
        // Task to epic
        taskManager.transformator(uuidtestTask1, TaskType.Epic, null);
        // Epic to task
        // taskManager.transformator(uuidtestEpic2, TaskType.Task, null);
        // Epic to subtask
        // taskManager.transformator(uuidtestEpic2, TaskType.SubTask, uuidtestEpic1);
        // SubTask to Epic
        // taskManager.transformator(uuidtestSubTask1, TaskType.Epic, null);
        // SubTask to Task
        // taskManager.transformator(uuidtestSubTask1, TaskType.Task, uuidtestEpic1);

        System.out.println("after transformation");
        System.out.println(taskManager.actionCreateGet().getTaskByUuid(uuidtestSubTask1));

        System.out.println("3");
        System.out.println("");

        //taskManager.actionDelete.deleteTask(uuidtestSubTask3);
        //taskManager.actionDelete.deleteAllTasks();


        // TC for change status
        System.out.println("4");

        System.out.println("");
        taskManager.lcManager().changeTaskStatus(uuidtestSubTask1, TaskStatus.IN_PROGRESS);
        System.out.println(taskManager.lcManager().checkTasksStatus(uuidtestSubTask1));
        System.out.println("статус эпика 2(ER: In_progress)");
        System.out.println("");

        taskManager.lcManager().changeTaskStatus(uuidtestSubTask2, TaskStatus.DONE);
        taskManager.lcManager().changeTaskStatus(uuidtestSubTask3, TaskStatus.DONE);
        System.out.println("uuidtestSubTask2");
        System.out.println(taskManager.lcManager().checkTasksStatus(uuidtestSubTask2));
        System.out.println("uuidtestSubTask3");
        System.out.println(taskManager.lcManager().checkTasksStatus(uuidtestSubTask3));
        System.out.println(taskManager.lcManager().checkTasksStatus(uuidtestEpic1));
        System.out.println("статус эпика 1(ER: Done)");
        System.out.println("");
        System.out.println(taskManager.lcManager().checkTasksStatus(uuidtestTask1));
        System.out.println("статус эпика 3(ER: New)");

    }
}
