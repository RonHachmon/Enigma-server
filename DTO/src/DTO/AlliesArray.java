package DTO;

import engine.enigma.battlefield.BattleField;
import engine.enigma.battlefield.BattleFieldInfo;

import java.util.List;

public class AlliesArray {
    private final String[] allies;

    public AlliesArray(List<String> dynamicAllies) {
        allies=new String[dynamicAllies.size()];
        for (int i = 0; i <dynamicAllies.size() ; i++) {
            allies[i]=dynamicAllies.get(i);
        }
    }

    public String[] getAllies() {
        return allies;
    }

}
