package DTO;


import engine.enigma.bruteForce2.utils.CodeConfiguration;

import java.util.List;

public class CodeSettingDTO {


    private final String charIndexes;
    private final int[] rotorsID;
    private final int reflectorID;



    public CodeSettingDTO(CodeConfiguration codeConfiguration) {
        this.charIndexes = codeConfiguration.getCharIndexes();
        this.rotorsID = new int[codeConfiguration.getRotorsID().size()];
        for (int i =0;i<codeConfiguration.getRotorsID().size();i++)
        {
            rotorsID[i]=codeConfiguration.getRotorsID().get(i);
        }
        this.reflectorID = codeConfiguration.getReflectorID();

    }

    public String getCharIndexes() {
        return charIndexes;
    }

    public int[] getRotorsID() {
        return rotorsID;
    }

    public int getReflectorID() {
        return reflectorID;
    }
}
