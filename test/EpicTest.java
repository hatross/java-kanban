import entities.Epic;
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
}