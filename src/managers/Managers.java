package managers;

import java.io.File;
import java.nio.file.Path;

public class Managers {
    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackedManager(Path path) {
        return new FileBackedTaskManager().loadFromFile(new File(String.valueOf(path)));
    }
}
