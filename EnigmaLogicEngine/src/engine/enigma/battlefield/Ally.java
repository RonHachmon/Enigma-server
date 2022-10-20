package engine.enigma.battlefield;

import DTO.AgentData;
import engine.enigma.bruteForce2.Agent;

import java.util.ArrayList;
import java.util.List;

public class Ally {
    private String allyName;
    private List<BattleAgent> agentList=new ArrayList<>();
    private boolean isReady=false;

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public List<BattleAgent> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<BattleAgent> agentList) {
        this.agentList = agentList;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void addAgent(AgentData agentData) {
        BattleAgent battleAgent=new BattleAgent(agentData);
        this.agentList.add(battleAgent);
    }
}
