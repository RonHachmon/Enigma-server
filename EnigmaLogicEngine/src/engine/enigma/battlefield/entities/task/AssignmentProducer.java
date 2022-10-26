package engine.enigma.battlefield.entities.task;

import DTO.DMData;
import DTO.MachineInformationDTO;
import engine.enigma.bruteForce2.utils.CodeConfiguration;
import engine.enigma.bruteForce2.utils.QueueLock;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import utils.ListPermutation;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AssignmentProducer implements Runnable {

    public static boolean isDone = false;
    private final MachineInformationDTO machineInformationDTO;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private CodeConfiguration codeConfiguration;
    private Permutation permutation;
    private ListPermutation listPermutation;

    private boolean toStop = false;
    private String startingIndexes;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, MachineInformationDTO machineInformationDTO) throws Exception {
        this.queue = codeQueue;
        this.dmData = dmData;
        this.setInitialConfiguration();
        this.machineInformationDTO=machineInformationDTO;
        this.permutation = new Permutation(machineInformationDTO.getAvailableChars());


    }
    private void generateCode() throws InterruptedException {
        String nextPermutation;
        do {

            if (toStop) {
                System.out.println("Producer stopped  code :)");
                return;
            }
            /* System.out.println("Producer working");*/
            queue.put(codeConfiguration.clone(codeConfiguration));
            nextPermutation = permutation.increasePermutation(dmData.getAssignmentSize(), codeConfiguration.getCharIndexes());
            this.codeConfiguration.setCharIndexes(nextPermutation);
        } while (permutation.isOverFlow() == false);
        permutation.cleanOverFLow();
        codeConfiguration.setCharIndexes(this.startingIndexes);
    }

    private void setInitialConfiguration() {
        //easy set up
        this.startingIndexes = "";
        List<Integer> startingRotors = new ArrayList<>();//done

        for (int rotorID : machineInformationDTO.getStartingRotors()) {
            // Add each element into the list
            startingRotors.add(rotorID);
        }
        int reflectorID = machineInformationDTO.getReflectorID();
        for (int i = 0; i < machineInformationDTO.getAmountOfRotorsRequired(); i++) {
            startingIndexes += machineInformationDTO.getAvailableChars().charAt(0);
        }
        codeConfiguration = new CodeConfiguration(startingIndexes, startingRotors, reflectorID);
    }

    @Override
    public void run() {
        try {
            chooseDifficulty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private void chooseDifficulty() throws InterruptedException {
        /*    System.out.println("starting push code");*/
        switch (dmData.getDifficulty()) {
            case EASY:
                this.generateCode();
                break;
            case MEDIUM:
                this.goOverAllReflector();
                break;
            case HARD:
                this.goOverAllKnownRotor();
                break;
            case IMPOSSIBLE:
                this.goOverAllKRotor();
                break;
        }
        isDone = true;
        System.out.println("Producer Done");
    }



    private void goOverAllReflector() throws InterruptedException {
        for (int i = 0; i < machineInformationDTO.getAvailableReflectors(); i++) {
            codeConfiguration.setReflectorID(i);
            this.generateCode();
            if (toStop) {
                /* System.out.println("Producer stopped reflector combination :)");*/
                return;
            }

        }

    }

    private void goOverAllKnownRotor() throws InterruptedException {
        this.listPermutation = new ListPermutation(codeConfiguration.getRotorsID(), codeConfiguration.getRotorsID());
        List<Integer> currentIDs;
        while (!listPermutation.done()) {
            currentIDs = listPermutation.increasePermutation();
            codeConfiguration.setRotorsID(currentIDs);
            goOverAllReflector();
            if (toStop) {
                /* System.out.println("Producer stopped rotor combination :)");*/
                return;
            }
        }
    }

    private void goOverAllKRotor() throws InterruptedException {
        List<Integer> allRotors = new ArrayList<>();
        for (int i = 0; i < machineInformationDTO.getAmountOfRotors(); i++) {
            allRotors.add(i);

        }
        this.listPermutation = new ListPermutation(codeConfiguration.getRotorsID(), allRotors);
        List<Integer> currentIDs;
        while (!listPermutation.done()) {
            currentIDs = listPermutation.increasePermutation();
            codeConfiguration.setRotorsID(currentIDs);
            goOverAllReflector();
            if (toStop) {
                /*System.out.println("Producer stopped rotor combination :)");*/
                return;
            }
        }
    }


    public void stop() {
        this.toStop = true;
    }
}
