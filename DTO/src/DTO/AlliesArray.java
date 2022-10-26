package DTO;

import engine.enigma.battlefield.entities.Ally;

import java.util.List;

public class AlliesArray {
    private final AllyDTO[] allies;

    public AlliesArray(List<Ally> dynamicAllies) {
        allies=new AllyDTO[dynamicAllies.size()];
        for (int i = 0; i <dynamicAllies.size() ; i++) {
            allies[i]=new AllyDTO(dynamicAllies.get(i));
        }
    }

    public AllyDTO[] getAllies() {

        return allies;
    }

    public String[] getAlliesNames() {
         String[] alliesNames=new String[allies.length];
        for (int i = 0; i <alliesNames.length ; i++) {
            alliesNames[i]=(allies[i].getAllyName());
        }
        return alliesNames;
    }

}
