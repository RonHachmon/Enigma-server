package engine.enigma.battlefield;


import engine.enigma.jaxb_classes.CTEBattlefield;
import utils.DifficultyLevel;


public class BattleFieldInfo {
    private DifficultyLevel level;

    private String battleName;

    private int requiredAllies;


    private String uboatName;
    private int signedAllies=0;

    private boolean isStarted=false;
    private boolean isEnded=false;

    public static BattleFieldInfo createFromXML(CTEBattlefield cteBattlefield) {
        BattleFieldInfo battleField=new BattleFieldInfo();
        battleField.setBattleName(cteBattlefield.getBattleName());
        battleField.setRequiredAllies(cteBattlefield.getAllies());
        battleField.setDifficultyByString(cteBattlefield.getLevel());
        return battleField;

    }
    public void setEnded(boolean isEnded)
    {
        this.isEnded=isEnded;
    }
    public boolean isEnded()
    {
        return this.isEnded;
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
    public boolean getIsStarted() {
        return isStarted;
    }

    public void setSignedAllies(int signedAllies) {
        this.signedAllies = signedAllies;
    }

    public void signAlly()
    {
        this.signedAllies=this.getSignedAllies()+1;
    }
    public void unSignAlly()
    {
        this.signedAllies=this.getSignedAllies()-1;
    }



    public boolean isFull() {
        return this.signedAllies==this.requiredAllies;
    }

    public void startBattle( ) {
        isStarted=true;
    }
}
