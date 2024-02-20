import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    @Test
    public void addAllTypesTasks() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        Task task = new Task("Задача", "Описание", Status.NEW);
        Epic epic = new Epic("Эпик", "Описание", null);
        Subtask subtask = new Subtask("Подзадача", "Описание", Status.NEW, 2);

        int taskUid = manager.createTask(task);
        Task savedTask = manager.getTaskById(taskUid);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        int epicUid = manager.createEpic(epic);
        Task savedEpic = manager.getEpicById(epicUid);
        assertNotNull(savedEpic, "Эпик не найден;");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

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
    public void shouldNotBeEqualWhenCreatingWithCustomUid() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        Task task1 = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        Task task2 = new Task(1, "Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int task1Id = manager.createTask(task1);
        int task2Id = manager.createTask(task2);

        assertNotEquals(manager.getTaskById(task1Id), manager.getTaskById(task2Id), "Задачи совпали.");
    }

}