import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldBeNotNullIfInitializedInMemoryTaskManager() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        assertNotNull(manager);
    }

    @Test
    public void shouldBeNotNullIfInitializedInMemoryHistoryManager() {
        HistoryManager manager = Managers.getDefaultHistory();
        assertNotNull(manager);
    }

}