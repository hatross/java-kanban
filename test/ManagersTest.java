import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldBeNotNullIfInitializedInMemoryTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    public void shouldBeNotNullIfInitializedInMemoryHistoryManager() {
        HistoryManager manager = Managers.getDefaultHistory();
        assertNotNull(manager);
    }

}