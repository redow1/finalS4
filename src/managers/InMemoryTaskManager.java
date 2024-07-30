package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    Map<String, Task> taskMap = new HashMap<>();
    Map<String, SubTask> subTaskMap = new HashMap<>();
    Map<String, Epic> epicMap = new HashMap<>();


    @Override
    public String createTask(Task task) {
        task.setUuid(UUIDGen());
        task.setTaskStatus(TaskStatus.NEW);
        taskMap.put(task.getUuid(), task);
        return task.getUuid();
    }

    @Override
    public String createSubTask(SubTask subTask) {
        subTask.setUuid(UUIDGen());
        subTask.setTaskStatus(TaskStatus.NEW);
        subTaskMap.put(subTask.getUuid(), subTask);
        updateEpicStatus(subTask.getEpicUuidUuid());
        return subTask.getUuid();
    }

    @Override
    public String createEpic(Epic epic) {
        epic.setUuid(UUIDGen());
        epic.setTaskStatus(TaskStatus.NEW);
        epicMap.put(epic.getUuid(), epic);
        updateEpicStatus(epic.getUuid());
        return epic.getUuid();
    }

    // Get
    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<SubTask>(subTaskMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<Epic>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(String EpicUuid) {
        ArrayList<SubTask> forPrint = new ArrayList<>();
        for (String string : subTaskMap.keySet()) {
            if (EpicUuid.equals(subTaskMap.get(string).getEpicUuidUuid())) {
                forPrint.add(subTaskMap.get(string));
            }
        }
        return forPrint;
    }

    @Override
    public Task getTask(String uuid) {
        historyManager.add(taskMap.get(uuid));
        return taskMap.get(uuid);
    }


    @Override
    public SubTask getSubTask(String uuid) {
        historyManager.add(subTaskMap.get(uuid));
        return subTaskMap.get(uuid);
    }

    @Override
    public Epic getEpic(String uuid) {
        historyManager.add(epicMap.get(uuid));
        return epicMap.get(uuid);
    }


    @Override
    public void updateTaskParameters(Task task) {
        if (task != null && task.getName() != null) {
            taskMap.get(task.getUuid()).setName(task.getName());
        }
        if (task != null && task.getDescription() != null) {
            taskMap.get(task.getUuid()).setDescription(task.getDescription());
        }
    }

    @Override
    public void updateSubTaskParameters(SubTask subTask) {
        if (subTask != null && subTask.getName() != null) {
            subTaskMap.get(subTask.getUuid()).setName(subTask.getName());
        }
        if (subTask != null && subTask.getDescription() != null) {
            subTaskMap.get(subTask.getUuid()).setDescription(subTask.getDescription());
        }
        if (subTask != null && subTask.getEpicUuidUuid() != null && !subTask.getUuid().equals(subTask.getEpicUuidUuid())) {
            SubTask task = subTaskMap.get(subTask.getUuid());
            String oldEpicUuid = task.getEpicUuidUuid();
            task.setEpicUuidUuid(subTask.getEpicUuidUuid());
            subTaskMap.put(task.getUuid(), task);
            updateEpicStatus(oldEpicUuid);
            updateEpicStatus(subTask.getEpicUuidUuid());
        }
    }

    @Override
    public void updateEpicParameters(Epic epic) {
        if (epic != null && epic.getName() != null) {
            epicMap.get(epic.getUuid()).setName(epic.getName());
        }
        if (epic != null && epic.getDescription() != null) {
            epicMap.get(epic.getUuid()).setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteTasks() {
        for (Task task : taskMap.values()) {
            historyManager.remove(task);
        }
        taskMap.clear();


    }

    @Override
    public void deleteSubTasks() {
        for (SubTask subTask : subTaskMap.values()) {
            historyManager.remove(subTask);
        }
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            updateEpicStatus(epic.getUuid());
        }


    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epicMap.values()) {
            historyManager.remove(epic);
        }
        for (SubTask subTask : subTaskMap.values()) {
            historyManager.remove(subTask);
        }
        epicMap.clear();
        subTaskMap.clear();

    }

    @Override
    public void deleteTask(String uuid) {
        historyManager.remove(taskMap.get(uuid));
        taskMap.remove(uuid);

    }

    @Override
    public void deleteSubTask(String uuid) {
        historyManager.remove(subTaskMap.get(uuid));
        String oldEpicUUid = subTaskMap.get(uuid).getEpicUuidUuid();
        subTaskMap.remove(uuid);
        updateEpicStatus(oldEpicUUid);

    }

    @Override
    public void deleteEpic(String uuid) {
        historyManager.remove(epicMap.get(uuid));
        Iterator<String> it = subTaskMap.keySet().iterator();
        while (it.hasNext()) {

            String key = it.next();

            if (subTaskMap.get(key).getEpicUuidUuid().equals(uuid))
                historyManager.remove(subTaskMap.get(key));
            it.remove();
        }
        epicMap.remove(uuid);

    }

    @Override
    public TaskStatus checkTaskStatus(String uuid) {
        return getTask(uuid).getTaskStatus();
    }

    @Override
    public TaskStatus checkSubTaskStatus(String uuid) {
        return getSubTask(uuid).getTaskStatus();
    }

    @Override
    public TaskStatus checkEpicStatus(String uuid) {
        return getEpic(uuid).getTaskStatus();
    }

    @Override
    public void updateTaskStatus(String uuid, TaskStatus newStatus) {
        taskMap.get(uuid).setTaskStatus(newStatus);
    }

    @Override
    public void updateSubTaskStatus(String uuid, TaskStatus newStatus) {
        subTaskMap.get(uuid).setTaskStatus(newStatus);
        updateEpicStatus(subTaskMap.get(uuid).getEpicUuidUuid());
    }


    @Override
    public void updateEpicStatus(String uuid)  {
        Epic epic = getEpic(uuid);
        int counterForCompletedSubTasks = 0;
        int counterForLinkedSubTasks = 0;
        boolean statusUnderEpic = false;
        boolean previousStatusUnderEpic = true;
        if (epic != null) {
            List<SubTask> subTasks = getEpicSubtasks(uuid);
            for (SubTask subTask : subTasks) {
                counterForLinkedSubTasks = counterForLinkedSubTasks + 1;
                if (subTask.getTaskStatus().equals(TaskStatus.DONE)) {
                    statusUnderEpic = previousStatusUnderEpic;
                    counterForCompletedSubTasks = counterForCompletedSubTasks + 1;
                } else {
                    statusUnderEpic = false;
                    previousStatusUnderEpic = false;
                }
            }
            if (statusUnderEpic) {
                epicMap.get(uuid).setTaskStatus(TaskStatus.DONE);
                System.out.println("Под эпиком " + epic.getName() + " все задачи завершены");
            } else {
                epicMap.get(uuid).setTaskStatus(TaskStatus.IN_PROGRESS);


            }
            if (counterForLinkedSubTasks == 0) {
                epicMap.get(uuid).setTaskStatus(TaskStatus.NEW);
                System.out.println("Под эпиком " + epic.getName() + " " + counterForCompletedSubTasks + " завершенные задачи " + (counterForLinkedSubTasks - counterForCompletedSubTasks) + " незавершенных задач");
            }
        }
    }


}
