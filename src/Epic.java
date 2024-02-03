import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    ArrayList<Integer> linkedTasks;
    public Epic(String projectCode, Integer uid, String summary, String description, Status status, ArrayList<Integer> linkedTasks) {
        super(projectCode, uid, summary, description, status);
        this.linkedTasks = Objects.requireNonNullElseGet(linkedTasks, ArrayList::new);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "projectCode='" + projectCode + '\'' +
                ", taskNumber=" + uid +
                ", summary='" + summary + '\'';

        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", description.length=null";
        }

        if (!linkedTasks.isEmpty()) {
            result = result + ", linkedTasks=" + linkedTasks;
        } else {
            result = result + ", linkedTasks=null";
        }
        return result + "}";
    }
}
