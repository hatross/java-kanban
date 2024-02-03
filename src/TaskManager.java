import java.util.*;

public class TaskManager {
    static HashMap<Integer, Task> taskList = new HashMap<>();
    static HashMap<Integer, Epic> epicList = new HashMap<>();
    static HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    static HashMap<String, Integer> projectCounters = new HashMap<>();
    static int uid;
    StatusManager statusManager = new StatusManager();

    public void create(Object obj) {
        String className = obj.getClass().getName();
        switch (className) {
            case "Task":
                Task task = (Task) obj;
                createTask(task);
                break;
            case "Subtask":
                Subtask subtask = (Subtask) obj;
                createSubtask(subtask);
                break;
            case "Epic":
                Epic epic = (Epic) obj;
                createEpic(epic);
                break;
        }
    }

    public void update(Integer id, Object obj) {
        String className = obj.getClass().getName();
        switch (className) {
            case "Task":
                Task task = (Task) obj;
                updateTask(id, task);
                break;
            case "Subtask":
                Subtask subtask = (Subtask) obj;
                updateSubtask(id, subtask);
                break;
            case "Epic":
                Epic epic = (Epic) obj;
                updateEpic(id, epic);
                break;
        }
    }

    private void createTask(Task task) {
        taskList.put(task.hashCode(), task);
    }

    private void createEpic(Epic epic) {
        epicList.put(epic.hashCode(), epic);
    }

    private void createSubtask(Subtask subtask) {
        subtaskList.put(subtask.hashCode(), subtask);

        if (subtask.epicId != null && epicList.containsKey(subtask.epicId)) {
            epicList.get(subtask.epicId).linkedTasks.add(subtask.hashCode());
        }
    }

    private void updateTask(Integer taskId, Task task) {
        taskList.remove(taskId);
        taskList.put(taskId, task);
        statusManager.updateStatus(taskId, task.status);
    }
    private void updateSubtask(Integer subtaskId, Subtask subtask) {
        Subtask updatedSubtask = subtaskList.get(subtaskId);

        if (epicList.containsKey(updatedSubtask.epicId) && updatedSubtask.epicId != null) {
            if (!updatedSubtask.epicId.equals(subtask.epicId)) {
                Epic oldEpic = epicList.get(updatedSubtask.epicId);

                if (!oldEpic.linkedTasks.isEmpty()) {
                    oldEpic.linkedTasks.remove(subtaskId);
                    Epic newEpic = epicList.get(subtask.epicId);
                    if (newEpic != null) {
                        newEpic.linkedTasks.add(subtask.epicId);
                    }
                }
            }
        }

        subtaskList.remove(subtaskId);
        subtaskList.put(subtaskId, subtask);
        statusManager.updateStatus(subtaskId, subtask.status);
    }

    private void updateEpic(Integer epicId, Epic epic) {
        ArrayList<Integer> storedLinkedTasks = epicList.get(epicId).linkedTasks;
        ArrayList<Integer> newLinkedTasks = epic.linkedTasks;
        if (newLinkedTasks == null && storedLinkedTasks != null) {
            Epic updatedEpic = epicList.get(epicId);
            updatedEpic.projectCode = epic.projectCode;
            updatedEpic.uid = epic.uid;
            updatedEpic.summary = epic.summary;
            updatedEpic.description = epic.description;
            updatedEpic.status = epic.status;
        } else {
            epicList.remove(epicId);
            epicList.put(epicId, epic);
        }
        statusManager.updateStatus(epicId, epic.status);
    }

    public Object getTaskById(Integer id) {
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

    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasksList = new ArrayList<>();

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

        for (Integer subtaskId : epicList.get(epicId).linkedTasks) {
            subtask = subtaskList.get(subtaskId);
            subtasks.add(subtask);
        }

        return subtasks;
    }


    public void deleteTaskById(Integer id) {
        if (epicList.containsKey(id)) {
            epicList.remove(id);
            for (Integer subtaskId : epicList.get(id).linkedTasks) {
                subtaskList.remove(subtaskId);
            }
        } else if (subtaskList.containsKey(id)) {
            for (Integer epicId : epicList.keySet()) {
                for (Integer subtaskId : epicList.get(epicId).linkedTasks) {
                    if (subtaskId.equals(id)) {
                        epicList.get(epicId).linkedTasks.remove(subtaskId);
                        statusManager.updateStatus(id, Status.NEW);
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

    public int generateUid(String projectCode) {
        projectCounters.putIfAbsent(projectCode, 0);
        uid = projectCounters.get(projectCode) + 1;
        projectCounters.put(projectCode, uid);
        return uid;
    }
}
