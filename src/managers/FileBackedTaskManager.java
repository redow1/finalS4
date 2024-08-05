package managers;

import exeptions.ManagerSaveException;
import tasks.*;
import tasks.formatters.CSVFormatter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager (Path path) {
        path = this.path;
    }
    public FileBackedTaskManager () {
    }

    Path path;

    File file;

    @Override
    public String createTask(Task task) {
        String uuid = super.createTask(task);
        saveNewTask(task);
        return uuid;
    }
    @Override
    public String createSubTask(SubTask subTask) {
        String uuid = super.createSubTask(subTask);
        saveNewTask(subTask);
        return uuid;
    }

    @Override
    public String createEpic(Epic epic) {
        String uuid = super.createEpic(epic);
        saveNewTask(epic);
        return uuid;
    }

    @Override
    public void updateTaskParameters(String uuid, String newName, String newDescription) {
        super.updateTaskParameters(uuid, newName, newDescription);
        reSaveFile();
    }

    @Override
    public void updateSubTaskParameters(String uuid, String newName, String newDescription, String newEpicUuid) {
        super.updateSubTaskParameters(uuid, newName, newDescription, newEpicUuid);
        reSaveFile();
    }

    @Override
    public void updateEpicParameters(String uuid, String newName, String newDescription) {
        super.updateEpicParameters(uuid, newName, newDescription);
        reSaveFile();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        reSaveFile();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        reSaveFile();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        reSaveFile();
    }

    @Override
    public void deleteTask(String uuid) {
        super.deleteTask(uuid);
        reSaveFile();
    }

    @Override
    public void deleteSubTask(String uuid) {
        super.deleteSubTask(uuid);
        reSaveFile();
    }

    @Override
    public void deleteEpic(String uuid) {
        super.deleteEpic(uuid);
        reSaveFile();
    }

    public void saveNewTask(Task task) {
        if (fileExists(file)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                if (file.length() == 0) {
                    bw.write(CSVFormatter.getHeaders());
                    bw.newLine();
                    bw.write(CSVFormatter.toString(task));
                } else {
                    bw.newLine();
                    bw.write(CSVFormatter.toString(task));
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        } else {
            System.out.println("Фаил не существует");
        }
    }

    public void reSaveFile() {
        Map<String, Task> mergedMapForSave = groupMapsForSave(taskMap, subTaskMap, epicMap);
        if (fileExists(file)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Task task: mergedMapForSave.values()) {
                   bw.write(CSVFormatter.toString(task));
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        } else {
            System.out.println("Фаил не существует");
        }
    }

    public FileBackedTaskManager loadFromFile(File file) {
        this.path = file.toPath();
        this.file = file;
        List<String> linesForLoad;
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);
        String[] split;
        if (fileExists(file)) {
            try {
                linesForLoad = Files.readAllLines(path);
                for (int i = 1; i < linesForLoad.size(); i++) {
                    split = CSVFormatter.fromString(linesForLoad.get(i));
                    String uuid = split[0];
                    TaskType taskType = TaskType.valueOf(split[1]);
                    String name = split[2];
                    TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
                    String description = split[4];

                    if (taskType.equals(TaskType.Task)) {
                        Task task = new Task(name, description, taskType);
                        task.setUuid(uuid);
                        task.setTaskStatus(taskStatus);
                        taskMap.put(task.getUuid(),task);
                    } else if (taskType.equals(TaskType.SubTask)) {
                        String epic = split[5];
                        SubTask subTask = new SubTask(name, description, taskType, epic);
                        subTask.setUuid(uuid);
                        subTask.setTaskStatus(taskStatus);
                        subTaskMap.put(subTask.getUuid(),subTask);
                    } else if (taskType.equals(TaskType.Epic)) {
                        Epic epic = new Epic(name, description, taskType);
                        epic.setUuid(uuid);
                        epic.setTaskStatus(taskStatus);
                        taskMap.put(epic.getUuid(),epic);
                    } else {
                        System.out.println("Такой тип задачи не поддерживается");
                        break;
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        } else {
            System.out.println("Фаил не существует");
        }
        return fileBackedTaskManager;
    }

    private static boolean fileExists(File file) {
        return (file.exists() && file.isFile());
    }
}
