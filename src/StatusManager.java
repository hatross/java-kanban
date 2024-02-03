public class StatusManager {
    public void updateStatus(Integer id, Status status) {
        if (TaskManager.taskList.containsKey(id)) {
            Task task = TaskManager.taskList.get(id);
            task.status = status;
        } else if (TaskManager.subtaskList.containsKey(id)) {
            Subtask subtask = TaskManager.subtaskList.get(id);
            subtask.status = status;
            updateEpicStatus(subtask.epicId, subtask.status);
        }
    }

    private boolean isDoneEpic(Integer epicId) {
        Epic epic = TaskManager.epicList.get(epicId);
        int doneSubtasks = 0;
        if (epic.linkedTasks.isEmpty()) {
            return false;
        } else {
            for (Integer taskId : epic.linkedTasks) {
                Subtask subtask = TaskManager.subtaskList.get(taskId);
                if (subtask.status == Status.DONE) {
                    doneSubtasks++;
                }
            }
            return doneSubtasks == epic.linkedTasks.size();
        }
    }

    private boolean isNewEpic(Integer epicId) {
        Epic epic = TaskManager.epicList.get(epicId);
        int newSubtasks = 0;
        if (epic.linkedTasks.isEmpty()) {
            return false;
        } else {
            for (Integer taskId : epic.linkedTasks) {
                Subtask subtask = TaskManager.subtaskList.get(taskId);
                if (subtask.status == Status.NEW) {
                    newSubtasks++;
                }
            }
            return newSubtasks == epic.linkedTasks.size();
        }
    }

    private void updateEpicStatus(Integer epicId, Status status) {
        Epic epic = TaskManager.epicList.get(epicId);

        if (isDoneEpic(epicId)) {
            epic.status = Status.DONE;
        } else if (status == Status.IN_PROGRESS || (status == Status.DONE && !isDoneEpic(epicId))) {
            epic.status = Status.IN_PROGRESS;
        } else if (isNewEpic(epicId)) {
            epic.status = Status.NEW;
        }
    }
}
