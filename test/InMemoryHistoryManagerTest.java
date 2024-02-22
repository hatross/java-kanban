import entities.Status;
import entities.Task;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void shouldStorePreviousTaskVersion() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);

        taskManager.getTaskById(taskId);
        task = new Task(taskId, "Первая задача в проекте TASK", "Новое описание!", Status.NEW);
        taskManager.updateTask(task);

        taskManager.getTaskById(taskId);

        assertNotEquals(taskManager.getHistory().get(0), taskManager.getHistory().get(1));
    }

}