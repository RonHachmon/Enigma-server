package engine.enigma.machineutils;


import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.battlefield.BattlesManager;
import engine.enigma.bruteForce2.utils.Dictionary;
import engine.enigma.jaxb_classes.CTEDictionary;
import engine.enigma.jaxb_classes.CTEEnigma;
import engine.enigma.machineparts.Machine;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineManager implements Serializable {
    private Machine machine = new Machine();
    private Statistic statistic = new Statistic();
    private NewStatistic newStatistic = new NewStatistic();
    private Setting setting = new Setting();
    private MachineInformation machineInformation = null;
    private int processedInputCounter = 0;
    private boolean isMachineExists = false;
    private BattleFieldInfo battleField;

    private String filePath;
    private Dictionary dictionary=new Dictionary();

    public Dictionary getDictionary() {
        return dictionary;
    }
    public BattleFieldInfo getBattleField() {
        return battleField;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public int getReflector()
    {
         return this.machine.getReflectorId();
    }
    public String getAvailableChars() {
        return machineInformation.getAvailableChars();
    }
    public String getStatistic() {
        return statistic.historyAndStatistic();
    }

    public void addCodeToStatistic() {
        this.statistic.addCodeFormats(this.setting.getInitialFullMachineCode());
    }

    public int getAmountOfProcessedInputs() {
        return this.statistic.getAmountOfProcessedInputs();
    }

    public String encryptSentenceAndAddToStatistic(String sentence) {
        Instant timeStart = Instant.now();
        String output = this.machine.runEncryptOnString(sentence);
        StringBuilder timeAndStatistic = new StringBuilder(++processedInputCounter + ". ");
        Duration duration = Duration.between(timeStart, Instant.now());

        buildHistoryAndStatistic(sentence, timeAndStatistic, output, duration);
        newStatistic.addStatistic(setting.getInitialFullMachineCode(),sentence,output,duration.getNano());
        newStatistic.printMap();
        this.statistic.addProcessedInput(timeAndStatistic.toString());

        return output;
    }
    public String encryptSentence(String sentence) {
        String output = this.machine.runEncryptOnString(sentence);
        return output;
    }

    public void commitChangesToMachine() {
        processedInputCounter = 0;
        machine.setTheInitialCodeDefined(true);
        this.addCodeToStatistic();
    }

    public void setSelectedRotors(List<Integer> rotorsID) {
        if (rotorsID.size() != machineInformation.getAmountOfRotorsRequired()) {
            throw new IllegalArgumentException("amount of indexes must be " + machineInformation.getAmountOfRotorsRequired());
        }
        this.machine.setSelectedRotors(rotorsID);
        this.setting.setSettingRotators(rotorsID);
    }

    public void setSelectedReflector(int reflectorId) {
        this.machine.setSelectedReflector(reflectorId);
        this.setting.setSettingReflector(reflectorId + 1);
    }

    public void setStartingIndex(String startingCharArray) {
        this.machine.setStartingIndex(startingCharArray);
        this.setting.setSettingStartingChar(startingCharArray,this.machine);
    }

    public void setIsMachineExists(boolean isMachineExists){
        this.isMachineExists = isMachineExists;
    }

    public void setSwitchPlug(String plugs) {
        if (plugs.length() % 2 != 0) {
            throw new IllegalArgumentException("invalid plugs, each character should be paired ");
        }
        this.machine.resetSwitchPlug();
        this.setting.resetPlugs();

        for (int i = 0; i < plugs.length(); i += 2) {
            this.addSwitchPlug(plugs.charAt(i), plugs.charAt(i + 1));
        }
    }

    private void addSwitchPlug(char firstLetter, char secondLetter) {
        this.machine.addSwitchPlug(firstLetter, secondLetter);
        this.setting.addPlug(firstLetter, secondLetter);
    }

    public String getInitialFullMachineCode() {
        return this.setting.getInitialFullMachineCode();
    }

    public String getCurrentCodeSetting() {
        return this.setting.getCurrentMachineCode(this.machine);
    }

    public List<Integer> getCurrentRotorsList() {
        return this.setting.getCurrentRotorsList();
    }

    public void resetMachineCode() {
        StringBuilder stringBuilder = new StringBuilder(this.setting.getInitialRotorIndexes());
        stringBuilder.reverse();
        this.setStartingIndex(stringBuilder.toString());
    }

    public MachineInformation getMachineInformation() {
        return machineInformation;
    }
    public void createMachineFromXML(InputStream inputStream) {

        CTEEnigma enigma_machine = null;
        try {
            enigma_machine = JAXBClassGenerator.unmarshall(inputStream, CTEEnigma.class);
        } catch (JAXBException e) {
            String msg;
            if (e.getLinkedException() instanceof FileNotFoundException) {
                msg = "File Not Found";
            } else {
                msg = "JAXB exception";
            }
            throw new IllegalArgumentException(msg);
        }

        if (enigma_machine == null) {
            throw new IllegalArgumentException("Failed to load JAXB class");
        }

        this.loadEnigmaPartFromXMLEnigma(enigma_machine);
    }


    public void createMachineFromXML(String filePath) {
        Path path = Paths.get(filePath);
        if (filePath.length() < 4 || !compareFileType(filePath, ".xml")) {
            throw new IllegalArgumentException(path.getFileName() + ", must be a xml file");
        }

        CTEEnigma enigma_machine = null;
        try {
            enigma_machine = JAXBClassGenerator.unmarshall(filePath, CTEEnigma.class);
            this.filePath=filePath;
        } catch (JAXBException e) {
            String msg;
            if (e.getLinkedException() instanceof FileNotFoundException) {
                msg = "File Not Found";
            } else {
                msg = "JAXB exception";
            }
            throw new IllegalArgumentException(msg);
        }

        if (enigma_machine == null) {
            throw new IllegalArgumentException("Failed to load JAXB class");
        }

        this.loadEnigmaPartFromXMLEnigma(enigma_machine);
        //this.m_GameStatus = GriddlerLogic.eGameStatus.LOADED;
    }

    private boolean compareFileType(String fileName, String fileType) {
        if (fileType.length() >= fileName.length())
            return false;
        String file_ending = fileName.substring(fileName.length() - fileType.length()).toLowerCase();
        return file_ending.compareTo(fileType) == 0;
    }

    private void loadEnigmaPartFromXMLEnigma(CTEEnigma enigma) {
        Machine tempMachine = new Machine();
        tempMachine.loadCharSet(enigma);
        tempMachine.loadRotators(enigma);
        tempMachine.loadReflector(enigma);
        CTEDictionary cteDictionary = enigma.getCTEDecipher().getCTEDictionary();
        this.dictionary.setDictionary(cteDictionary.getWords(), cteDictionary.getExcludeChars());
        this.battleField=BattlesManager.getInstance().createBattle(enigma.getCTEBattlefield());
        BattlesManager.getInstance().addBattle(battleField);
        machine = tempMachine;
        isMachineExists = true;
        this.machineInformation = new MachineInformation(machine);
    }

    public void autoZeroMachine() {
        Random rand = new Random();
        List<Integer> indexes = new ArrayList<>();
        String startingCharArray = new String();

        // Set rotors & starting indexes
        for (int i = 0; i < machineInformation.getAmountOfRotorsRequired(); i++) {
            int rotorIndex = rand.nextInt(machineInformation.getAmountOfRotors());
            while (indexes.contains(rotorIndex)) {
                rotorIndex = rand.nextInt(machineInformation.getAmountOfRotors());
            }
            int randomInt = rand.nextInt(machineInformation.getAvailableChars().length());
            char randomChar = machineInformation.getAvailableChars().charAt(randomInt);

            indexes.add(rotorIndex);
            startingCharArray += randomChar;
        }

        setSelectedRotors(indexes);
        setStartingIndex(startingCharArray);
        setSelectedReflector(rand.nextInt(machineInformation.getAvailableReflectors()));
        commitChangesToMachine();
    }



    public boolean isMachineExists() {
        return this.isMachineExists;
    }

    public boolean isMachineSettingInitialized() {
        if(machine==null)
        {
            return false;
        }
        return machine.isTheInitialCodeDefined();
    }


    public void saveMachineToFile(String path) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(path))) {
            out.writeObject(machine);
            out.writeObject(statistic);
            out.writeObject(setting);
            out.writeObject(processedInputCounter);
            out.flush();
        }
    }

    public void loadMachineFromFile(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(path))) {
            machine = (Machine) in.readObject();
            statistic = (Statistic) in.readObject();
            setting = (Setting) in.readObject();
            processedInputCounter = (int) in.readObject();
            this.machineInformation = new MachineInformation(machine);
        }
    }

    public void reset()
    {
        this.statistic.reset();
        this.processedInputCounter=0;

    }
    private void buildHistoryAndStatistic(String sentence, StringBuilder timeAndStatistic, String output, Duration duration) {
        timeAndStatistic.append('<' + sentence + '>');
        timeAndStatistic.append("-->");
        timeAndStatistic.append('<' + output + '>');
        timeAndStatistic.append("(" + duration.getNano() + " nano-seconds)");
    }

    //--------------------------------------------New StatisticRelated--------------------------------
    public ArrayList<NewStatisticInput> getStatsPerCode(String code)
    {
        return newStatistic.getStatsPerCode(code);
    }

    public NewStatisticInput getLastStatsPerCode(String code)
    {
        ArrayList<NewStatisticInput> statsPerCode = newStatistic.getStatsPerCode(code);
        int lastIndex = statsPerCode.size();
        return  statsPerCode.get(lastIndex-1);
    }
    public void sendToStats(String input,String output,Integer duration)
    {
        newStatistic.addStatistic(this.getInitialFullMachineCode(),input,output,duration);

    }
    public int getProcessedInputCounter(){
        return newStatistic.getTotalEncryptedWordCounter();
    }
    //--------------------------------------------EndOf:New StatisticRelated--------------------------------

    //--------------------------------------------Brute Force Related--------------------------------

    public MachineManager clone() throws CloneNotSupportedException{
        MachineManager clonedManager = new MachineManager();
        clonedManager.machine = this.machine.clone();
        clonedManager.setting=this.setting.clone();
        clonedManager.machineInformation=this.machineInformation.clone(clonedManager.machine);

        return clonedManager;
    }
    //--------------------------------------------ENDof: Brute Force Related--------------------------------
}
