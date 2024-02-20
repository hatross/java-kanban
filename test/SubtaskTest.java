import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void shouldBeFalseIfSelfLinkingSubtask() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        boolean result;

        // подзадача не может быть без эпика, поэтому создаем его
        Epic epic = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, 1);
        manager.createSubtask(subtask);

        subtask = new Subtask(2, "Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, 2);
        manager.updateSubtask(subtask);

        result = (manager.getSubtaskById(2).getUid().equals(manager.getSubtaskById(2).getEpicId()));
        assertFalse(result);
    }
}