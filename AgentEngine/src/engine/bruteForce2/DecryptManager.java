package engine.bruteForce2;

import DTO.AgentData;
import DTO.DMData;
import engine.bruteForce2.utils.CandidateList;
import engine.bruteForce2.utils.Dictionary;
import engine.jaxb_classes.CTEDictionary;
import engine.jaxb_classes.CTEEnigma;
import engine.machineutils.JAXBClassGenerator;
import engine.machineutils.MachineManager;
import utils.ObjectCloner;


import javax.xml.bind.JAXBException;


public class DecryptManager {


    private MachineManager machineManager;
    private DMData dmData;
    private AgentData agentData;
    private TaskManger tasksManager;
    private Dictionary dictionary = new Dictionary();


    public DecryptManager(MachineManager machineManager,DMData dmData,AgentData agentData) {
        this.agentData=agentData;
        this.machineManager = machineManager;
        this.dmData = dmData;
        this.loadDictionary();
    }



    public void stop() {
  /*      TaskManger.resetStaticMembers();*/
        if (tasksManager != null) {
            tasksManager.stop();
        }

    }


    public void startDeciphering() throws Exception{

        tasksManager = new TaskManger(dictionary,dmData,machineManager,(stop) -> System.out.println("ff"),agentData);
        tasksManager.start();
    }



    private void loadDictionary() {
        try {
            dictionary= (Dictionary) ObjectCloner.deepCopy(machineManager.getDictionary());
            machineManager.setDictionary(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public CandidateList getCandidateList() {
        return tasksManager.getCandidateList();
    }

    public int getSizeOfCandidateList() {
        return tasksManager.getCandidateList().getSize();
    }

    public long getTotalTaskSize()
    {
        return tasksManager.getTotalWork();
    }
    public long getTotalTaskDurationInNanoSeconds(){
        return tasksManager.getTotalTaskDurationInMilliSeconds();
    }
    public long getAvgTaskDuration() {
        return tasksManager.getAvgTaskDuration();
    }

    public long getWorkDone() {
        return tasksManager.getWorkDone();
    }


}