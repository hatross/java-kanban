package managers;

import entities.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_HISTORY_SIZE = 10;
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        while (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
