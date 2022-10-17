package engine.enigma.battlefield;

import DTO.AllBattles;
import DTO.DecryptionCandidate;
import engine.enigma.jaxb_classes.CTEBattlefield;

import java.io.InputStream;
import java.util.*;

public class BattlesManager {


   private static BattlesManager single_instance = null;
   private final LinkedList<BattleField> battleLinkedList=new LinkedList<>();
   private final Map<BattleField, List<DecryptionCandidate>> mapToDecryptionCandidates=new HashMap<>();
   private final Map<BattleField, List<InputStream>> mapToMachineFile=new HashMap<>();

   private BattlesManager  ()
   {

   }
   public LinkedList<BattleField> getBattleLinkedList() {
      return battleLinkedList;
   }
   public static BattlesManager getInstance()
   {
      if (single_instance == null) {
         single_instance = new BattlesManager();
      }

      return single_instance;
   }

   public synchronized boolean isExist(String battleName)
   {
      for (BattleField battleField:battleLinkedList)
      {
            if(battleField.getBattleFieldInfo().getBattleName().equals(battleName))
            {
               return true;
            }
      }
      return false;
   }
   public BattleFieldInfo createBattle(CTEBattlefield cteBattlefield) {
      if(isExist(cteBattlefield.getBattleName()))
      {
         //throw somthing
      }


      return BattleFieldInfo.createFromXML(cteBattlefield);

   }

   public synchronized void addBattle(BattleFieldInfo battleFieldInfo) {
      BattleField battleField=new BattleField();
      battleField.setBattleFieldInfo(battleFieldInfo);
      this.battleLinkedList.add(battleField);


   }
   public BattleField getBattleField(String battleShip) {

      for (BattleField battleField : battleLinkedList) {
         if (battleField.getBattleFieldInfo().getBattleName().equals(battleShip)) {
            return battleField;

         }
      }
      return null;
   }

   public AllBattles convertToArray() {

      return new AllBattles(battleLinkedList);

   }


    public boolean joinBoatToBattle(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleField(battleShip);
         if(battleField!=null)
         {
            battleField.getBattleFieldInfo().setUboatName(username);
         }
      }
       return false;
    }

   public boolean joinAllyToBattle(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleField(battleShip);
         if(battleField!=null)
         {
            battleField.addAlly(username);
            return true;
         }
      }
      return false;
   }
   public List<String> getAllies(String battleShip) {
      BattleField battleField = getBattleField(battleShip);
      if(battleField!=null)
      {
         return battleField.getAllies();
      }
      return null;

   }

    public void removeBoat(String battleShip, String username) {
       if(username!=null&&!username.isEmpty()) {
          BattleField battleField = getBattleField(battleShip);
          if(battleField!=null)
          {
             this.battleLinkedList.removeFirstOccurrence(battleField);
          }
       }
    }
   public void removeAlly(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleField(battleShip);
         battleField.removeAlly(username);
      }
   }
}
