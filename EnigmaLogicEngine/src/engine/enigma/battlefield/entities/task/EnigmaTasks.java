package engine.enigma.battlefield.entities.task;

import DTO.CodeSettingDTO;
import DTO.MachineInformationDTO;
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
    BlockingQueue<CodeConfiguration> blockingQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);

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
        System.out.println("start producer");
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
   /*     try {
            assignmentProducer=new AssignmentProducer(blockingQueue,new DMData(),machineInformationDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        //to be implemented
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
        for (int i = 0; i <amountOfTask ; i++) {
            CodeConfiguration codeConfiguration = null;
            try {
                codeConfiguration = blockingQueue.poll(400,TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            codeSettingDTOS[i]=new CodeSettingDTO(codeConfiguration);
        }
        TaskDataDTO taskDataDTO=new TaskDataDTO(codeSettingDTOS,this.getTaskSize());


        return taskDataDTO;


    }

}
