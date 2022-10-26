package DTO;

import engine.enigma.battlefield.entities.BattleAgent;

public class AgentData {
    private String agentAlly;
    private int numberOfThreads;
    private int taskSize;
    private String agentName;

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
}
