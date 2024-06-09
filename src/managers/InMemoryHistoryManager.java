package managers;
import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task){
        if (historyList.size()<10) {
            historyList.add(task);
        } else {
            for (int i = historyList.size()-1; i > 0; i-- ){
                historyList.set(i, historyList.get(i-1));
            }
            historyList.set(1,task);
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
