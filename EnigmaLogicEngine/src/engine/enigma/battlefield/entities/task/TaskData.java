package engine.enigma.battlefield.entities.task;

public class TaskData {


    private  Integer taskSize=null;
    private  long taskCreated;
    private  long taskPulled;
    private long totalCombinations;

    public Integer getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }


    public long getTaskCreated() {
        return taskCreated;
    }

    public void setTaskCreated(int taskCreated) {
        this.taskCreated = taskCreated;
    }

    public long getTaskPulled() {
        return taskPulled;
    }

    public void setTaskPulled(int taskPulled) {
        this.taskPulled = taskPulled;
    }

    public long getTotalCombinations() {
        return totalCombinations;
    }

    public void setTotalCombinations(long totalCombinations) {
        this.totalCombinations = totalCombinations;
    }
    public void increasedTaskCreated() {
        this.taskCreated++;
    }
    public void increasedTaskPulled() {
        this.taskPulled++;
    }
}
