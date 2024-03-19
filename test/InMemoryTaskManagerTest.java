import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    @Test
    public void addAllTypesTasks() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Задача", "Описание", Status.NEW);
        Epic epic = new Epic("Эпик", "Описание", null);

        int taskUid = manager.createTask(task);
        Task savedTask = manager.getTaskById(taskUid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        int epicUid = manager.createEpic(epic);
        Task savedEpic = manager.getEpicById(epicUid);
        assertNotNull(savedEpic, "Эпик не найден;");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        Subtask subtask = new Subtask("Подзадача", "Описание", Status.NEW, epicUid);
        int subtaskUid = manager.createSubtask(subtask);
        Task savedSubtask = manager.getSubtaskById(subtaskUid);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        ArrayList<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        ArrayList<Epic> epics = manager.getAllEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void shouldNotAssignCustomId() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task(123, "Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = manager.createTask(task);

        assertNotEquals(123, taskId);
    }
}