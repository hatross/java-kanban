import entities.Status;
import entities.Task;
import managers.FileBackedTaskManager;
import managers.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    @Test
    public void savingEmptyFile() {
        try {
            File temp = File.createTempFile("taskmanager", ".txt");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(temp);
            fileBackedTaskManager.save();

            BufferedReader br = new BufferedReader(new FileReader(temp));
            String line = br.readLine();

            assertNotNull(line);
        } catch (IOException | ManagerSaveException e) {
            assertTrue(false);
        }
    }

    @Test
    public void loadFromEmptyFile() {
        try {
            File temp = File.createTempFile("taskmanager", ".txt");
            FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(temp);

            assertTrue(fileBackedTaskManager.getAllTasks().isEmpty());
            assertTrue(fileBackedTaskManager.getAllEpics().isEmpty());
            assertTrue(fileBackedTaskManager.getAllSubtasks().isEmpty());
            assertTrue(fileBackedTaskManager.getHistory().isEmpty());
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void savingTasksToFile() {
        try {
            File temp = File.createTempFile("taskmanager", ".txt");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(temp);

            Task task1 = new Task(1, "задача", "описание", Status.NEW);
            Task task2 = new Task(2, "задача2", "описание2", Status.NEW);
            fileBackedTaskManager.createTask(task1);
            fileBackedTaskManager.createTask(task2);

            assertFalse(temp.length() == 0);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}
