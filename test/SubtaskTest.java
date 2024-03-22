import entities.Epic;
import entities.Status;
import entities.Subtask;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void shouldNotSelfLinkSubtask() {
        TaskManager manager = Managers.getDefault();

        // подзадача не может быть без эпика, поэтому создаем его
        Epic epic = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = manager.createSubtask(subtask);

        subtask = new Subtask(subtaskId, "Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, subtaskId);
        manager.updateSubtask(subtask);

        assertNotEquals(subtask.getEpicId(), manager.getSubtaskById(subtaskId).getEpicId());
    }

    @Test
    public void shouldRemoveSubtaskWhenRemovingEpic() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId = manager.createSubtask(subtask);

        manager.deleteEpicById(epicId);
        assertNull(manager.getSubtaskById(subtaskId));
    }
}