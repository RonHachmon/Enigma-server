package engine.enigma.machineparts;

import engine.enigma.jaxb_classes.CTEEnigma;
import engine.enigma.jaxb_classes.CTEReflector;
import engine.enigma.jaxb_classes.CTEReflectors;
import engine.enigma.jaxb_classes.CTERotor;
import utils.EnigmaUtils;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Machine implements Serializable {
    private Map<Character, Integer> charMap = new HashMap<>();
    private final Map<Integer, Character> reverseCharMap = new HashMap<>();
    private final Map<Character, Character> switchPlug = new HashMap<>();
    private final List<Reflector> allReflectors = new ArrayList<>();
    private List<Rotor> selectedRotors = new ArrayList<>();
    private List<Rotor> allRotors = new ArrayList<>();
    private Reflector selectedReflector;
    private String allChars;
    private int amountOfRotorNeeded;
    private boolean isTheInitialCodeDefined;

    private int reflectorId;

    public Machine() {
        isTheInitialCodeDefined = false;
        this.initializeReflector();
    }

    public int getAmountOfRotorNeeded() {
        return amountOfRotorNeeded;
    }

    private void initializeReflector() {
        for (int i = 0; i < 5; i++) {
            allReflectors.add(null);
        }
    }

    public List<Rotor> getAllRotors() {
        return allRotors;
    }

    public List<Rotor> selectedRotors() {
        return selectedRotors;
    }

    public int getReflectorId()
    {
        return reflectorId;
    }

    //todo: why not just list.size ?
    //not sure yet if reflector can be with gaps for 1,3,5 (I,III,V);
    public int getAmountOfAvailableReflectors() {
        int count = 0;
        for (Reflector reflector : this.allReflectors) {
            if (reflector != null) {
                count++;
            }
        }
        return count;
    }

    public String getAllChars() { return this.allChars; }

    public void resetSwitchPlug() {
        this.switchPlug.clear();
    }

    private void setReverseCharMap() {
        charMap.forEach((key, value) -> reverseCharMap.put(value, key));
    }

    public void addSwitchPlug(char firstLetter, char secondLetter) {

        if (!allChars.contains(String.valueOf(firstLetter))) {
            throw new IllegalArgumentException("'" + firstLetter + "' not included in machine character Collection");
        }
        if (!allChars.contains(String.valueOf(secondLetter))) {
            throw new IllegalArgumentException("'" + secondLetter + "' not included in machine character Collection");
        }
        if (switchPlug.put(firstLetter, secondLetter) != null) {
            throw new IllegalArgumentException("Cannot assign letter '" + firstLetter + "' twice");
        }
        if (switchPlug.put(secondLetter, firstLetter) != null) {
            throw new IllegalArgumentException("Cannot assign letter '" + secondLetter + "' twice ");
        }
    }

    private void setCharMap(String char_set) {
        //maybe change to lambda, not sure how to iterate with index
        for (int i = 0; i < char_set.length(); i++) {
            if (charMap.put(char_set.charAt(i), i) != null) {
                //throw duplicate char
            }
        }
    }

    public void setSelectedReflector(int reflectorId) {

        if (reflectorId < 0 || reflectorId > this.getAmountOfAvailableReflectors()) {
            throw new IllegalArgumentException("Reflector ID must be between 1 - " + getAmountOfAvailableReflectors());
        }

        this.selectedReflector = this.allReflectors.get(reflectorId);
        if (this.selectedReflector == null) {
            throw new IllegalArgumentException("Reflector does not exist");
        }
        this.reflectorId=reflectorId;
    }

    public void setSelectedRotors(List<Integer> rotorsID) {
        this.selectedRotors.clear();
        checkArrayIsUnique(rotorsID);
        //throw out of bound or does not exist
        for (int id : rotorsID) {
            if (id < 0 || id > allRotors.size() - 1) {
                throw new IllegalArgumentException("Rotor ID must be in range of 1-" + allRotors.size());
            }
            selectedRotors.add(allRotors.get(id));
        }
    }

    public Character runEncryptOnChar(char input) {
        input = runCharThroughSwitchPlug(input);
        int running_index = charMap.get(input);
        boolean toRotate = true;
        Character result;

        //run char thought right side of Rotors
        for (int i = 0; i < selectedRotors.size(); i++) {
            //System.out.println("    machineparts.Rotor number: " + (i + 1));
            if (toRotate) {
                selectedRotors.get(i).rotate();
                toRotate = selectedRotors.get(i).isRotorOnNotch();
            }
            running_index = selectedRotors.get(i).getExitIndexFromRight(running_index);
        }

        //System.out.println("    machineparts.Reflector:");
        running_index = selectedReflector.getExitIndex(running_index);

        //run char thought left side of Rotors
        for (int i = selectedRotors.size() - 1; i >= 0; i--) {
            //System.out.println("    machineparts.Rotor number: "+(i+1));
            running_index = selectedRotors.get(i).getExitIndexFromLeft(running_index);
        }

        result = reverseCharMap.get(running_index);
        result = runCharThroughSwitchPlug(result);
        //System.out.println("output char: "+result);

        return result;
    }

    private char runCharThroughSwitchPlug(char input) {
        if (switchPlug.get(input) != null) {
            input = switchPlug.get(input);
        }
        return input;
    }

    public String runEncryptOnString(String input) {
        this.isAllCharsExistInCharSet(input);
        String res = new String();
        for (int i = 0; i < input.length(); i++) {
            res += this.runEncryptOnChar(input.charAt(i));
        }
        return res;
    }

    //XML function
    public void loadRotators(CTEEnigma enigma_machine) {
        List<CTERotor> xmlRotorsArr = enigma_machine.getCTEMachine().getCTERotors().getCTERotor();
        amountOfRotorNeeded = enigma_machine.getCTEMachine().getRotorsCount();
        if (amountOfRotorNeeded < 2) {
            throw new IllegalArgumentException("amount of rotors on the machine must be at least 2");
        }
        xmlRotorsArr = sortXMLRotors(xmlRotorsArr);
        loadArrayOfRotators(xmlRotorsArr);
        if (allRotors.size() < amountOfRotorNeeded) {
            throw new IllegalArgumentException("amount of rotors should be at least " + amountOfRotorNeeded);
        }
    }

    private void loadArrayOfRotators(List<CTERotor> xmlRotorsArr) {
        Rotor currentRotor = new Rotor();
        for (int i = 0; i < xmlRotorsArr.size(); i++) {
            //check if the id are sequential and unique
            if (xmlRotorsArr.get(i).getId() != i + 1) {
                throw new IllegalArgumentException("rotor id must be unique and in a sequential order," +
                        "starting from 1");
            }

            currentRotor = Rotor.createRotorFromXML(xmlRotorsArr.get(i), this.allChars);
            allRotors.add(currentRotor);
        }
    }

    private List<CTERotor> sortXMLRotors(List<CTERotor> xmlRotorsArr) {
        xmlRotorsArr = xmlRotorsArr.stream().
                sorted(Comparator.comparing(CTERotor::getId)).
                collect(Collectors.toList());

        return xmlRotorsArr;
    }

    public void loadReflector(CTEEnigma enigma_machine) {
        CTEReflectors xmlReflectorArr = enigma_machine.getCTEMachine().getCTEReflectors();
        Reflector currentReflector;

        if (xmlReflectorArr.getCTEReflector().isEmpty()) {
            throw new IllegalArgumentException("xml must hold at least one reflector");
        }

        for (CTEReflector xmlReflector : xmlReflectorArr.getCTEReflector()) {
            currentReflector = Reflector.createReflectorFromXML(xmlReflector, charMap.size());
            EnigmaUtils.convertRomanToInt("I");
            int position = EnigmaUtils.convertRomanToInt(xmlReflector.getId())-1;
            //System.out.println("machineparts.Reflector:"+ currentReflector);
            if (allReflectors.set(position, currentReflector) != null) {
                throw new IllegalArgumentException("Each reflectors must have a different ID");
            }
        }
        for (int i = 0; i < this.getAmountOfAvailableReflectors(); i++) {
            if (allReflectors.get(i) == null) {
                throw new IllegalArgumentException("Reflectors cannot have gaps between id's");
            }
        }
    }

    public void loadCharSet(CTEEnigma enigma_machine) {
        String charCollection = enigma_machine.getCTEMachine().getABC();
        charCollection = charCollection.replaceAll("[\\n\t]", "");
        charCollection = replaceSpecialXMLchar(charCollection);
        if (charCollection.length() % 2 == 1) {
            throw new IllegalArgumentException("amount of characters must be even");
        }

        this.allChars = charCollection.toUpperCase(Locale.ROOT);
        //System.out.println("all char: "+this.allChars);
        this.setCharMap(charCollection);
        this.setReverseCharMap();
    }

    private String replaceSpecialXMLchar(String string) {
        String result = string.replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", "<")
                .replace("&quot;", "")
                .replace("&apos;", "'");
        return result;
    }

    public void setStartingIndex(String startingCharArray) {
        if (startingCharArray.length() != selectedRotors.size()) {
            throw new IllegalArgumentException("please choose starting index for all " + selectedRotors.size() + " rotors");
        }

        for (int i = 0; i < startingCharArray.length(); i++) {
            this.selectedRotors.get(i).setStartingIndex(startingCharArray.charAt(i));
        }
    }



    private void isAllCharsExistInCharSet(String inputString) {
        Set<Character> allChar = new HashSet<>();
        Set<Character> notFoundChars = new HashSet<>();
        StringBuilder nonExistingChar = new StringBuilder();

        allChars.chars().forEach(character -> allChar.add((char) character));
        inputString.chars().forEach(c ->
        {
            //for new char in set add return true
            if (allChar.add((char) c)) {
                notFoundChars.add((char) c);
            }
        });

        if (!notFoundChars.isEmpty()) {
            notFoundChars.forEach(character -> nonExistingChar.append(character + " , "));
            throw new IllegalArgumentException("machine does not contain letter: " + notFoundChars);
        }
    }


    private void checkArrayIsUnique(List<Integer> rotorsID) {
        if (rotorsID.stream().distinct().count() != rotorsID.size()) {
            throw new IllegalArgumentException("rotors id must be unique");
        }
    }

    public boolean isTheInitialCodeDefined() {
        return isTheInitialCodeDefined;
    }

    public void setTheInitialCodeDefined(boolean theInitialCodeDefined) {
        isTheInitialCodeDefined = theInitialCodeDefined;
    }

    public Machine clone() throws CloneNotSupportedException {
        Machine clonedMachine = new Machine();
        clonedMachine.selectedReflector = (Reflector) this.selectedReflector.clone();
        clonedMachine.selectedRotors = new ArrayList<>(this.selectedRotors);
        clonedMachine.allChars = this.allChars;
        clonedMachine.charMap = new HashMap<>(this.charMap);
        clonedMachine.setReverseCharMap();
        return clonedMachine;



    }


}
