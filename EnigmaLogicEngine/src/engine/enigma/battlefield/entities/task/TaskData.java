package engine.enigma.battlefield.entities.task;

public class TaskData {


    private  Integer taskSize=null;
    private  long taskCreated;
    private  long taskPulled;
    private long totalCombinations;
    private long totalTasks;

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
        this.totalTasks =totalCombinations/taskSize.intValue();
        if((totalCombinations%taskSize)!=0)
        {
            totalTasks++;

        }
    }
    public void increasedTaskCreated() {
        this.taskCreated++;
    }
    public void increasedTaskPulled() {
        this.taskPulled++;
    }

    public void reset() {
        this.taskPulled=0;
        this.taskCreated=0;
    }

    public long getTotalTasks() {
        return totalTasks;
    }
}
