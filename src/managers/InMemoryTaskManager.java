package managers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public Map<String, Task> getTaskMap() {
        return taskMap;
    }

    public Map<String, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public Map<String, Epic> getEpicMap() {
        return epicMap;
    }

    protected Map<String, Task> taskMap = new HashMap<>();
    protected Map<String, SubTask> subTaskMap = new HashMap<>();
    protected Map<String, Epic> epicMap = new HashMap<>();

    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    public Map groupMapsForSave(Map taskMap, Map subTaskMap, Map epicMap) {
        Map<String, Task> mergedMap = new HashMap<>();
        mergedMap.putAll(taskMap);
        mergedMap.putAll(subTaskMap);
        mergedMap.putAll(epicMap);
        return mergedMap;
    }

    @Override
    public String createTask(Task task) {
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(existingTask -> isTimeForTasksOverlap(task, existingTask));
        if (hasOverlap) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения");
        }
        task.setUuid(uuidgen());
        task.setTaskStatus(TaskStatus.NEW);
        taskMap.put(task.getUuid(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task.getUuid();
    }

    @Override
    public String createSubTask(SubTask subTask) {
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(existingTask -> isTimeForTasksOverlap(subTask, existingTask));

        if (hasOverlap) {
            throw new IllegalArgumentException("Подзадача пересекается с другой задачей или подзадачей по времени выполнения");
        }
        subTask.setUuid(uuidgen());
        subTask.setTaskStatus(TaskStatus.NEW);
        subTaskMap.put(subTask.getUuid(), subTask);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
        updateEpicStatus(subTask.getEpicUuid());
        setEpicEndTime(subTask.getEpicUuid());
        return subTask.getUuid();
    }

    @Override
    public String createEpic(Epic epic) {
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(existingTask -> isTimeForTasksOverlap(epic, existingTask));
        if (hasOverlap) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения");
        }
        epic.setUuid(uuidgen());
        epic.setTaskStatus(TaskStatus.NEW);
        epicMap.put(epic.getUuid(), epic);
        updateEpicStatus(epic.getUuid());
        setEpicEndTime(epic.getUuid());
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        return epic.getUuid();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<SubTask>(subTaskMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getEpicSubtasks(String epicUuid) {
        return (ArrayList<SubTask>)subTaskMap.values().stream()
                .filter(subTask -> epicUuid.equals(subTask.getEpicUuid()))
                .collect(Collectors.toList());
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
    public void updateTaskParameters(String uuid, String newName, String newDescription, Duration newDuration, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Task task = taskMap.get(uuid);
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        if (task != null && newName != null) {
            taskMap.get(task.getUuid()).setName(newName);
        }
        if (task != null && newDescription != null) {
            taskMap.get(task.getUuid()).setDescription(newDescription);
        }
        if (task != null && newStartTime != null) {
            task.setStartTime(newStartTime);
        }
        if (task != null && newDuration != null) {
            task.setDuration(newDuration);
        }
        if (task != null && newEndTime != null) {
            task.setEndTime(newEndTime);
        }
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubTaskParameters(String uuid, String newName, String newDescription, Duration newDuration, LocalDateTime newStartTime, LocalDateTime newEndTime, String newEpicUuid) {
        SubTask subTask = subTaskMap.get(uuid);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.remove(subTask);
        }
        if (subTask != null && newName != null) {
            subTaskMap.get(subTask.getUuid()).setName(newName);
        }
        if (subTask != null && newDescription != null) {
            subTaskMap.get(subTask.getUuid()).setDescription(newDescription);
        }
        if (subTask != null && newStartTime != null) {
            subTask.setStartTime(newStartTime);
            setEpicEndTime(subTask.getEpicUuid());
        }
        if (subTask != null && newDuration != null) {
            subTask.setDuration(newDuration);
            setEpicEndTime(subTask.getEpicUuid());
        }
        if (subTask != null && newEndTime != null) {
            subTask.setEndTime(newEndTime);
            setEpicEndTime(subTask.getEpicUuid());
        }
        if (subTask != null && subTask.getEpicUuid() != null && !subTask.getUuid().equals(newEpicUuid)) {
            SubTask task = subTaskMap.get(subTask.getUuid());
            String oldEpicUuid = task.getEpicUuid();
            task.setEpicUuidUuid(newEpicUuid);
            subTaskMap.put(task.getUuid(), task);
            updateEpicStatus(oldEpicUuid);
            updateEpicStatus(subTask.getEpicUuid());
            setEpicEndTime(oldEpicUuid);
            setEpicEndTime(subTask.getEpicUuid());
        }
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
    }


    @Override
    public void updateEpicParameters(String uuid, String newName, String newDescription, Duration newDuration, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Epic epic = epicMap.get(uuid);
        if (epic.getStartTime() != null) {
            prioritizedTasks.remove(epic);
        }
        if (epic != null && newName != null) {
            epic.setName(newName);
        }
        if (epic != null && newDescription != null) {
            epic.setDescription(newDescription);
        }
        if (epic != null && newStartTime != null) {
            epic.setStartTime(newStartTime);
        }
        if (epic != null && newDuration != null) {
            epic.setDuration(newDuration);
        }
        if (epic != null && newEndTime != null) {
            epic.setEndTime(newEndTime);
        }
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        epicMap.put(uuid,epic);
    }

    @Override
    public void updateEpicTimePrametrs(String uuid, String newName, String newDescription, Duration newDuration, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Epic epic = epicMap.get(uuid);
        if (epic.getStartTime() != null) {
            prioritizedTasks.remove(epic);
        }
        if (epic != null && newName != null) {
            epic.setName(newName);
        }
        if (epic != null && newDescription != null) {
            epic.setDescription(newDescription);
        }
        if (epic != null && newStartTime != null) {
            epic.setStartTime(newStartTime);
        }
        if (epic != null && newDuration != null) {
            epic.setDuration(newDuration);
        }
        if (epic != null && newEndTime != null) {
            epic.setEndTime(newEndTime);
        }
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        epicMap.put(uuid,epic);
    }

    public void deleteTasks() {
        taskMap.values().stream()
                .forEach(task -> {
                    if (task.getStartTime() != null) {
                        prioritizedTasks.remove(task);
                    }
                    historyManager.remove(task);
                });

        taskMap.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTaskMap.values().stream()
                .forEach(subTask -> {
                    if (subTask.getStartTime() != null) {
                        prioritizedTasks.remove(subTask);
                    }
                    historyManager.remove(subTask);
                });

        subTaskMap.clear();

        epicMap.values().forEach(epic -> {
            updateEpicStatus(epic.getUuid());
            setEpicEndTime(epic.getUuid());
        });
    }

    @Override
    public void deleteEpics() {
        epicMap.values().stream()
                .forEach(epic -> {
                    historyManager.remove(epic);
                });

        subTaskMap.values().forEach(historyManager::remove);

        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public void deleteTask(String uuid) {
        if (taskMap.get(uuid).getStartTime() != null) {
            prioritizedTasks.remove(taskMap.get(uuid));
        }
        historyManager.remove(taskMap.get(uuid));
        taskMap.remove(uuid);

    }

    @Override
    public void deleteSubTask(String uuid) {
        if (subTaskMap.get(uuid).getStartTime() != null) {
            prioritizedTasks.remove(subTaskMap.get(uuid));
        }
        historyManager.remove(subTaskMap.get(uuid));
        String oldEpicUUid = subTaskMap.get(uuid).getEpicUuid();
        subTaskMap.remove(uuid);
        updateEpicStatus(oldEpicUUid);
        setEpicEndTime(oldEpicUUid);

    }

    @Override
    public void deleteEpic(String uuid) {
        historyManager.remove(epicMap.get(uuid));
        Iterator<String> it = subTaskMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (subTaskMap.get(key).getEpicUuid().equals(uuid)) {
                historyManager.remove(subTaskMap.get(key));
                it.remove();
            }
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
        updateEpicStatus(subTaskMap.get(uuid).getEpicUuid());
        setEpicEndTime(subTaskMap.get(uuid).getEpicUuid());
    }


    @Override
    public void updateEpicStatus(String uuid)  {
        Epic epic = getEpic(uuid);
        if (epic == null) {
            return;
        }
        List<SubTask> subTasks = getEpicSubtasks(uuid);
        long completedSubTasksCount = subTasks.stream()
                .filter(subTask -> subTask.getTaskStatus().equals(TaskStatus.DONE))
                .count();
        long totalSubTasksCount = subTasks.size();
        if (totalSubTasksCount == 0) {
            epic.setTaskStatus(TaskStatus.NEW);
            System.out.println("Под эпиком " + epic.getName() + " нет задач.");
        } else if (completedSubTasksCount == totalSubTasksCount) {
            epic.setTaskStatus(TaskStatus.DONE);
            System.out.println("Под эпиком " + epic.getName() + " все задачи завершены.");
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            System.out.println("Под эпиком " + epic.getName() + " " + completedSubTasksCount + " завершенные задачи, "
                    + (totalSubTasksCount - completedSubTasksCount) + " незавершенных задач.");
        }
    }

    @Override
    public void setEpicEndTime(String uuid) {
        Epic epic = getEpic(uuid);
        if (epic == null) {
            return;
        }

        List<SubTask> subTasks = getEpicSubtasks(uuid);
        if (subTasks.isEmpty()) {
            System.out.println("Под эпиком нет подзадач");
            updateEpicTimePrametrs(uuid, null, null, Duration.ZERO, null, null);
            return;
        }

        LocalDateTime minStartTime = subTasks.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime maxEndTime = subTasks.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration sumOfSubTasksDuration = subTasks.stream()
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        updateEpicTimePrametrs(uuid, null, null, sumOfSubTasksDuration, minStartTime, maxEndTime);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isTimeForTasksOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
