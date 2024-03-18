package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> linkedTasks;

    public Epic(Integer uid, String summary, String description, ArrayList<Integer> linkedTasks) {
        super(uid, summary, description, Status.NEW);
        this.linkedTasks = Objects.requireNonNullElseGet(linkedTasks, ArrayList::new);
    }

    public Epic(String summary, String description, ArrayList<Integer> linkedTasks) {
        super(summary, description, Status.NEW);
        this.linkedTasks = Objects.requireNonNullElseGet(linkedTasks, ArrayList::new);
    }

    public ArrayList<Integer> getLinkedTasks() {
        return linkedTasks;
    }

    public void setLinkedTasks(ArrayList<Integer> linkedTasks) {
        this.linkedTasks = linkedTasks;
    }

    @Override
    public String toString() {
        String result = "entities.Epic{" +
                "uid=" + getUid() +
                ", summary='" + getSummary() + '\'';

        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }

        result = result + ", status=" + getStatus();

        if (!linkedTasks.isEmpty()) {
            result = result + ", linkedTasks=" + Arrays.toString(linkedTasks.toArray());
        } else {
            result = result + ", linkedTasks=null";
        }
        return result + "}";
    }
}