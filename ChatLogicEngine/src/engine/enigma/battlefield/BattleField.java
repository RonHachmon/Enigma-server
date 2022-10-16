package engine.enigma.battlefield;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    private BattleFieldInfo battleFieldInfo;
    private List<String> Allies=new ArrayList<>();

    public BattleFieldInfo getBattleFieldInfo() {
        return battleFieldInfo;
    }

    public void setBattleFieldInfo(BattleFieldInfo battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }

    public List<String> getAllies() {
        return Allies;
    }

    public void setAllies(List<String> allies) {
        Allies = allies;
    }

    public void addAlly(String username) {
        battleFieldInfo.signAlly();
        Allies.add(username);

    }
    public void removeAlly(String username) {
        battleFieldInfo.unSignAlly();
        Allies.remove(username);

    }
}
