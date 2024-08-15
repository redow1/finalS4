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
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    static File testFile = new File("testfiles/test.txt");
    static TaskManager taskManager = Managers.getFileBackedManager(Path.of(testFile.getAbsolutePath()));
    static File testFile1 = new File("testfiles/test1.txt");
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
        Task task = new Task("не спать", "вообще", TaskType.Task, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(15), null);
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
        Task task = new Task("поспать1", "часов 8", TaskType.Task, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(12), null);
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
        Epic epic = new Epic("сходить в магазин4", "сходить в магазин прям сейчас", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(10), null);
        final String epicUuid1 = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("сходить в магазин2", "купить арбуз", TaskType.SubTask, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(8), null, epicUuid1);
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
        Epic epic = new Epic("сходить в магазин1", "сходить в магазин завтра", TaskType.Epic, Duration.ofMinutes(1), LocalDateTime.now().minusMinutes(6), null);
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
    void loadFile() {
        int expectedContent = taskManager.getTasks().size();
        int content = taskManager1.getTasks().size();
        assertEquals(expectedContent, content);
    }

}
