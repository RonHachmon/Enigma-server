package engine.bruteForce2.utils;

public class QueueData {
    private int taskPulled=0;
    private int taskDone=0;
    private int taskLeft=0;


    public void increaseTaskPulled(int taskPulled)
    {
        this.taskPulled+=taskPulled;
    }
    public synchronized void increaseTaskDone(int taskDone)
    {
        this.taskDone+=taskDone;
    }
    public synchronized void increaseTaskLeft(int taskLeft)
    {
        this.taskLeft+=taskLeft;
    }

    public int getTaskPulled() {
        return taskPulled;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public int getTaskLeft() {
        return taskLeft;
    }
}
