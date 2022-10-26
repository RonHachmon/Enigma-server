package DTO;

import engine.enigma.battlefield.BattleField;

import engine.enigma.battlefield.BattleFieldInfo;

import java.util.List;

public class AllBattles {


    private  BattleFieldInfoDTO[] battleFieldInfoDTOS;

    public AllBattles(List<BattleField> dynamicBattleFields) {
        battleFieldInfoDTOS=new BattleFieldInfoDTO[dynamicBattleFields.size()];
        for (int i = 0; i <dynamicBattleFields.size() ; i++) {
            battleFieldInfoDTOS[i]=new BattleFieldInfoDTO(dynamicBattleFields.get(i).getBattleFieldInfo());
        }
    }

    public BattleFieldInfoDTO[] getBattleFields() {
        return battleFieldInfoDTOS;
    }
}
