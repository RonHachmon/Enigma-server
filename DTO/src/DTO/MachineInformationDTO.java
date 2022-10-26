package DTO;


import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;

import java.util.List;

public class MachineInformationDTO {
    private final String encryptedMessage;




    private final int amountOfRotors;
    private final int availableReflectors;
    private final String availableChars;
    private final int amountOfRotorsRequired;
    private final int reflectorID;


    //currently in user rotors
    private final int[] startingRotors;
    public MachineInformationDTO(MachineManager machineManager,String encryptedMessage) {
        List<Integer> currentRotorsList = machineManager.getCurrentRotorsList();
        startingRotors=new int[machineManager.getCurrentRotorsList().size()];
        for (int i = 0; i <currentRotorsList.size() ; i++) {
            startingRotors[i]=currentRotorsList.get(i);
        }
        reflectorID=machineManager.getReflector();

        MachineInformation machineInformation = machineManager.getMachineInformation();
        amountOfRotorsRequired= machineInformation.getAmountOfRotorsRequired();
        availableChars= machineInformation.getAvailableChars();
        availableReflectors= machineInformation.getAvailableReflectors();
        amountOfRotors= machineInformation.getAmountOfRotors();


        this.encryptedMessage=encryptedMessage;
    }


    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public int getAmountOfRotors() {
        return amountOfRotors;
    }

    public int getAvailableReflectors() {
        return availableReflectors;
    }

    public String getAvailableChars() {
        return availableChars;
    }

    public int getAmountOfRotorsRequired() {
        return amountOfRotorsRequired;
    }

    public int getReflectorID() {
        return reflectorID;
    }

    public int[] getStartingRotors() {
        return startingRotors;
    }
}
