public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(null, "Первая задача в проекте TASK", "Текст описания!", Status.NEW);
        Task task2 = new Task(null, "Вторая задача в проекте TASK", "Текст описания!", Status.NEW);
        Epic epic1 = new Epic(null, "Эпик с двумя подзадачами", "Какой-то текст!", null);
        Epic epic2 = new Epic(null, "Эпик с одной подзадачей", "Какой-то текст!",null);
        Subtask subtask1 = new Subtask(null, "Первая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, 3);
        Subtask subtask2 = new Subtask(null, "Вторая подзадача в EPIC1", "Другой текст!"
                , Status.NEW, 3);
        Subtask subtask3 = new Subtask(null, "Первая подзадача в EPIC2", "Иной текст!"
                , Status.NEW, 4);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        System.out.println(taskManager.getAllTasks());

        subtask2 = new Subtask(5,"Первая подзадача в EPIC1", "Другой текст!"
                , Status.DONE, 3);
        taskManager.updateSubtask(subtask2);
        taskManager.deleteTaskById(1);
        System.out.println(taskManager.getTaskById(7));

        taskManager.deleteAllTasks();
    }
}
