package engine.machineutils;



import engine.machineparts.Machine;
import utils.EnigmaUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting implements Serializable {
    public static final String EMPTY = "";
    private String setOfPlugs = EMPTY;
    private List<Integer> selectedRotorsIndexes = new ArrayList<>();
    private String chosenReflector;
    private String initialRotorIndexes = EMPTY;
    private String initialRotorIndexesAndDistanceFromNotch = EMPTY;

    public String getCurrentMachineCode(Machine machine) {
        String code = "<" + RotorsAndDistanceFromNotch() + ">" +
                "<" + rotorsIndexAndDistanceFromNotch(machine) + ">" +
                "<" + chosenReflector + ">" +
                getAddPlugs();

        return code;
    }

    public String getInitialFullMachineCode() {
        String code = "<" + RotorsAndDistanceFromNotch()  + ">" +
                "<" + initialRotorIndexesAndDistanceFromNotch + ">" +
                "<" + chosenReflector + ">" +
                getAddPlugs();

        return code;
    }

    public void resetPlugs() {
        setOfPlugs = EMPTY;
    }

    public String getInitialRotorIndexes() {
        return this.initialRotorIndexes;
    }


    public void setSettingRotators(List<Integer> RotorsID) {
        selectedRotorsIndexes = RotorsID;
    }
    public List<Integer> getCurrentRotorsList() {
        return selectedRotorsIndexes;
    }
    public int getReflector()
    {
        return  (EnigmaUtils.convertRomanToInt(this.chosenReflector)-1);
    }

    //gets full set of characters for example "AO!"
    public void setSettingStartingChar(String startingCharArray,Machine machine) {
        StringBuilder string = new StringBuilder(startingCharArray);
        initialRotorIndexes = string.reverse().toString();
        initialRotorIndexesAndDistanceFromNotch = rotorsIndexAndDistanceFromNotch(machine);
    }

    public void setSettingReflector(int selectedReflector) {
        this.chosenReflector = EnigmaUtils.convertIntToRoman(selectedReflector);
    }

    public void addPlug(Character firstChar, Character secondChar) {
        if (this.setOfPlugs.isEmpty()) {
            this.setOfPlugs += firstChar.toString() + '|' + secondChar.toString();
        } else {
            this.setOfPlugs += ',' + firstChar.toString() + '|' + secondChar.toString();
        }
    }


    private String RotorsAndDistanceFromNotch() {
        String result = EMPTY;
        int i;
        int currentRotorIndex;
        for ( i = selectedRotorsIndexes.size() - 1; i > 0; i--) {
             currentRotorIndex = selectedRotorsIndexes.get(i);
            result += (currentRotorIndex + 1) +",";
        }
        currentRotorIndex = selectedRotorsIndexes.get(i);
        result += (currentRotorIndex + 1);
        return result;
    }
    private String rotorsIndexAndDistanceFromNotch(Machine machine) {
        String result = EMPTY;
        for (int i = selectedRotorsIndexes.size() - 1; i >= 0; i--) {
            int currentRotorIndex = selectedRotorsIndexes.get(i);
            result += machine.getAllRotors().get(currentRotorIndex).currentStartingChar() +
                    "(" + machine.getAllRotors().get(currentRotorIndex).distanceFromNotch() + ")";
        }
        return result;
    }

    private String getAddPlugs() {
        if (setOfPlugs.isEmpty()) {
            return EMPTY;
        }
        return "<" + setOfPlugs + ">";
    }
    public Setting clone() throws CloneNotSupportedException {
        Setting clonedSetting = new Setting();
        clonedSetting.selectedRotorsIndexes = new ArrayList<>(selectedRotorsIndexes);
        clonedSetting.setOfPlugs = setOfPlugs;
        clonedSetting.chosenReflector = this.chosenReflector;
        clonedSetting.initialRotorIndexes = initialRotorIndexes;
        clonedSetting.initialRotorIndexesAndDistanceFromNotch=initialRotorIndexesAndDistanceFromNotch;
        return clonedSetting;

    }

}
