package engine.enigma.battlefield.entities;

import DTO.AgentData;

public class BattleAgent {

    private String agentName;
    private int taskSize;
    private int threads;
    private int candidatesFound=0;
    private int taskPulled=0;
    private int taskDone=0;
    public void resetTaskData()
    {
        this.taskDone=0;
        this.candidatesFound=0;
        this.taskPulled=0;
    }

    public int getCandidatesFound() {
        return candidatesFound;
    }

    public void setCandidatesFound(int candidatesFound) {
        this.candidatesFound = candidatesFound;
    }

    public int getTaskPulled() {
        return taskPulled;
    }

    public synchronized void increaseTaskPulled(int taskPulled) {

        this.taskPulled += taskPulled;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public synchronized void increaseTaskDone(int taskDone) {
        this.taskDone += taskDone;
    }

    public BattleAgent(AgentData agentData) {
        this.setAgentName(agentData.getAgentName());
        this.setTaskSize(agentData.getTaskSize());
        this.setThreads(agentData.getNumberOfThreads());
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
