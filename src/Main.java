import managers.Managers;
import managers.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        //Test data
        /*
        System.out.println("TD creation:");
        String uuidtestEpic1 = taskManager.createEpic(new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic));
        String uuidtestEpic2 = InMemoryTaskManager.createEpic(new Epic("сходить в магазин", "сходить в магазин сегодня", TaskType.Epic));
        String uuidtestSubTask2 = InMemoryTaskManager.createSubTask(new SubTask("сходить в магазин1", "купить попить", TaskType.SubTask, uuidtestEpic1));
        String uuidtestSubTask3 = InMemoryTaskManager.createSubTask(new SubTask("сходить в магазин2", "купить попить", TaskType.SubTask, uuidtestEpic1));
        String uuidtestSubTask1 = InMemoryTaskManager.createSubTask(new SubTask("сходить в магазин3", "купить поесть", TaskType.SubTask, uuidtestEpic2));
        String uuidtestTask1 = InMemoryTaskManager.createTask(new Task("поспать1", "часов 8", TaskType.Task));
        String uuidtestTask2 = InMemoryTaskManager.createTask(new Task("поспать2", "часов 8", TaskType.Task));
        */

        // TC for get
        /*
        System.out.println("");
        System.out.println("0");
        System.out.println(InMemoryTaskManager.getTasks());
        System.out.println(InMemoryTaskManager.getSubTasks());
        System.out.println(InMemoryTaskManager.getEpics());

        System.out.println("");
        System.out.println("1");
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println("");
        System.out.println("GetHistory1");
        System.out.println("");
        System.out.println(InMemoryTaskManager.getHistory());
        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getHistory());
        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println("");
        System.out.println("GetHistory2");
        System.out.println("");
        System.out.println(InMemoryTaskManager.getHistory());

         */

        /*
        // TC for delete
        System.out.println("2");
        System.out.println("");

//        InMemoryTaskManager.deleteTask(uuidtestTask2);
//        InMemoryTaskManager.deleteSubTask(uuidtestSubTask3);
//        InMemoryTaskManager.deleteEpic(uuidtestEpic2);

//        InMemoryTaskManager.deleteTasks();
//        InMemoryTaskManager.deleteSubTasks();
//        InMemoryTaskManager.deleteEpics();


        // TC for change status
        System.out.println("4");
        System.out.println("");
        InMemoryTaskManager.updateTaskStatus(uuidtestTask1, TaskStatus.IN_PROGRESS);
        System.out.println(InMemoryTaskManager.checkTaskStatus(uuidtestTask1));
        System.out.println("статус таски 1(ER: In_progress)");
        System.out.println("");

        InMemoryTaskManager.updateSubTaskStatus(uuidtestSubTask1, TaskStatus.IN_PROGRESS);
        System.out.println(InMemoryTaskManager.checkSubTaskStatus(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic2));
        System.out.println("статус эпика 2(ER: In_progress)");
        System.out.println("");

        InMemoryTaskManager.updateSubTaskStatus(uuidtestSubTask2, TaskStatus.DONE);
        InMemoryTaskManager.updateSubTaskStatus(uuidtestSubTask3, TaskStatus.DONE);
        System.out.println("uuidtestSubTask2");
        System.out.println(InMemoryTaskManager.checkSubTaskStatus(uuidtestSubTask2));;
        System.out.println("uuidtestSubTask3");
        System.out.println(InMemoryTaskManager.checkSubTaskStatus(uuidtestSubTask3));
        System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic1));
        System.out.println("статус эпика 1(ER: Done)");
        System.out.println("");


        // TC for update
        System.out.println("before update");
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));

        System.out.println("after update");
        //Task
        InMemoryTaskManager.updateTaskParameters(new Task("погулять", "полчасика", uuidtestTask1));
        System.out.println(InMemoryTaskManager.getTask(uuidtestTask1));
        //Subtask
        InMemoryTaskManager.updateSubTaskParameters(new SubTask("заказать проудктов", "к родителям", uuidtestSubTask1, uuidtestEpic1));
        System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic1));
        System.out.println("статус эпика 1(ER: In Progress)");
        System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic2));
        System.out.println("статус эпика 2(ER: New)");

        System.out.println(InMemoryTaskManager.getSubTask(uuidtestSubTask1));
        //Epic
        InMemoryTaskManager.updateEpicParameters(new Epic("сходить на работу", null, uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));
        //negative
        InMemoryTaskManager.updateEpicParameters(new Epic(null, null, uuidtestEpic1));
        System.out.println(InMemoryTaskManager.getEpic(uuidtestEpic1));


        //InMemoryTaskManager.deleteSubTasks();
        //System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic2));
        //System.out.println(InMemoryTaskManager.checkEpicStatus(uuidtestEpic1));

        InMemoryTaskManager.deleteEpic(uuidtestEpic1);
        System.out.println(InMemoryTaskManager.getSubTasks());

         */



    }


}
