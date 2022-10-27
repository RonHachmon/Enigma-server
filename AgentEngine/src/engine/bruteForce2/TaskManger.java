package engine.bruteForce2;

import DTO.AgentData;
import DTO.DMData;
import engine.bruteForce2.utils.CandidateList;
import engine.bruteForce2.utils.CodeConfiguration;
import engine.bruteForce2.utils.Dictionary;
import engine.bruteForce2.utils.QueueLock;
import engine.machineutils.MachineManager;
import utils.EnigmaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

public class TaskManger {


    private static final int MAX_QUEUE_SIZE = 1000;
    private final Dictionary dictionary;
    private MachineManager machineManager;
    private DMData dmData;

    private Consumer<Runnable> onCancel;
    private CandidateList candidateList = new CandidateList();
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);



    private static long totalCombinations=0;
    private static long doneCombinations=0;
    private static long totalTaskDurationInNanoSeconds = 1;


    //
    private List<MachineManager> agentMachines = new ArrayList<>();
    private AssignmentProducer assignmentProducer;
    private List<Agent> agents = new ArrayList<>();
    private final QueueLock mLock = new QueueLock();
    private final AgentData agentData;



    public CandidateList getCandidateList() {
        return candidateList;
    }

    public TaskManger(Dictionary dictionary, DMData dMData, MachineManager machineManager, Consumer<Runnable> onCancel, AgentData agentData) throws Exception {
        this.machineManager = machineManager;
        this.onCancel = onCancel;
        dmData = dMData;
        this.dictionary = dictionary;
        this.agentData=agentData;
    }



    public void stop() {
        assignmentProducer.stop();
        agents.forEach(agent -> agent.stop());
    }


    public void start() {
        try {
            this.assignmentProducer = new AssignmentProducer(blockingQueue, dmData,mLock,agentData);
            System.out.println("task manager - producer created");
            Thread producer = new Thread(this.assignmentProducer);
            producer.setName("Producer");
            producer.setDaemon(true);
            producer.start();
            System.out.println("task manager - producer start");
            for (int i = 0; i < dmData.getAmountOfAgents(); i++) {

                Agent agent = new Agent(blockingQueue, machineManager, dmData, candidateList, dictionary, i,mLock);
                System.out.println("task manager - agent created");
                Thread AgentThread = new Thread(agent);
                AgentThread.setName("Agent "+(i+1));
                AgentThread.setDaemon(true);
                AgentThread.start();
                System.out.println("task manager - agent start");
                agents.add(agent);
            }

        } catch (Exception e) {
            System.out.println("execption in task manger");
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }


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