package test;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;
import tasks.formatters.CSVFormatter;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    static File testFile = new File("/java-kanban/testfiles/test.txt");
    static TaskManager taskManager = Managers.getFileBackedManager(Path.of(testFile.getAbsolutePath()));
    static File testFile1 = new File("/java-kanban/testfiles/test1.txt");
    static TaskManager taskManager1 = Managers.getFileBackedManager(Path.of(testFile1.getAbsolutePath()));

    @AfterAll
    public static void clearTestFile() throws IOException {
        if (testFile.exists()) {
            testFile.delete();
            testFile.createNewFile();
        }
    }

    @Test
    void checkTitleLine() {
        Task task = new Task("не спать", "вообще", TaskType.Task);
        String uuid1 = taskManager.createTask(task);
        String stringToAppend = CSVFormatter.getHeaders();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            String lineForTest = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(",description,")) {
                    lineForTest = line;
                }
            }
            assertEquals(stringToAppend, lineForTest);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveNewTaskToEmptyFile() {
        Task task = new Task("поспать1", "часов 8", TaskType.Task);
        final String uuid2 = taskManager.createTask(task);
        String stringToAppend = CSVFormatter.toString(task);
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            String lineForTest = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(task.getUuid())) {
                    lineForTest = line;
                }
            }
            assertEquals(lineForTest, stringToAppend);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveNewSubTask() {
        Epic epic = new Epic("сходить в магазин2", "сходить в магазин прям сейчас", TaskType.Epic);
        final String epicUuid1 = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин2", "купить арбуз", TaskType.SubTask, epicUuid1);
        final String subTask1Uuid1 = taskManager.createSubTask(subTask);
        String stringToAppend = CSVFormatter.toString(subTask);
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            String lineForTest = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(subTask.getUuid())) {
                    lineForTest = line;
                }
            }
            assertEquals(lineForTest, stringToAppend);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveNewEpic() {
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic);
        final String uuid3 = taskManager.createEpic(epic);
        String stringToAppend = CSVFormatter.toString(epic);
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            String lineForTest = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(epic.getUuid())) {
                    lineForTest = line;
                }
            }
            assertEquals(lineForTest, stringToAppend);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadFile(){
        ArrayList<Task> expectedContent = taskManager.getTasks();
        ArrayList<Task> content = taskManager1.getTasks();
        assertEquals(expectedContent, content);
    }
}
