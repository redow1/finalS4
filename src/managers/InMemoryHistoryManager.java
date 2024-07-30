package managers;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> historyList = new LinkedList<>();
    int sizeForPrint = 10;


    @Override
    public void add(Task task){
        if (task != null) {
            if (historyList.size() < sizeForPrint) {
                historyList.add(task);
            } else {
                for (int i = historyList.size() - 1; i > 0; i--) {
                    historyList.set(i, historyList.get(i - 1));
                }
                historyList.set(1, task);
            }
        }
    }

    @Override
    public LinkedList<Task> getHistory() {

        return new LinkedList<>(historyList);
    }


}
