package DTO;

import engine.enigma.battlefield.entities.Ally;

public class AllyDTO {
    private String allyName;
    private boolean isReady;
    private int numberOfAgents;
    private Integer taskSize=null;

    public AllyDTO(Ally ally) {
        this.allyName = ally.getAllyName();
        this.isReady=ally.isReady();
        this.numberOfAgents=ally.getAgentList().size();
        this.taskSize=ally.getEnigmaTasks().getTaskSize();
    }

    public String getAllyName() {
        return allyName;
    }


    public boolean isReady() {
        return isReady;
    }



    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public Integer getTaskSize() {
        return taskSize;
    }
}
