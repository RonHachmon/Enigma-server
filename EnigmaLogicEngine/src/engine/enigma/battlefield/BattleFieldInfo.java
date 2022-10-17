package engine.enigma.battlefield;

import engine.enigma.bruteForce2.utils.DifficultyLevel;
import engine.enigma.jaxb_classes.CTEBattlefield;


public class BattleFieldInfo {
    private DifficultyLevel level;

    private String battleName;

    private int requiredAllies;
    private String status="on hold";

    private String uboatName;
    private int signedAllies=0;
    private String currentAndRequiredRatio="";

    public static BattleFieldInfo createFromXML(CTEBattlefield cteBattlefield) {
        BattleFieldInfo battleField=new BattleFieldInfo();
        battleField.setBattleName(cteBattlefield.getBattleName());
        battleField.setRequiredAllies(cteBattlefield.getAllies());
        battleField.setDifficultyByString(cteBattlefield.getLevel());
        battleField.updateSignedRatio();
        return battleField;

    }

    private  void setDifficultyByString(String level ) {
        switch (level)
        {
            case "Easy":
            {
                this.setLevel(DifficultyLevel.EASY);
                break;
            }
            case "Medium":
            {
                this.setLevel(DifficultyLevel.MEDIUM);
                break;
            }
            case "Hard":
            {
                this.setLevel(DifficultyLevel.HARD);
                break;
            }
            case "Insane":
            {
                this.setLevel(DifficultyLevel.IMPOSSIBLE);
                break;
            }
        }
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public int getRequiredAllies() {
        return requiredAllies;
    }

    public void setRequiredAllies(int requiredAllies) {
        this.requiredAllies = requiredAllies;
    }

    public String getUboatName() {
        return uboatName;
    }

    public void setUboatName(String uboatName) {
        this.uboatName = uboatName;
    }

    public int getSignedAllies() {
        return signedAllies;
    }

    public void setSignedAllies(int signedAllies) {
        this.signedAllies = signedAllies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentAndRequiredRatio() {
        return currentAndRequiredRatio;
    }

    public void setCurrentAndRequiredRatio(String currentAndRequiredRatio) {
        this.currentAndRequiredRatio = currentAndRequiredRatio;
    }
    public void signAlly()
    {
        this.signedAllies=this.getSignedAllies()+1;
        updateSignedRatio();
    }
    public void unSignAlly()
    {
        this.signedAllies=this.getSignedAllies()-1;
        updateSignedRatio();
    }

    private void updateSignedRatio() {
        this.currentAndRequiredRatio=this.signedAllies+"/"+this.requiredAllies;
    }

    public boolean isFull() {
        return this.signedAllies==this.requiredAllies;
    }
}
