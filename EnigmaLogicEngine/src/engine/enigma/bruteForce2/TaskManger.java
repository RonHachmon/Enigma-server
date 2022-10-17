package engine.enigma.bruteForce2;

import DTO.DMData;
import engine.enigma.bruteForce2.utils.CandidateList;
import engine.enigma.bruteForce2.utils.CodeConfiguration;
import engine.enigma.bruteForce2.utils.Dictionary;
import engine.enigma.bruteForce2.utils.QueueLock;
import engine.enigma.machineutils.MachineManager;
import utils.EnigmaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TaskManger {


    private static final int MAX_QUEUE_SIZE = 1000;
    private final Dictionary dictionary;
    private MachineManager machineManager;
    private DMData dmData;

    private Consumer<Runnable> onCancel;
    private CandidateList candidateList = new CandidateList();
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);
    private QueueLock queueLock = new QueueLock();


    private static long totalCombinations=0;
    private static long doneCombinations=0;
    private static long totalTaskDurationInNanoSeconds = 1;


    //
    private List<MachineManager> agentMachines = new ArrayList<>();
    private AssignmentProducer assignmentProducer;
    private List<Agent> agents = new ArrayList<>();



    public CandidateList getCandidateList() {
        return candidateList;
    }

    public TaskManger(Dictionary dictionary, DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel) throws Exception {
        this.machineManager = machineManager;
        this.onCancel = onCancel;
        dmData = dMData;
        this.dictionary = dictionary;
        calcMissionSize();
    }

    public void pause() {
        queueLock.lock();
    }

    public void resume() {
        queueLock.unlock();
    }

    public void stop() {
        assignmentProducer.stop();
        agents.forEach(agent -> agent.stop());
    }


    public void start() {
        try {
            this.assignmentProducer = new AssignmentProducer(blockingQueue, dmData, machineManager, queueLock);
            Thread producer = new Thread(this.assignmentProducer);
            producer.setName("Producer");
            producer.setDaemon(true);
            producer.start();
            for (int i = 0; i < dmData.getAmountOfAgents(); i++) {

                Agent agent = new Agent(blockingQueue, machineManager, dmData, candidateList, dictionary, i, queueLock);
                Thread AgentThread = new Thread(agent);
                AgentThread.setName("Agent "+(i+1));
                AgentThread.setDaemon(true);
                AgentThread.start();
                agents.add(agent);
            }

        } catch (Exception e) {
            System.out.println("execption in task manger");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }


    }

    private void calcMissionSize() {
        long easy = (long) Math.pow(machineManager.getAvailableChars().length(), machineManager.getCurrentRotorsList().size());
        long medium = easy * machineManager.getMachineInformation().getAvailableReflectors();
        long hard = medium * EnigmaUtils.factorial(machineManager.getCurrentRotorsList().size());
        long impossible = hard * EnigmaUtils.binomial(machineManager.getCurrentRotorsList().size(), machineManager.getMachineInformation().getAmountOfRotors());

        switch (dmData.getDifficulty()) {
            case EASY:
                totalCombinations = easy;
                break;
            case MEDIUM:
                totalCombinations = medium;
                break;
            case HARD:
                totalCombinations = hard;
                break;
            case IMPOSSIBLE:
                totalCombinations = impossible;
                break;
        }
        System.out.println("Total combination " + totalCombinations);
    }


    public long getTotalTaskDurationInMilliSeconds() {

        return (totalTaskDurationInNanoSeconds);
    }


    public static long getTotalWork() {
        return totalCombinations;
    }


    public long getAvgTaskDuration() {
        return (totalTaskDurationInNanoSeconds / (totalCombinations / dmData.getAssignmentSize()));
    }


    public static long getWorkDone() {
        return doneCombinations;
    }
    public static void resetStaticMembers() {
        TaskManger.resetTotalWork();
        TaskManger.resetWorkDone();
        TaskManger.resetTime();

    }

    public static void resetTotalWork() {
        totalCombinations = 0;
    }


    synchronized static public void addWorkDone(long number, long currentEncryptionDuration) {
        doneCombinations += number;
        totalTaskDurationInNanoSeconds += currentEncryptionDuration;
    }

    public static void resetWorkDone() {
        doneCombinations = 0;
    }
    public static void resetTime() {
        totalTaskDurationInNanoSeconds = 1;
    }


}