package engine.enigma.battlefield.entities.task;

import DTO.CodeSettingDTO;
import DTO.MachineInformationDTO;
import DTO.QueueDataDTO;
import DTO.TaskDataDTO;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.bruteForce2.utils.CodeConfiguration;
import utils.EnigmaUtils;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class EnigmaTasks {
    private static final int MAX_QUEUE_SIZE = 1000;

    private TaskData taskData=new TaskData();

    private AssignmentProducer assignmentProducer;
    BlockingQueue<CodeConfiguration> blockingQueue ;

    public Integer getTaskSize() {
        return taskData.getTaskSize();
    }

    public void setTaskSize(Integer taskSize) {
        this.taskData.setTaskSize(taskSize);
    }

    public BlockingQueue<CodeConfiguration> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue<CodeConfiguration> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void startProducer(MachineInformationDTO machineInformationDTO, BattleFieldInfo battleFieldInfo) {
        blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);
        this.calcMissionSize(machineInformationDTO,battleFieldInfo);
        try {
            assignmentProducer =new AssignmentProducer(blockingQueue,taskData,battleFieldInfo.getLevel(),machineInformationDTO);
            Thread producer = new Thread(this.assignmentProducer);
            producer.setName("Producer");
            producer.setDaemon(true);
            producer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void calcMissionSize(MachineInformationDTO machineInformationDTO,BattleFieldInfo battleFieldInfo) {
        long easy = (long) Math.pow(machineInformationDTO.getAvailableChars().length(), machineInformationDTO.getAmountOfRotorsRequired());
        long medium = easy * machineInformationDTO.getAvailableReflectors();
        long hard = medium * EnigmaUtils.factorial(machineInformationDTO.getStartingRotors().length);
        long impossible = hard * EnigmaUtils.binomial(machineInformationDTO.getAmountOfRotorsRequired(),machineInformationDTO.getAmountOfRotors());

        switch (battleFieldInfo.getLevel()) {
            case EASY:
                taskData.setTotalCombinations(easy);
                break;
            case MEDIUM:
                taskData.setTotalCombinations(medium);
                break;
            case HARD:
                taskData.setTotalCombinations(hard);
                break;
            case IMPOSSIBLE:
                taskData.setTotalCombinations(impossible);
                break;
        }
    }
    public synchronized TaskDataDTO getTasks(int amountOfTask)
    {

        CodeSettingDTO[] codeSettingDTOS=new CodeSettingDTO[amountOfTask];
        int actualSettingSize=0;
        for (int i = 0; i <amountOfTask ; i++) {
            CodeConfiguration codeConfiguration = null;
            try {
                codeConfiguration = blockingQueue.poll(400,TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(codeConfiguration==null)
            {
                break;
            }
            actualSettingSize++;
            codeSettingDTOS[i]=new CodeSettingDTO(codeConfiguration);
        }

        if(actualSettingSize!=amountOfTask)
        {
            CodeSettingDTO[] codeSettingDTOS2=new CodeSettingDTO[actualSettingSize];
            for (int i = 0; i <actualSettingSize ; i++) {
                codeSettingDTOS2[i]=codeSettingDTOS[i];
            }
            codeSettingDTOS=codeSettingDTOS2;
        }
        TaskDataDTO taskDataDTO=new TaskDataDTO(codeSettingDTOS,this.getTaskSize());


        return taskDataDTO;


    }

    public void reset() {
        this.taskData.reset();
    }

    public QueueDataDTO getQueueData() {
        return new QueueDataDTO(this.taskData);
    }
}
