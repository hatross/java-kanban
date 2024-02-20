import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        while (history.size() >= 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
