package engine.enigma.battlefield.entities;

import DTO.AgentData;
import DTO.MachineInformationDTO;
import DTO.QueueDataDTO;
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
        enigmaTasks.reset();
        enigmaTasks.startProducer(machineInformationDTO,battleFieldInfo);
    }

    public  TaskDataDTO getTask(int amountOfTasks, int taskDone,String agentName)
    {
        TaskDataDTO tasks = this.enigmaTasks.getTasks(amountOfTasks);
        updateAgent(tasks.getCodeSettingDTO().length, taskDone, agentName);
        return tasks;
    }

    private void updateAgent(int amountOfTasks, int taskDone, String agentName) {
        BattleAgent battleAgent = getBattleAgent(agentName);
        if (battleAgent!=null) {
            battleAgent.increaseTaskDone(taskDone);

            battleAgent.increaseTaskPulled(amountOfTasks);
        }
    }

    private BattleAgent getBattleAgent(String agentName) {
        for (int i = 0; i <agentList.size() ; i++) {
            if(agentList.get(i).getAgentName().equals(agentName))
            {
                return agentList.get(i);
            }
        }
        return null;
    }

    public void updateAmountOfCandidateFound(String agentName, int candidateFound) {
        BattleAgent battleAgent = getBattleAgent(agentName);
        battleAgent.setCandidatesFound(battleAgent.getCandidatesFound()+candidateFound);
    }

    public void resetAgents() {
        this.agentList.forEach(battleAgent -> battleAgent.resetTaskData());
    }

    public QueueDataDTO getQueueData() {
        return this.enigmaTasks.getQueueData();
    }
}
