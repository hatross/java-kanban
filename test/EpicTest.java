import entities.Epic;
import entities.Status;
import entities.Subtask;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void shouldBeFalseIfSelfLinkingEpic() {
        TaskManager manager = Managers.getDefault();
        ArrayList<Integer> linkedTasks = new ArrayList<>();

        Epic epic = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        int epicId = manager.createEpic(epic);

        linkedTasks.add(epicId);

        epic = new Epic(epicId, "Эпик с одной подзадачей", "Какой-то текст!", linkedTasks);
        manager.updateEpic(epic);

        assertNotEquals(epic.getLinkedTasks(), manager.getEpicById(epicId).getLinkedTasks());
    }

    @Test
    public void shouldNotContainDeletedSubtasks() {
        TaskManager taskManager = Managers.getDefault();

        Epic epic = new Epic("Эпик", "Какой-то текст!",null);
        int epicId = taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        int subtaskId1 = taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Вторая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, epicId);
        taskManager.createSubtask(subtask2);

        taskManager.deleteSubtaskById(subtaskId1);

        assertTrue(!taskManager.getEpicById(epicId).getLinkedTasks().contains(subtaskId1));
    }
}