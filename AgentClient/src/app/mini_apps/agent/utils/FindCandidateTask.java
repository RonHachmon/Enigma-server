package app.mini_apps.agent.utils;

import DTO.AgentData;
import DTO.DMData;
import DTO.DecryptionCandidate;

import app.mini_apps.agent.bodies.ContestController;
import app.mini_apps.agent.utils.threads.DaemonThread;

import engine.bruteForce2.DecryptManager;
import engine.bruteForce2.TaskManger;
import engine.bruteForce2.utils.QueueData;
import engine.machineutils.MachineManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import utils.EnigmaUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FindCandidateTask extends Task<Boolean> {
    private final UIAdapter uiAdapter;
    private final ContestController controller;
    private DecryptManager decryptManager;
    private int lastKnownIndex = 0;
    private ScheduledExecutorService timedExecute;

    ScheduledFuture<?> scheduledFuture;
    private SimpleStringProperty totalWork=new SimpleStringProperty();

    private long totalWorkSize = 0;

    private long totalTime=0;
    private long avgTime=0;

    public FindCandidateTask(DMData dmData, UIAdapter uiAdapter, ContestController contestController, MachineManager machineManager, AgentData agentData) {
        this.uiAdapter = uiAdapter;
        this.controller = contestController;
        /*controller.bindTaskToUIComponents(this);*/
        DaemonThread daemonThreadFactory = new DaemonThread();
        timedExecute = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);
        decryptManager =new DecryptManager(machineManager,dmData, agentData);

    }


    @Override
    protected Boolean call() throws Exception {
        TaskManger.resetStaticMembers();
        updateMessage("starting to work");
        decryptManager.startDeciphering();




        totalWorkSize = decryptManager.getTotalTaskSize();
        updateProgress(0, totalWorkSize);
      /*  uiAdapter.updateProgress("permutation: "+0+"/"+ EnigmaUtils.formatToIntWithCommas(totalWorkSize));*/
        startTimedTask();

        return null;

    }

    private void startTimedTask() {
        scheduledFuture = timedExecute.scheduleAtFixedRate(() -> update(), 100, 100, TimeUnit.MILLISECONDS);
    }


    public void pause() {

        scheduledFuture.cancel(true);

    }

    public void resume() {
        startTimedTask();
    }
    public void stop() {
        updateMessage("Cancelled ;/");
        this.cancelled();
    }

    @Override
    protected void cancelled() {
        timedExecute.shutdownNow();
        decryptManager.stop();
        super.cancelled();
        this.cancel();
        this.done();
    }

    private void update() {
        long workDone = decryptManager.getWorkDone();
        QueueData queueData = decryptManager.getQueueData();
        uiAdapter.updateProgressData(queueData);
        List<DecryptionCandidate> decryptionCandidates = decryptManager.getCandidateList().getList();
        if (decryptionCandidates.size() > lastKnownIndex) {
            for (int i = lastKnownIndex; i < decryptionCandidates.size(); i++) {
                uiAdapter.addNewCandidate(decryptionCandidates.get(i));
            }
            lastKnownIndex = decryptionCandidates.size();
        }
    }

    private void checkIfDone(long workDone) {
        if(workDone >=totalWorkSize)
        {
            setTime();
            updateMessage("Done work");
            uiAdapter.done();
            this.cancelled();
        }
    }


    private void setTime() {
        this.totalTime= decryptManager.getTotalTaskDurationInNanoSeconds();
        this.avgTime=decryptManager.getAvgTaskDuration();
    }



}
