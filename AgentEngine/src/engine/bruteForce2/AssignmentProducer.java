package engine.bruteForce2;


import DTO.*;
import engine.bruteForce2.utils.CandidateList;
import engine.bruteForce2.utils.CodeConfiguration;
import engine.bruteForce2.utils.QueueData;
import engine.bruteForce2.utils.QueueLock;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static web.Constants.*;

public class AssignmentProducer implements Runnable {

    private final QueueLock queueLock;
    private CandidateList candidateList;
    private BlockingQueue<CodeConfiguration> queue;
    private DMData dmData;
    private boolean toStop = false;
    private final AgentData agentData;
    private int lastIndexSendToServer=0;
    private QueueData queueData;
    private boolean firstTimeTakingTask=true;

    public AssignmentProducer(BlockingQueue<CodeConfiguration> codeQueue, DMData dmData, QueueLock lock, AgentData agentData, CandidateList candidateList, QueueData queueData) throws Exception {
        queueLock=lock;
        this.queue = codeQueue;
        this.dmData = dmData;
        this.agentData=agentData;
        this.candidateList=candidateList;
        this.queueData=queueData;
    }


    @Override
    public void run() {
        while (!toStop) {
            this.queueLock.lock();
            this.takeTaskFromServer();
            //wait until empty
            this.queueLock.checkIfLocked();
            if(toStop)
            {
                return;
            }
            this.sendTaskToServer();
            //send candidates to server

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
                .parse(CONFIGURATION_TASKS)
                .newBuilder()
                .addQueryParameter("ally",agentData.getAgentAlly())
                .addQueryParameter("task", String.valueOf(agentData.getTaskSize()))
                .addQueryParameter("taskDone", String.valueOf(firstTimeTakingTask? 0:agentData.getTaskSize()))
                .build()
                .toString();
        firstTimeTakingTask=false;
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful())
                {
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
        queueData.increaseTaskLeft(codeSettingDTO.length);
        for (int i = 0; i <codeSettingDTO.length ; i++) {
            CodeConfiguration codeConfiguration=new CodeConfiguration(codeSettingDTO[i]);

            try {

                queue.put(codeConfiguration);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        queueData.increaseTaskPulled(codeSettingDTO.length);


    }
    
    public void stop() {
        this.toStop = true;
    }
}
