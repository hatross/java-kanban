import entities.Status;
import entities.Task;
import managers.FileBackedTaskManager;
import managers.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

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

    @Test
    public void updateTask() {
        try {
            File temp = File.createTempFile("taskmanager", ".txt");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(temp);

            Task task1 = new Task(1, "задача", "описание", Status.NEW);
            fileBackedTaskManager.createTask(task1);
            long preUpdateLength = temp.length();
            task1 = new Task(1, "другое имя", "другое описание", Status.NEW);
            fileBackedTaskManager.updateTask(task1);
            long postUpdateLength = temp.length();

            assertFalse(preUpdateLength == postUpdateLength);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void deleteTask() {
        try {
            File temp = File.createTempFile("taskmanager", ".txt");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(temp);

            Task task1 = new Task(1, "задача", "описание", Status.NEW);
            int taskId = fileBackedTaskManager.createTask(task1);
            long preUpdateLength = temp.length();
            fileBackedTaskManager.deleteTaskById(taskId);
            long postUpdateLength = temp.length();

            assertTrue(preUpdateLength > postUpdateLength);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}
