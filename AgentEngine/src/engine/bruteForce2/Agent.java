package engine.bruteForce2;

import DTO.DMData;
import DTO.DecryptionCandidate;
import engine.bruteForce2.utils.CandidateList;
import engine.bruteForce2.utils.CodeConfiguration;
import engine.bruteForce2.utils.Dictionary;
import engine.bruteForce2.utils.QueueLock;
import engine.machineutils.MachineManager;
import utils.ObjectCloner;
import utils.Permutation;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


//Consumer
public class Agent implements Runnable {

    private BlockingQueue<CodeConfiguration> queue;

    private int id = 0;
    private MachineManager machineManager;
    private DMData dMdata;
    private String currentCharConfiguration;
    private Permutation permutation;


    private CandidateList candidateList;
    private Dictionary dictionary;

    private int amountOfPermutaionIWent = 0;
    private boolean toStop = false;
    private final QueueLock queueLock;

    public Agent(BlockingQueue<CodeConfiguration> queue, MachineManager machineManager, DMData dMData, CandidateList candidateList, Dictionary dictionary, int ID, QueueLock queueLock) throws Exception {
        this.id = ID + 1;
        this.machineManager = (MachineManager) ObjectCloner.deepCopy(machineManager);
        this.dMdata = dMData;
        this.candidateList = candidateList;
        this.queue = queue;
        permutation = new Permutation(machineManager.getMachineInformation().getAvailableChars());
        this.dictionary = dictionary;
        this.queueLock=queueLock;
    }

    @Override
    public void run() {
        try {
            System.out.println("Agent run method");
            /*System.out.println("starting pull code");*/
            while (!toStop) {
                CodeConfiguration codeConfiguration = queue.poll();
                //if queue is empty signal the producer to get more and wait
                if (codeConfiguration == null) {
                    queueLock.unlock();
                    codeConfiguration = queue.poll(2000,TimeUnit.MILLISECONDS);
                }
                if (codeConfiguration != null) {

    /*                System.out.println("Agent code taken");*/
                    this.setInitialMachine(codeConfiguration);
                    int amountOfPermutationIwentInTheLoop = 0;
                    String encryptionOutput;
                    for (int i = 0; i < dMdata.getAssignmentSize(); i++) {
                        amountOfPermutaionIWent++;

                        if (toStop) {
                            return;
                        }
                         /*System.out.println("Agent working");*/
                        /*System.out.println(machineManager.getInitialFullMachineCode());*/

                        encryptionOutput = machineManager.encryptSentence(dMdata.getEncryptedString());


                        amountOfPermutationIwentInTheLoop++;

                        if (dictionary.isAtDictionary(encryptionOutput)) {
                            candidateList.addCandidate(createCandidate(encryptionOutput));
                        }

                        increaseCodePermutation(codeConfiguration);
                        if (permutation.isOverFlow()) {
                            break;
                        }

                    }
                    permutation.cleanOverFLow();
                }
                else {
                    System.out.println("pemutation i went "+amountOfPermutaionIWent+ " "+Thread.currentThread().getName());
                    return;
                }
            }
            System.out.println("Agent stopped");

        } catch (Exception e) {
            System.out.println("Agent got exception");
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private boolean isDone() {
        if (TaskManger.getWorkDone() >= TaskManger.getTotalWork()) {
            return true;
        }
        return false;
    }

    private void increaseCodePermutation(CodeConfiguration codeConfiguration) {
        currentCharConfiguration = permutation.increasePermutation(1, currentCharConfiguration);
        this.machineManager.setStartingIndex(currentCharConfiguration);
    }

    private DecryptionCandidate createCandidate(String encryptionOutput) {
        return new DecryptionCandidate(this.getAgentId(), encryptionOutput, machineManager.getInitialFullMachineCode());
    }

    private void setInitialMachine(CodeConfiguration codeConfiguration) {

        this.machineManager.setSelectedRotors(codeConfiguration.getRotorsID());
        this.machineManager.setStartingIndex(codeConfiguration.getCharIndexes());
        this.machineManager.setSelectedReflector(codeConfiguration.getReflectorID());
        currentCharConfiguration = codeConfiguration.getCharIndexes();

    }

    public int getAgentId() {
        return id;
    }

    public void stop() {
        this.toStop = true;
    }




}