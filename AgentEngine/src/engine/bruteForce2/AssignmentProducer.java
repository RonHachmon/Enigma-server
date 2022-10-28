package engine.bruteForce2;


import DTO.*;
import engine.bruteForce2.utils.CandidateList;
import engine.bruteForce2.utils.CodeConfiguration;
import engine.bruteForce2.utils.QueueLock;

import okhttp3.*;
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

import static web.Constants.*;

public class AssignmentProducer implements Runnable {

    public static boolean isDone = false;
    private final QueueLock queueLock;
    private CandidateList candidateList;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private boolean toStop = false;
    private final AgentData agentData;
    private int amountOfCodeItook=0;
    private int lastIndexSendToServer=0;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, QueueLock lock, AgentData agentData, CandidateList candidateList) throws Exception {
        queueLock=lock;
        this.queue = codeQueue;
        this.dmData = dmData;
        this.agentData=agentData;
        this.candidateList=candidateList;
    }


    @Override
    public void run() {

        //take task from server


        while (!toStop) {
            this.queueLock.lock();
            this.takeTaskFromServer();
            //wait until empty
            this.queueLock.checkIfLocked();
            this.sendTaskToServer();
            System.out.println("Quo is empty");

            //send candidates to server
            //to be continued
        }


    }

    private  void sendTaskToServer() {
        if(candidateList.getSize()>this.lastIndexSendToServer)
        {
            DecryptionCandidate decryptionCandidate[]=new DecryptionCandidate[candidateList.getSize()-this.lastIndexSendToServer];
            int newArrayIndex=0;
            for (int i = lastIndexSendToServer; i < candidateList.getSize(); i++) {
                decryptionCandidate[newArrayIndex]=candidateList.getList().get(i);
                decryptionCandidate[newArrayIndex].setAllyName(agentData.getAgentAlly());
                decryptionCandidate[newArrayIndex].setAgentName(agentData.getAgentName());
                newArrayIndex++;
            }
            this.lastIndexSendToServer=candidateList.getSize();
            this.sendCandidatesToServer(decryptionCandidate);
        }
    }

    private void sendCandidatesToServer(DecryptionCandidate[] decryptionCandidate) {
        String finalUrl = HttpUrl
                .parse(CANDIDATES)
                .newBuilder()
                .addQueryParameter("ally",agentData.getAgentAlly())
                .build()
                .toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), GSON_INSTANCE.toJson(decryptionCandidate));
        HttpClientUtil.runAsyncWithBody(finalUrl,new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        },body);
    }

    private void takeTaskFromServer() {
        String finalUrl = HttpUrl
                .parse(TASK)
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    
    public void stop() {
        this.toStop = true;
    }
}
