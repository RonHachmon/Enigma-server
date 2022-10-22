package engine.enigma.battlefield;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    private BattleFieldInfo battleFieldInfo;
    private List<Ally> allies =new ArrayList<>();

    public BattleFieldInfo getBattleFieldInfo() {
        return battleFieldInfo;
    }

    public void setBattleFieldInfo(BattleFieldInfo battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }

    public List<Ally> getAllies() {
        return allies;
/*        List<String> alliesName=new ArrayList<>();
        System.out.println(allies.size());
        allies.forEach(ally -> alliesName.add(ally.getAllyName()));
        *//*allies.stream().map(ally -> alliesName.add(ally.getAllyName()));*//*
        System.out.println("size of new arr"+alliesName.size());
        return alliesName;*/
    }

    public Ally getAlly(String allyName) {

        for (Ally ally : allies) {
            if (ally.getAllyName().equals(allyName)) {
                return ally ;

            }
        }
        return null;
    }



    public void addAlly(Ally ally) {
        allies.add(ally);
        battleFieldInfo.signAlly();

    }
    public void removeAlly(String username) {
        allies.remove(getAlly(username));
        battleFieldInfo.unSignAlly();

    }
}
