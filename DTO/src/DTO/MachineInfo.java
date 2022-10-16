package DTO;

public class MachineInfo {
    private final int availableReflectors;
    private final int amountOfRotorsRequired;
    private final int amountOfRotors;
    private final String availableChars;

    public MachineInfo(int availableReflectors, int amountOfRotorsRequired, int amountOfRotors, String availableChars) {
        this.availableReflectors = availableReflectors;
        this.amountOfRotorsRequired = amountOfRotorsRequired;
        this.amountOfRotors = amountOfRotors;
        this.availableChars = availableChars;
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
}
