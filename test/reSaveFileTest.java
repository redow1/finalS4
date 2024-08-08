package tasks;

import exeptions.ManagerSaveException;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import tasks.formatters.CSVFormatter;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class reSaveFileTest {

    static File testFile = new File("testfiles/test.txt");
    static TaskManager taskManager = Managers.getFileBackedManager(Path.of(testFile.getAbsolutePath()));

    @AfterAll
    public static void clearTestFile() throws IOException {
        if (testFile.exists()) {
            testFile.delete();
            testFile.createNewFile();
        }
    }

    @Test
    void reSaveFile() {
        Task task = new Task("не спать почти", "вообще", TaskType.Task);
        String uuid5 = taskManager.createTask(task);
        taskManager.updateTaskParameters(uuid5, null, "for test1");
        Task task1 = taskManager.getTask(uuid5);
        String stringToAppend = CSVFormatter.toString(task1);
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            String lineForTest = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(task.getUuid())) {
                    lineForTest = line;
                }
            }
            assertEquals(lineForTest, stringToAppend);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

