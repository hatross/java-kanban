import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void shouldStorePreviousTaskVersion() {
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        int taskId = taskManager.createTask(task);

        taskManager.getTaskById(taskId);
        task = new Task(1, "Первая задача в проекте TASK", "Новое описание!", Status.NEW);
        taskManager.updateTask(task);

        taskManager.getTaskById(taskId);

        assertNotEquals(taskManager.getHistory().get(0), taskManager.getHistory().get(1));
    }

}