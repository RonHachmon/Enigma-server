package engine.enigma.machineutils;

import java.io.Serializable;

public class StatisticInput implements Serializable {
    private int codeIndex;
    private String processedInput;

    public int getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(int codeIndex) {
        this.codeIndex = codeIndex;
    }

    public String getProcessedInput() {
        return processedInput;
    }

    public void setProcessedInput(String processedInput) {
        this.processedInput = processedInput;
    }
}
