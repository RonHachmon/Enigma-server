package engine.enigma.battlefield.entities;

import DTO.AgentData;
import DTO.MachineInformationDTO;
import DTO.TaskDataDTO;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.battlefield.entities.task.EnigmaTasks;

import java.util.ArrayList;
import java.util.List;

public class Ally {
    private String allyName;
    private List<BattleAgent> agentList=new ArrayList<>();
    private boolean isReady=false;

    private EnigmaTasks enigmaTasks=new EnigmaTasks();

    public EnigmaTasks getEnigmaTasks() {
        return enigmaTasks;
    }

    public void setEnigmaTasks(Integer enigmaTasks) {
        this.enigmaTasks.setTaskSize(enigmaTasks);
    }



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

    public void startProducer(MachineInformationDTO machineInformationDTO, BattleFieldInfo battleFieldInfo) {
        enigmaTasks.startProducer(machineInformationDTO,battleFieldInfo);
    }
    public  TaskDataDTO getTask(int amountOfTasks)
    {
        return this.enigmaTasks.getTasks(amountOfTasks);
    }
}
