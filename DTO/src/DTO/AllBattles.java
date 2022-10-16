package DTO;

import engine.enigma.battlefield.BattleField;

import engine.enigma.battlefield.BattleFieldInfo;

import java.util.List;

public class AllBattles {
    private final BattleFieldInfo[] battleFieldInfos;

    public AllBattles(List<BattleField> dynamicBattleFields) {
        battleFieldInfos=new BattleFieldInfo[dynamicBattleFields.size()];
        for (int i = 0; i <dynamicBattleFields.size() ; i++) {
            battleFieldInfos[i]=dynamicBattleFields.get(i).getBattleFieldInfo();
        }
    }

    public BattleFieldInfo[] getBattleFields() {
        return battleFieldInfos;
    }
}
