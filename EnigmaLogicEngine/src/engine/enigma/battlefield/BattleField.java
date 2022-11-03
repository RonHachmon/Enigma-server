package engine.enigma.battlefield;

import DTO.DecryptionCandidate;
import engine.enigma.battlefield.entities.Ally;
import engine.enigma.battlefield.entities.BattleStatus;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    private BattleFieldInfo battleFieldInfo;
    private List<Ally> allies =new ArrayList<>();

    private String encryptedMessage;
    private BattleStatus battleStatus=BattleStatus.IDLE;
    private String winningTeam;

    public BattleFieldInfo getBattleFieldInfo() {
        return battleFieldInfo;
    }

    public void setBattleFieldInfo(BattleFieldInfo battleFieldInfo) {
        this.battleFieldInfo = battleFieldInfo;
    }

    public List<Ally> getAllies() {
        return allies;

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

    public boolean containsAlly(String allyName) {
        for (Ally ally:this.allies) {
          if(ally.getAllyName().equals(allyName))
          {
              return true;
          }
        }
        return false;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public void setStatus(BattleStatus battleStatus) {
        this.battleStatus =battleStatus;
        if(battleStatus.equals(BattleStatus.DONE)) {
            battleFieldInfo.setEnded(true);
        }
    }

    public BattleStatus getBattleStatus() {
        return battleStatus;
    }


    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    public void clearAllies() {
        allies.forEach(ally ->
        {
            ally.setReady(false);
            ally.setEnigmaTasks(null);
        });

        allies.clear();
    }
}
