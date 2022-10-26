package DTO;

import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.bruteForce2.utils.DifficultyLevel;

public class BattleFieldInfoDTO {

    private DifficultyLevel level;

    private String battleName;

    private int requiredAllies;
    private String status;

    private String uboatName;
    private int signedAllies=0;
    private String currentAndRequiredRatio="";


    public BattleFieldInfoDTO(BattleFieldInfo battleFieldInfo) {
        this.level = battleFieldInfo.getLevel();
        this.battleName = battleFieldInfo.getBattleName();
        this.requiredAllies = battleFieldInfo.getRequiredAllies();
        if(battleFieldInfo.getIsStarted())
        {
            this.status = "started";

        }
        else
        {
            this.status = "on hold";
        }

        this.uboatName = battleFieldInfo.getUboatName();
        this.signedAllies = battleFieldInfo.getSignedAllies();
        this.updateSignedRatio();

    }
    private void updateSignedRatio() {
        this.currentAndRequiredRatio=this.signedAllies+"/"+this.requiredAllies;
    }

    public boolean isFull() {
        return this.signedAllies==this.requiredAllies;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public String getBattleName() {
        return battleName;
    }

    public int getRequiredAllies() {
        return requiredAllies;
    }

    public String getStatus() {
        return status;
    }

    public String getUboatName() {
        return uboatName;
    }

    public int getSignedAllies() {
        return signedAllies;
    }

    public String getCurrentAndRequiredRatio() {
        return currentAndRequiredRatio;
    }
}
