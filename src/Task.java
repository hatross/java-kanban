import java.util.Objects;

public class Task {
    int uid;
    String projectCode;
    String summary;
    String description;
    Status status;
    Integer epicId;

    public Task(String projectCode, Integer uid, String summary, String description, Status status) {
        this.projectCode = projectCode;
        this.uid = uid;
        this.summary = summary;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "projectCode='" + projectCode + '\'' +
                ", taskNumber=" + uid +
                ", summary='" + summary + '\'';

        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", description.length=null";
        }

        return result + ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (uid != task.uid) return false;
        if (!Objects.equals(projectCode, task.projectCode)) return false;
        if (!Objects.equals(summary, task.summary)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (status != task.status) return false;
        return Objects.equals(epicId, task.epicId);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (projectCode != null ? projectCode.hashCode() : 0);
        result = 31 * result + uid;
        return result;
    }
}
