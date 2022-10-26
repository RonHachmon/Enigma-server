package engine.enigma.battlefield.entities.task;

import DTO.CodeSettingDTO;
import DTO.DMData;
import DTO.MachineInformationDTO;
import engine.enigma.bruteForce2.utils.CodeConfiguration;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EnigmaTasks {
    private static final int MAX_QUEUE_SIZE = 1000;
    private Integer taskSize=null;
    private int taskCreated;
    private int taskPulled;
    private AssignmentProducer assignmentProducer;
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);

    public Integer getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }

    public BlockingQueue<CodeConfiguration> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue<CodeConfiguration> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void startProducer(MachineInformationDTO machineInformationDTO) {
   /*     try {
            assignmentProducer=new AssignmentProducer(blockingQueue,new DMData(),machineInformationDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        //to be implemented
    }
}
