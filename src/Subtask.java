import java.util.Objects;

public class Subtask extends Task {
    public Subtask(String projectCode, Integer taskNumber, String summary, String description, Status status
                   , Integer epicId) {
        super(projectCode, taskNumber, summary, description, status);
        this.epicId = Objects.requireNonNull(epicId);
    }

    public String toString() {
        String result = "Subtask{" +
                "projectCode='" + projectCode + '\'' +
                ", taskNumber=" + uid +
                ", summary='" + summary + '\'';

        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", description.length=null";
        }

        return result + ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}
