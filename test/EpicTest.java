import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void shouldBeFalseIfSelfLinkingEpic() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        boolean result;
        ArrayList<Integer> linkedTasks = new ArrayList<>();
        linkedTasks.add(1);

        Epic epic = new Epic("Эпик с одной подзадачей", "Какой-то текст!",null);
        manager.createEpic(epic);

        epic = new Epic(1, "Эпик с одной подзадачей", "Какой-то текст!", linkedTasks);
        manager.updateEpic(epic);

        result = manager.getEpicById(1).getLinkedTasks().contains(epic.getUid());
        assertFalse(result);
    }
}