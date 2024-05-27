import managers.TaskManager;
import tasks.TaskStatus;
import tasks.TaskType;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        //Test data
        System.out.println("TD creation:");
        String uuidtestEpic1 = taskManager.createEpic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        String uuidtestEpic2 = taskManager.createEpic("сходить в магазин", "сходить в магазин сегодня", TaskType.Epic);
        String uuidtestSubTask2 = taskManager.createSubTask("сходить в магазин1", "купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask3 = taskManager.createSubTask("сходить в магазин2", "купить попить", TaskType.SubTask, uuidtestEpic1);
        String uuidtestSubTask1 = taskManager.createSubTask("сходить в магазин3", "купить поесть", TaskType.SubTask, uuidtestEpic2);
        String uuidtestTask1 = taskManager.createTask("поспать1", "часов 8", TaskType.Task);
        String uuidtestTask2 = taskManager.createTask("поспать2", "часов 8", TaskType.Task);

        // TC for get
        System.out.println("");
        System.out.println("0");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());

        System.out.println("");
        System.out.println("1");
        System.out.println(taskManager.getTask(uuidtestTask1));
        System.out.println(taskManager.getSubTask(uuidtestSubTask1));
        System.out.println(taskManager.getEpic(uuidtestEpic1));
        System.out.println("");

        // TC for delete
        System.out.println("2");
        System.out.println("");

//        taskManager.deleteTask(uuidtestTask2);
//        taskManager.deleteSubTask(uuidtestSubTask3);
//        taskManager.deleteEpic(uuidtestEpic2);

//        taskManager.deleteTasks();
//        taskManager.deleteSubTasks();
//        taskManager.deleteEpics();


        // TC for change status
        System.out.println("4");
        System.out.println("");
        taskManager.updateTaskStatus(uuidtestTask1, TaskStatus.IN_PROGRESS);
        System.out.println(taskManager.checkTaskStatus(uuidtestTask1));
        System.out.println("статус таски 1(ER: In_progress)");
        System.out.println("");

        taskManager.updateSubTaskStatus(uuidtestSubTask1, TaskStatus.IN_PROGRESS);
        System.out.println(taskManager.checkSubTaskStatus(uuidtestSubTask1));
        System.out.println(taskManager.checkEpicStatus(uuidtestEpic2));
        System.out.println("статус эпика 2(ER: In_progress)");
        System.out.println("");

        taskManager.updateSubTaskStatus(uuidtestSubTask2, TaskStatus.DONE);
        taskManager.updateSubTaskStatus(uuidtestSubTask3, TaskStatus.DONE);
        System.out.println("uuidtestSubTask2");
        System.out.println(taskManager.checkSubTaskStatus(uuidtestSubTask2));;
        System.out.println("uuidtestSubTask3");
        System.out.println(taskManager.checkSubTaskStatus(uuidtestSubTask3));
        System.out.println(taskManager.checkEpicStatus(uuidtestEpic1));
        System.out.println("статус эпика 1(ER: Done)");
        System.out.println("");


        // TC for update
        System.out.println("before update");
        System.out.println(taskManager.getTask(uuidtestTask1));
        System.out.println(taskManager.getSubTask(uuidtestSubTask1));
        System.out.println(taskManager.getEpic(uuidtestEpic1));

        System.out.println("after update");
        //Task
        taskManager.updateTaskParameters("погулять", "полчасика", uuidtestTask1);
        System.out.println(taskManager.getTask(uuidtestTask1));
        //Subtask
        taskManager.updateSubTaskParameters("заказать проудктов", "к родителям", uuidtestSubTask1, uuidtestEpic1);
        System.out.println(taskManager.checkEpicStatus(uuidtestEpic1));
        System.out.println("статус эпика 1(ER: In Progress)");
        System.out.println(taskManager.checkEpicStatus(uuidtestEpic2));
        System.out.println("статус эпика 2(ER: New)");

        System.out.println(taskManager.getSubTask(uuidtestSubTask1));
        //Epic
        taskManager.updateEpicParameters("сходить на работу", null, uuidtestEpic1);
        System.out.println(taskManager.getEpic(uuidtestEpic1));
        //negative
        taskManager.updateEpicParameters(null, null, uuidtestEpic1);
        System.out.println(taskManager.getEpic(uuidtestEpic1));

    }
}
