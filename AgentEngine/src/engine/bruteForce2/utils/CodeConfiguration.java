package engine.bruteForce2.utils;

import DTO.CodeSettingDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeConfiguration {

    private String charIndexes;
    private List<Integer> rotorsID;
    private int reflectorID;

    public CodeConfiguration(String charIndexes, List<Integer> rotorsID, int reflectorID) {
        this.charIndexes = charIndexes;
        this.rotorsID = rotorsID;
        this.reflectorID = reflectorID;
    }
    public CodeConfiguration(CodeSettingDTO codeSettingDTO) {
        this.charIndexes = codeSettingDTO.getCharIndexes();
        rotorsID=new ArrayList<>();
        for (int i = 0; i <codeSettingDTO.getRotorsID().length ; i++) {
            rotorsID.add(codeSettingDTO.getRotorsID()[i]);
        }
        this.reflectorID = codeSettingDTO.getReflectorID();
    }
    public CodeConfiguration clone(CodeConfiguration codeConfiguration)
    {
        CodeConfiguration clonedCode=new CodeConfiguration(codeConfiguration.getCharIndexes(),
                                                            new ArrayList<>(codeConfiguration.getRotorsID()),
                                                                codeConfiguration.getReflectorID());
        return clonedCode;
    }


    public String getCharIndexes() {
        return charIndexes;
    }

    public void setCharIndexes(String charIndexes) {
        this.charIndexes = charIndexes;
    }

    public List<Integer> getRotorsID() {
        return rotorsID;
    }

    public void setRotorsID(List<Integer> rotorsID) {
        this.rotorsID = rotorsID;
    }

    public int getReflectorID() {
        return reflectorID;
    }

    public void setReflectorID(int reflectorID) {
        this.reflectorID = reflectorID;
    }
}
