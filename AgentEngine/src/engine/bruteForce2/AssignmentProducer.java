package engine.bruteForce2;


import DTO.*;
import engine.bruteForce2.utils.CodeConfiguration;
import engine.bruteForce2.utils.QueueLock;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.ListPermutation;
import utils.ObjectCloner;
import utils.Permutation;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static web.Constants.CANDIDATES;
import static web.Constants.GSON_INSTANCE;

public class AssignmentProducer implements Runnable {

    public static boolean isDone = false;
    private final QueueLock queueLock;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private boolean toStop = false;
    private final AgentData agentData;
    private int amountOfCodeItook=0;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData,QueueLock lock,AgentData agentData) throws Exception {
        queueLock=lock;
        this.queue = codeQueue;
        this.dmData = dmData;
        this.agentData=agentData;
    }


    @Override
    public void run() {

        //take task from server


        while (!toStop) {
            this.queueLock.lock();
            this.takeTaskFromServer();
            //wait until empty
            this.queueLock.checkIfLocked();
            System.out.println("Quo is empty");

            //send candidates to server
            //to be continued
        }


    }

    private void takeTaskFromServer() {
        String finalUrl = HttpUrl
                .parse(CANDIDATES)
                .newBuilder()
                .addQueryParameter("ally",agentData.getAgentAlly())
                .addQueryParameter("task", String.valueOf(agentData.getTaskSize()))
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful())
                {
                    System.out.println("producer took code from server");
                    TaskDataDTO taskDataDTO =GSON_INSTANCE.fromJson(response.body().string(), TaskDataDTO.class);
                    putTaskInQueue(taskDataDTO);
                }
            }
        });

    }
    private void putTaskInQueue(TaskDataDTO dataDTO)
    {
        dmData.setAssignmentSize(dataDTO.getTaskSize());
        CodeSettingDTO[] codeSettingDTO = dataDTO.getCodeSettingDTO();
        for (int i = 0; i <codeSettingDTO.length ; i++) {
            CodeConfiguration codeConfiguration=new CodeConfiguration(codeSettingDTO[i]);

            try {
                queue.put(codeConfiguration);
                this.amountOfCodeItook++;
                System.out.println("amount i put "+this.amountOfCodeItook);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void chooseDifficulty() throws InterruptedException {
        /*    System.out.println("starting push code");*/
/*        switch (dmData.getDifficulty()) {
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
        }*/
        isDone = true;
        System.out.println("Producer Done");
    }





    public void stop() {
        this.toStop = true;
    }
}
