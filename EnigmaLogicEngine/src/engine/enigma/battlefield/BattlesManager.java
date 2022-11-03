package engine.enigma.battlefield;

import DTO.*;
import engine.enigma.battlefield.entities.Ally;
import engine.enigma.battlefield.entities.BattleAgent;
import engine.enigma.battlefield.entities.BattleStatus;
import engine.enigma.jaxb_classes.CTEBattlefield;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class BattlesManager {


   private static BattlesManager single_instance = null;
   private final LinkedList<BattleField> battleLinkedList=new LinkedList<>();
   private final Map<BattleField, List<DecryptionCandidate>> mapToDecryptionCandidates=new HashMap<>();
   private final Map<BattleField, InputStream> mapToMachineFile=new HashMap<>();
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
      this.mapToDecryptionCandidates.put(battleField,new ArrayList<DecryptionCandidate>());
   }
   public synchronized void addFile(InputStream file,String uboatName) {
      BattleField battleField = this.getBattleFieldByBattleName(uboatName);
      this.mapToMachineFile.put(battleField,file);
   }
   public BattleField getBattleFieldByBattleName(String battleShip) {

      for (BattleField battleField : battleLinkedList) {
         if (battleField.getBattleFieldInfo().getBattleName().equals(battleShip)) {
            return battleField;

         }
      }
      return null;
   }
   public BattleField getBattleFieldByUboatName(String uboatName) {

      for (BattleField battleField : battleLinkedList) {
         if (battleField.getBattleFieldInfo().getUboatName().equals(uboatName)) {
            return battleField;

         }
      }
      return null;
   }
   public BattleField getBattleFieldByAllyName(String allyName) {

      for (BattleField battleField : battleLinkedList) {
         if(battleField.containsAlly(allyName))
         {
            return battleField;
         }
      }
      return null;
   }

    public boolean joinBoatToBattle(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleFieldByBattleName(battleShip);
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
         ally.resetAgents();
         if(ally!=null) {
            BattleField battleField = getBattleFieldByBattleName(battleShip);
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
      BattleField battleField = getBattleFieldByBattleName(battleShip);
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
          BattleField battleField = getBattleFieldByBattleName(battleShip);
          if(battleField!=null)
          {
             this.battleLinkedList.removeFirstOccurrence(battleField);
             this.mapToDecryptionCandidates.remove(battleField);
             this.mapToMachineFile.remove(battleField);
          }
       }
    }
   public synchronized void removeAlly(String battleShip, String username) {
      if(username!=null&&!username.isEmpty()) {
         BattleField battleField = getBattleFieldByBattleName(battleShip);
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
         ally.setReady(true);
      }
   }

   public InputStream getFile(String allyName) {
      BattleField battleFieldByAllyName = this.getBattleFieldByAllyName(allyName);
      return mapToMachineFile.get(battleFieldByAllyName);
   }

    public void updateTask(String allyName,Integer taskSize) {
       Ally ally = this.allAllies.get(allyName);
       ally.setEnigmaTasks(taskSize);
    }

   public void updateEncryptedWord(String battleShip, String encryptedMessage) {
      BattleField battleField=getBattleFieldByBattleName(battleShip);
      battleField.setEncryptedMessage(encryptedMessage);
   }



   public void start(String battleShip, MachineInformationDTO machineInformationDTO) {
      BattleField battleField=getBattleFieldByBattleName(battleShip);
      battleField.setStatus(BattleStatus.INPROGRESS);
      List<Ally> allies = battleField.getAllies();
      for (Ally ally:allies)
      {
         ally.startProducer(machineInformationDTO,battleField.getBattleFieldInfo());
      }
   }

    public TaskDataDTO getTasks(String allyName, int amountOfTasks, int taskDone,String agentName) {
       Ally ally = this.allAllies.get(allyName);
       return ally.getTask(amountOfTasks,taskDone,agentName);
    }

   public synchronized void addCandidates(String allyName, DecryptionCandidate[] decryptionCandidates) {
      BattleField battleField = this.getBattleFieldByAllyName(allyName);
      List<DecryptionCandidate> decryptionCandidatesList = mapToDecryptionCandidates.get(battleField);
      for (int i = 0; i <decryptionCandidates.length ; i++) {
         decryptionCandidatesList.add(decryptionCandidates[i]);
      }
   }

   public List<DecryptionCandidate> getAllyCandidates(String username) {
      BattleField battleField = this.getBattleFieldByAllyName(username);
      if(battleField==null)
      {
         return null;
      }
      List<DecryptionCandidate> decryptionCandidatesList = mapToDecryptionCandidates.get(battleField);
      return decryptionCandidatesList.stream().
              filter(decryptionCandidate -> decryptionCandidate.getAllyName().equals(username)).
              collect(Collectors.toList());

   }

   public List<DecryptionCandidate> getUboatCandidates(String username) {
      BattleField battleField = this.getBattleFieldByUboatName(username);
      return mapToDecryptionCandidates.get(battleField);
   }

   public void addAmountOfCandidate(String allyName, String agentName,int candidateFound) {

      allAllies.get(allyName).updateAmountOfCandidateFound(agentName,candidateFound);
   }

   public QueueDataDTO getQueueData(String username) {
      Ally ally = allAllies.get(username);
      return ally.getQueueData();
   }

    public void deleteEntity(String usernameFromSession) {
       Ally ally = allAllies.get(usernameFromSession);
       if(ally!=null)
       {
          allAllies.remove(usernameFromSession);
       }
       else
       {
          ally.removeAgent(usernameFromSession);
       }
    }
}
