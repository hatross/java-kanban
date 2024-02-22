package managers;

import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private Integer uid = 0;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Integer createTask(Task task) {
        task.setUid(++uid);
        taskList.put(uid, task);
        return uid;
    }

    @Override
    public Integer createEpic(Epic epic) {
        epic.setUid(++uid);
        epicList.put(uid, epic);
        return uid;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        subtask.setUid(++uid);
        subtaskList.put(uid, subtask);

        if (subtask.getEpicId() != null && epicList.containsKey(subtask.getEpicId())) {
            ArrayList<Integer> newLinkedTasks = epicList.get(subtask.getEpicId()).getLinkedTasks();
            newLinkedTasks.add(uid);
            updateEpicStatus(subtask.getEpicId());
        }

        return uid;
    }


    @Override
    public void updateTask(Task task) {
        taskList.put(task.getUid(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = subtaskList.get(subtask.getUid());

        if (epicList.containsKey(subtask.getEpicId()) && updatedSubtask.getEpicId() != null) {
            if (!updatedSubtask.getEpicId().equals(subtask.getEpicId())) {
                Epic oldEpic = epicList.get(updatedSubtask.getEpicId());

                if (!oldEpic.getLinkedTasks().isEmpty()) {
                    ArrayList<Integer> newLinkedTasks = oldEpic.getLinkedTasks();
                    newLinkedTasks.remove(subtask.getUid());
                    oldEpic.setLinkedTasks(newLinkedTasks);
                    Epic newEpic = epicList.get(subtask.getEpicId());
                    if (newEpic != null) {
                        newLinkedTasks = newEpic.getLinkedTasks();
                        newLinkedTasks.add(subtask.getEpicId());
                    }
                }
            }
            subtaskList.put(subtask.getUid(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        ArrayList<Integer> storedLinkedTasks = epicList.get(epic.getUid()).getLinkedTasks();
        ArrayList<Integer> newLinkedTasks = epic.getLinkedTasks();

        if (newLinkedTasks == null && storedLinkedTasks != null) {
            Epic updatedEpic = epicList.get(epic.getUid());
            updatedEpic.setUid(epic.getUid());
            updatedEpic.setSummary(epic.getSummary());
        } else {
            boolean isAllSubtasks;
            int subtaskCount = 0;
            for (Integer subtaskId : newLinkedTasks) {
                if (subtaskList.containsKey(subtaskId)) {
                    subtaskCount++;
                }
            }
            isAllSubtasks = subtaskCount == newLinkedTasks.size();

            if (isAllSubtasks) {
                epicList.put(epic.getUid(), epic);
            }
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        if (taskList.get(id) != null) {
            historyManager.add(taskList.get(id));
        }
        return taskList.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (epicList.get(id) != null) {
            historyManager.add(epicList.get(id));
        }
        return epicList.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (subtaskList.get(id) != null) {
            historyManager.add(subtaskList.get(id));
        }
        return subtaskList.get(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasksList = new ArrayList<>();

        for (Integer taskId : taskList.keySet()) {
            Task task = taskList.get(taskId);
            allTasksList.add(task);
        }

        return allTasksList;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpicsList = new ArrayList<>();

        for (Integer epicId : epicList.keySet()) {
            Epic epic = epicList.get(epicId);
            allEpicsList.add(epic);
        }

        return allEpicsList;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasksList = new ArrayList<>();

        for (Integer subtaskId : subtaskList.keySet()) {
            Subtask subtask = subtaskList.get(subtaskId);
            allSubtasksList.add(subtask);
        }

        return allSubtasksList;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Integer epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        Subtask subtask;

        for (Integer subtaskId : epicList.get(epicId).getLinkedTasks()) {
            subtask = subtaskList.get(subtaskId);
            subtasks.add(subtask);
        }

        return subtasks;
    }


    @Override
    public void deleteTaskById(Integer id) {
        taskList.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        for (Integer subtaskId : epicList.get(id).getLinkedTasks()) {
            subtaskList.remove(subtaskId);
        }
        epicList.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtaskList.get(id);
        epicList.get(subtask.getEpicId()).getLinkedTasks().remove(id);
        updateEpicStatus(subtask.getEpicId());
        subtaskList.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        epicList.clear();
        subtaskList.clear();
        taskList.clear();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
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
