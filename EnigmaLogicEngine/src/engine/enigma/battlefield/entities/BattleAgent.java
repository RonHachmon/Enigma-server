package engine.enigma.battlefield.entities;

import DTO.AgentData;

public class BattleAgent {

    private String agentName;
    private int taskSize;
    private int threads;

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
