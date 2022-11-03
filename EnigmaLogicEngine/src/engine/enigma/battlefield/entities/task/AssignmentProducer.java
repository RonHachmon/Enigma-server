package engine.enigma.battlefield.entities.task;


import DTO.MachineInformationDTO;
import engine.enigma.bruteForce2.utils.CodeConfiguration;


import utils.DifficultyLevel;
import utils.ListPermutation;
import utils.Permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AssignmentProducer implements Runnable {

   /* public static boolean isDone = false;*/
    private final MachineInformationDTO machineInformationDTO;
    private final TaskData taskData;
    private final DifficultyLevel difficultyLevel;
    private BlockingQueue<CodeConfiguration> queue;

    private CodeConfiguration codeConfiguration;
    private Permutation permutation;
    private ListPermutation listPermutation;

    private boolean toStop = false;
    private String startingIndexes;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, TaskData taskData, DifficultyLevel difficultyLevel, MachineInformationDTO machineInformationDTO) throws Exception {
        this.machineInformationDTO=machineInformationDTO;
        this.queue = codeQueue;
        this.taskData = taskData;
        this.difficultyLevel = difficultyLevel;
        this.setInitialConfiguration();
        this.permutation = new Permutation(machineInformationDTO.getAvailableChars());
        System.out.println("Producer initi");


    }
    private void generateCode() throws InterruptedException {
        String nextPermutation;
        do {

            if (toStop) {
                System.out.println("Producer stopped  code :)");
                return;
            }
            queue.put(codeConfiguration.clone(codeConfiguration));
            taskData.increasedTaskCreated();

            nextPermutation = permutation.increasePermutation(taskData.getTaskSize(), codeConfiguration.getCharIndexes());
            this.codeConfiguration.setCharIndexes(nextPermutation);
        } while (permutation.isOverFlow() == false);
        permutation.cleanOverFLow();
        codeConfiguration.setCharIndexes(this.startingIndexes);
    }

    private void setInitialConfiguration() {
        //easy set up
        this.startingIndexes = "";
        List<Integer> startingRotors = new ArrayList<>();

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
        switch (difficultyLevel) {
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
/*        isDone = true;*/
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
