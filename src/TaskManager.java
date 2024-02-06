import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private Integer uid = 0;

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

        if (subtask.getEpicId() != null && epicList.containsKey(subtask.getEpicId())) {
            ArrayList<Integer> newLinkedTasks = epicList.get(subtask.getEpicId()).getLinkedTasks();
            newLinkedTasks.add(uid);
            updateEpicStatus(subtask.getEpicId());
        }

    }

    public void updateTask(Task task) {
        taskList.put(task.getUid(), task);
    }
    public void updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = subtaskList.get(subtask.getUid());

        if (epicList.containsKey(updatedSubtask.getEpicId()) && updatedSubtask.getEpicId() != null) {
            if (!updatedSubtask.getEpicId().equals(subtask.getEpicId())) {
                Epic oldEpic = epicList.get(updatedSubtask.getEpicId());

                if (!oldEpic.getLinkedTasks().isEmpty()) {
                    ArrayList <Integer> newLinkedTasks = oldEpic.getLinkedTasks();
                    newLinkedTasks.remove(subtask.getUid());
                    oldEpic.setLinkedTasks(newLinkedTasks);
                    Epic newEpic = epicList.get(subtask.getEpicId());
                    if (newEpic != null) {
                        newLinkedTasks = newEpic.getLinkedTasks();
                        newLinkedTasks.add(subtask.getEpicId());
                    }
                }
            }
        }

        subtaskList.put(subtask.getUid(), subtask);
        updateEpicStatus(subtask.getEpicId());
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
        return taskList.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epicList.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtaskList.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasksList = new ArrayList<>();

        for (Integer taskId : taskList.keySet()) {
            Task task = taskList.get(taskId);
            allTasksList.add(task);
        }

        return allTasksList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpicsList = new ArrayList<>();

        for (Integer epicId : epicList.keySet()) {
            Epic epic = epicList.get(epicId);
            allEpicsList.add(epic);
        }

        return allEpicsList;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasksList = new ArrayList<>();

        for (Integer subtaskId : subtaskList.keySet()) {
            Subtask subtask = subtaskList.get(subtaskId);
            allSubtasksList.add(subtask);
        }

        return allSubtasksList;
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
        taskList.remove(id);
    }

    public void deleteEpicById(Integer id) {
        for (Integer subtaskId : epicList.get(id).getLinkedTasks()) {
            subtaskList.remove(subtaskId);
        }
        epicList.remove(id);
    }

    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtaskList.get(id);
        epicList.get(subtask.getEpicId()).getLinkedTasks().remove(id);
        updateEpicStatus(subtask.getEpicId());
        subtaskList.remove(id);
    }

    public void deleteAllTasks() {
        epicList.clear();
        subtaskList.clear();
        taskList.clear();
    }

    private void updateEpicStatus(Integer epicId) {
        Epic epic = epicList.get(epicId);
        if (isDoneEpic(epicId)) {
            epic.setStatus(Status.DONE);
        } else if (isNewEpic(epicId)) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
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
        Epic epic = epicList.get(epicId);
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
