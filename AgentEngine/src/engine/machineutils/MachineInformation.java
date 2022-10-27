package engine.machineutils;



import engine.machineparts.Machine;

import java.io.Serializable;

public class MachineInformation implements Serializable {
    private final int availableReflectors;
    private final int amountOfRotorsRequired;
    private final int amountOfRotors;
    private final String availableChars;

    public MachineInformation(Machine machine) {
        this.availableReflectors = machine.getAmountOfAvailableReflectors();
        this.amountOfRotorsRequired = machine.getAmountOfRotorNeeded();
        this.amountOfRotors = machine.getAllRotors().size();
        this.availableChars = machine.getAllChars();
    }

    public int getAvailableReflectors() {
        return availableReflectors;
    }

    public int getAmountOfRotorsRequired() {
        return amountOfRotorsRequired;
    }

    public int getAmountOfRotors() {
        return amountOfRotors;
    }

    public String getAvailableChars() {
        return availableChars;
    }

    public MachineInformation clone(Machine machine){
        return  new MachineInformation(machine);
    }
}
