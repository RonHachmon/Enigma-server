package engine.enigma.bruteForce2;

import DTO.DMData;
import engine.enigma.bruteForce2.utils.CodeConfiguration;
import engine.enigma.bruteForce2.utils.QueueLock;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import engine.enigma.bruteForce2.utils.DifficultyLevel;
import utils.ListPermutation;
import utils.ObjectCloner;
import utils.Permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AssignmentProducer implements Runnable {
    private final QueueLock lock;
    public static boolean isDone = false;
    private final MachineInformation machineInformation;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private CodeConfiguration codeConfiguration;
    private Permutation permutation;
    private ListPermutation listPermutation;
    private MachineManager machineManager;
    private boolean toStop = false;
    private String startingIndexes;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, MachineManager machineManager, QueueLock queueLock) throws Exception {
        this.queue = codeQueue;
        this.dmData = dmData;
        this.machineManager = (MachineManager) ObjectCloner.deepCopy(machineManager);
        this.machineInformation = machineManager.getMachineInformation();
        this.setInitialConfiguration();
        this.permutation = new Permutation(machineManager.getAvailableChars());
        lock = queueLock;

    }

    private void setInitialConfiguration() {
        //easy set up
        this.startingIndexes = "";
        List<Integer> startingRotors = machineManager.getCurrentRotorsList();
        int reflectorID = machineManager.getReflector();
        for (int i = 0; i < machineInformation.getAmountOfRotorsRequired(); i++) {
            startingIndexes += machineInformation.getAvailableChars().charAt(0);
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

    private void generateCode() throws InterruptedException {
        String nextPermutation;
        do {

            lock.checkIfLocked();
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

    private void goOverAllReflector() throws InterruptedException {
        for (int i = 0; i < machineInformation.getAvailableReflectors(); i++) {
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
        for (int i = 0; i < machineInformation.getAmountOfRotors(); i++) {
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
