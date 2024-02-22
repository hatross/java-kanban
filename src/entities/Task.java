package entities;

import java.util.Objects;

public class Task {
    private int uid;
    private String summary;
    private String description;
    private Status status;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(Integer uid, String summary, String description, Status status) {
        this.uid = uid;
        this.summary = summary;
        this.description = description;
        this.status = status;
    }

    public Task(String summary, String description, Status status) {
        this.summary = summary;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "entities.Task{" +
                "uid=" + uid +
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
        if (!Objects.equals(summary, task.summary)) return false;
        if (!Objects.equals(description, task.description)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + status.hashCode();
        return result;
    }
}
