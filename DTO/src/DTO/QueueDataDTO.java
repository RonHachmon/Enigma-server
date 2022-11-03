package DTO;

import engine.enigma.battlefield.entities.task.TaskData;

public class QueueDataDTO {
    private final long totalTasks;
    private final   long taskCreated;

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getTaskCreated() {
        return taskCreated;
    }

    public QueueDataDTO(TaskData taskData) {
        this.totalTasks = taskData.getTotalTasks();
        this.taskCreated=taskData.getTaskCreated();
    }
}
