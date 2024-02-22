import entities.Epic;
import entities.Status;
import entities.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void shouldBeEqualIfSameIdTasks() {
        Task task1 = new Task(1, "", "", Status.NEW);
        Task task2 = new Task(1, "", "", Status.NEW);
        assertEquals(task1, task2);
    }

    @Test
    public void shouldBeEqualIfSameIdTaskInheritors() {
        Task epic1 = new Epic(1, "", "",null);
        Task epic2 = new Epic(1, "", "",null);
        assertEquals(epic1, epic2);
    }
}