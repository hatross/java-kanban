import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void shouldBeEqualIfSameIdTasks() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        Task task1 = new Task("Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        manager.createTask(task1);
        task1 = manager.getTaskById(1);
        Task task2 = manager.getTaskById(1);
        assertEquals(task1, task2);
    }

    @Test
    public void shouldBeEqualIfSameIdTaskInheritors() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        Task epic1 = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        manager.createTask(epic1);
        Task epic2 = manager.getTaskById(1);
        assertEquals(epic1, epic2);
    }
}