import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private Integer uid = 0;
    private ArrayList<Integer> newLinkedTasks;

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(HashMap<Integer, Task> taskList) {
        this.taskList = taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public void setEpicList(HashMap<Integer, Epic> epicList) {
        this.epicList = epicList;
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(HashMap<Integer, Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void createTask(Task task) {
        task.setUid(++uid);
        taskList.put(uid, task);
    }

    public void createEpic(Epic epic) {
        epic.setUid(++uid);
        epicList.put(uid, epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setUid(++uid);
        subtaskList.put(uid, subtask);

        if (subtask.epicId != null && epicList.containsKey(subtask.epicId)) {
            newLinkedTasks = epicList.get(subtask.epicId).getLinkedTasks();
            newLinkedTasks.add(uid);
            epicList.get(subtask.epicId).setLinkedTasks(newLinkedTasks);
        }

    }

    public void updateTask(Task task) {
        taskList.put(task.getUid(), task);
    }
    public void updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = subtaskList.get(subtask.getUid());

        if (epicList.containsKey(updatedSubtask.epicId) && updatedSubtask.epicId != null) {
            if (!updatedSubtask.epicId.equals(subtask.epicId)) {
                Epic oldEpic = epicList.get(updatedSubtask.epicId);

                if (!oldEpic.getLinkedTasks().isEmpty()) {
                    newLinkedTasks = oldEpic.getLinkedTasks();
                    newLinkedTasks.remove(subtask.getUid());
                    oldEpic.setLinkedTasks(newLinkedTasks);
                    Epic newEpic = epicList.get(subtask.epicId);
                    if (newEpic != null) {
                        newLinkedTasks = newEpic.getLinkedTasks();
                        newLinkedTasks.add(subtask.epicId);
                    }
                }
            }
        }

        subtaskList.put(subtask.getUid(), subtask);
        updateEpicStatus(subtask.epicId, subtask.getStatus());
    }

    public void updateEpic(Epic epic) {
        ArrayList<Integer> storedLinkedTasks = epicList.get(epic.getUid()).getLinkedTasks();
        ArrayList<Integer> newLinkedTasks = epic.getLinkedTasks();
        if (newLinkedTasks == null && storedLinkedTasks != null) {
            Epic updatedEpic = epicList.get(epic.getUid());
            updatedEpic.setUid(epic.getUid());
            updatedEpic.setSummary(epic.getSummary());
            updatedEpic.setSummary(epic.getSummary());
        } else {
            epicList.put(epic.getUid(), epic);
        }
    }

    public Task getTaskById(Integer id) {
        if (epicList.containsKey(id)) {
            return epicList.get(id);
        } else if (subtaskList.containsKey(id)) {
            return subtaskList.get(id);
        } else if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else {
            return null;
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasksList = new ArrayList<>();

        for (Integer epicId : epicList.keySet()) {
            Epic epic = epicList.get(epicId);
            allTasksList.add(epic);
        }

        for (Integer subtaskId : subtaskList.keySet()) {
            Subtask subtask = subtaskList.get(subtaskId);
            allTasksList.add(subtask);
        }

        for (Integer taskId : taskList.keySet()) {
            Task task = taskList.get(taskId);
            allTasksList.add(task);
        }

        return allTasksList;
    }

    public ArrayList<Subtask> getEpicSubtasks(Integer epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        Subtask subtask;

        for (Integer subtaskId : epicList.get(epicId).getLinkedTasks()) {
            subtask = subtaskList.get(subtaskId);
            subtasks.add(subtask);
        }

        return subtasks;
    }


    public void deleteTaskById(Integer id) {
        if (epicList.containsKey(id)) {
            for (Integer subtaskId : epicList.get(id).getLinkedTasks()) {
                subtaskList.remove(subtaskId);
            }
            epicList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            for (Integer epicId : epicList.keySet()) {
                for (Integer subtaskId : epicList.get(epicId).getLinkedTasks()) {
                    if (subtaskId.equals(id)) {
                        newLinkedTasks = epicList.get(epicId).getLinkedTasks();
                        newLinkedTasks.remove(subtaskId);
                        updateEpicStatus(subtaskList.get(subtaskId).epicId, Status.NEW);
                    }
                }
            }
            subtaskList.remove(id);
        } else taskList.remove(id);
    }

    public void deleteAllTasks() {
        epicList.clear();
        subtaskList.clear();
        taskList.clear();
    }

    private void updateEpicStatus(Integer epicId, Status status) {
        Epic epic = epicList.get(epicId);
        if (isDoneEpic(epicId)) {
            epic.setStatus(Status.DONE);
        } else if (status == Status.IN_PROGRESS || (status == Status.DONE && !isDoneEpic(epicId))) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (isNewEpic(epicId)) {
            epic.setStatus(Status.NEW);
        }
    }

    private boolean isDoneEpic(Integer epicId) {
        Epic epic = epicList.get(epicId);
        int doneSubtasks = 0;
        if (epic.getLinkedTasks().isEmpty()) {
            return false;
        } else {
            for (Integer taskId : epic.getLinkedTasks()) {
                Subtask subtask = subtaskList.get(taskId);
                if (subtask.getStatus() == Status.DONE) {
                    doneSubtasks++;
                }
            }
            return doneSubtasks == epic.getLinkedTasks().size();
        }
    }

    private boolean isNewEpic(Integer epicId) {
        Epic epic = getEpicList().get(epicId);
        int newSubtasks = 0;
        if (epic.getLinkedTasks().isEmpty()) {
            return false;
        } else {
            for (Integer taskId : epic.getLinkedTasks()) {
                Subtask subtask = subtaskList.get(taskId);
                if (subtask.getStatus() == Status.NEW) {
                    newSubtasks++;
                }
            }
            return newSubtasks == epic.getLinkedTasks().size();
        }
    }
}
