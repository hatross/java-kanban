import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer uid, String summary, String description, Status status, Integer epicId) {
        super(uid, summary, description, status);
        this.epicId = Objects.requireNonNull(epicId);
    }

    public Subtask(String summary, String description, Status status, Integer epicId) {
        super(summary, description, status);
        this.epicId = Objects.requireNonNull(epicId);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public String toString() {
        String result = "Subtask{" +
                "uid=" + getUid() +
                ", summary='" + getSummary() + '\'';

        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }

        return result + ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
