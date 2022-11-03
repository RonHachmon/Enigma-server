package DTO;

import engine.enigma.battlefield.entities.BattleAgent;

public class AgentData {
    private String agentAlly;
    private int numberOfThreads;
    private int taskSize;
    private String agentName;
    private int candidatesFound=0;
    private int taskPulled=0;
    private int taskDone=0;

    public AgentData(String agentAlly, int numberOfThreads, int taskSize, String agentName) {
        this.agentAlly = agentAlly;
        this.numberOfThreads = numberOfThreads;
        this.taskSize = taskSize;
        this.agentName = agentName;
    }

    public AgentData(BattleAgent battleAgent,String allyName) {
        this.agentAlly = allyName;
        this.numberOfThreads = battleAgent.getThreads();
        this.taskSize = battleAgent.getTaskSize();
        this.agentName = battleAgent.getAgentName();
        this.candidatesFound=battleAgent.getCandidatesFound();
        this.taskDone=battleAgent.getTaskDone();
        this.taskPulled=battleAgent.getTaskPulled();
    }

    public String getAgentAlly() {
        return agentAlly;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentAlly(String agentAlly) {
        this.agentAlly = agentAlly;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public void setTaskPulled(int taskPulled) {
        this.taskPulled = taskPulled;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }
}
