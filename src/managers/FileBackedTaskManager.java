package managers;

import entities.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Integer createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return task.getUid();
    }

    @Override
    public Integer createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return epic.getUid();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return subtask.getUid();
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                bw.append(toString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                bw.append(toString(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtasks()) {
                bw.append(toString(subtask) + "\n");
            }

            bw.append("\n" + "Просмотрено: " + "\n");
            bw.append(historyToString(getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (br.ready()) {
            String currentLine = br.readLine();
            if (currentLine.isEmpty()) {
                break;
            }
            String[] split = currentLine.split(",");
            switch (split[1]) {
                case "TASK":
                    fileBackedTaskManager.createTask(fileBackedTaskManager.taskFromString(currentLine));
                    break;
                case "EPIC":
                    fileBackedTaskManager.createEpic(fileBackedTaskManager.epicFromString(currentLine));
                    break;
                case "SUBTASK":
                    fileBackedTaskManager.createSubtask(fileBackedTaskManager.subtaskFromString(currentLine));
                    break;
            }
        }
        br.close();
        return fileBackedTaskManager;
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getUid() + ", ");
        }
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] split = value.split(", ");
        List<Integer> history = new ArrayList<>();
        for (String s : split) {
            history.add(Integer.valueOf(s));
        }
        return history;
    }

    String toString(Task task) {
        return String.join(",", task.getUid().toString(), Type.TASK.toString(), task.getSummary(),
                task.getStatus().toString(), task.getDescription());
    }

    String toString(Epic epic) {
        return String.join(",", epic.getUid().toString(), Type.EPIC.toString(), epic.getSummary(),
                epic.getStatus().toString(), epic.getDescription());
    }

    String toString(Subtask subtask) {
        return String.join(",", subtask.getUid().toString(), Type.SUBTASK.toString(), subtask.getSummary(),
                subtask.getStatus().toString(), subtask.getDescription(), subtask.getEpicId().toString());
    }

    public Task taskFromString(String value) {
        String[] split = value.split(",");
        int uid = Integer.valueOf(split[0]);
        String summary = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        return new Task(uid, summary, description, status);
    }

    public Epic epicFromString(String value) {
        String[] split = value.split(",");
        int uid = Integer.valueOf(split[0]);
        String summary = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        return new Epic(uid, summary, description, status);
    }

    public Subtask subtaskFromString(String value) {
        String[] split = value.split(",");
        int uid = Integer.valueOf(split[0]);
        String summary = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        Integer epicId = Integer.valueOf(split[5]);
        return new Subtask(uid, summary, description, status, epicId);
    }
}
