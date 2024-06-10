package managers;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> historyList = new LinkedList<>();
    int sizeForPrint = 10;

    @Override
    public void add(Task task){
        if (task != null) {
            historyList.add(task);
        }
    }
    @Override
    public LinkedList<Task> getHistory() {
        int size = historyList.size()-1;
        LinkedList<Task> returnList = new LinkedList<>();
        if (size <= 9) {
            return historyList;
        } else {
            for (int i = size; i > historyList.size() - sizeForPrint - 1; i--) {
                returnList.add(historyList.get(i));
            }
            return returnList;
        }


    }
}
