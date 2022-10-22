package engine.enigma.battlefield;

import DTO.AgentData;
import DTO.AllBattles;
import DTO.AlliesArray;
import DTO.DecryptionCandidate;
import engine.enigma.jaxb_classes.CTEBattlefield;

import java.io.InputStream;
import java.util.*;

public class BattlesManager {


   private static BattlesManager single_instance = null;
   private final LinkedList<BattleField> battleLinkedList=new LinkedList<>();
   private final Map<BattleField, List<DecryptionCandidate>> mapToDecryptionCandidates=new HashMap<>();
   private final Map<BattleField, List<InputStream>> mapToMachineFile=new HashMap<>();
   private final Map<String, Ally> allAllies=new HashMap<>();

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
         Ally ally = allAllies.get(username);
         if(ally!=null) {
            BattleField battleField = getBattleField(battleShip);
            if (battleField != null) {
               battleField.addAlly(ally);
               return true;
            }
         }
      }
      return false;
   }
   public void joinAgentToBattle( AgentData agentData) {
      Ally ally = allAllies.get(agentData.getAgentAlly());
      ally.addAgent(agentData);
   }

   public synchronized boolean addAlly( String username) {
      if(username!=null&&!username.isEmpty()) {
         Ally ally =new Ally();
         ally.setAllyName(username);
         allAllies.computeIfAbsent(username,k->ally);
      }
      return false;
   }
   public List<Ally> getAllies(String battleShip) {
      BattleField battleField = getBattleField(battleShip);
      if(battleField!=null)
      {
         return battleField.getAllies();
      }
      return null;

   }

   public AgentData[] getAgents(String allyName)
   {
      Ally ally = allAllies.get(allyName);
      List<BattleAgent> agentList = ally.getAgentList();
      AgentData[] agentData=new AgentData[ally.getAgentList().size()];
      for (int i = 0; i <agentData.length ; i++) {
         agentData[i]=new AgentData (agentList.get(i),allyName);
      }
      return agentData;
   }

    public synchronized void removeBoat(String battleShip, String username) {
       if(username!=null&&!username.isEmpty()) {
          BattleField battleField = getBattleField(battleShip);
          if(battleField!=null)
          {
             this.battleLinkedList.removeFirstOccurrence(battleField);
          }
       }
    }
   public synchronized void removeAlly(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleField(battleShip);
         battleField.removeAlly(username);
      }
   }

   public AllBattles convertToArray() {

      return new AllBattles(battleLinkedList);

   }

   public AlliesArray getAllAllies() {

      allAllies.size();

    return new AlliesArray(new ArrayList<>(allAllies.values()));
   }


   public void readyAlly(String username) {
      Ally ally = this.allAllies.get(username);
      if(ally!=null)
      {
         System.out.println("ready :D");
         ally.setReady(true);
      }
   }
}
