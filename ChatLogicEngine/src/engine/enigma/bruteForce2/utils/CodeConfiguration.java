package engine.enigma.bruteForce2.utils;

import java.util.ArrayList;
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
