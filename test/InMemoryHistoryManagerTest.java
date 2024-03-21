import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void shouldNotStorePreviousTaskVersion() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);

        taskManager.getTaskById(taskId);
        task = new Task(taskId, "Первая задача в проекте TASK", "Новое описание!", Status.NEW);
        taskManager.updateTask(task);

        taskManager.getTaskById(taskId);

        int historySize = taskManager.getHistory().size();

        assertEquals(1, historySize);
    }

    @Test
    public void addingTasksToHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);
        Epic epic = new Epic("Эпик", "Какой-то текст!",null);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void removingTasksFromHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);

        taskManager.getTaskById(taskId);
        taskManager.deleteTaskById(taskId);

        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void removingEpicsFromHistory() {
        TaskManager taskManager = Managers.getDefault();

        Epic epic = new Epic("Эпик", "Какой-то текст!",null);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        taskManager.deleteEpicById(epicId);
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void removingSubtasksFromHistory() {
        TaskManager taskManager = Managers.getDefault();

        Epic epic = new Epic("Эпик", "Какой-то текст!",null);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);
        taskManager.deleteSubtaskById(subtaskId);

        assertEquals(1, taskManager.getHistory().size());
        assertTrue(taskManager.getEpicById(epicId).getLinkedTasks().isEmpty());
    }

    @Test
    public void clearingHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);
        Epic epic = new Epic("Эпик", "Какой-то текст!",null);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtaskId);

        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getHistory().size());
    }
}