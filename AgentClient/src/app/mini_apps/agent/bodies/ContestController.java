package app.mini_apps.agent.bodies;

import DTO.*;
import app.mini_apps.agent.bodies.absractScene.MainAppScene;
import app.mini_apps.agent.bodies.refreshers.BattleInfoRefresher;
import app.mini_apps.agent.bodies.refreshers.BattleStatusRefresher;


import app.mini_apps.agent.utils.FindCandidateTask;
import app.mini_apps.agent.utils.UIAdapter;
import app.mini_apps.agent.utils.candidate.CandidateController;
import app.mini_apps.agent.winner.WinnerController;
import engine.bruteForce2.utils.QueueData;
import engine.machineutils.MachineManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.EnigmaUtils;
import web.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static web.Constants.REFRESH_RATE;
import static web.Constants.UPLOAD_FILE;

public class ContestController extends MainAppScene implements Closeable {

    public static final String WORD_CANDIDATE_FXML = "/app/mini_apps/agent/smallComponent/wordCandidate.fxml";
    @FXML
    private Label battleFieldName;

    @FXML
    private Label difficulty;

    @FXML
    private Label encryptedMessage;

    @FXML
    private Label allyLabel;

    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private Label battleStatus;

    @FXML
    private Label taskLeft;

    @FXML
    private Label taskDone;

    @FXML
    private Label taskPulled;

    @FXML
    private Label candidatesFound;

    private AgentData agentData;
    private DMData dmData = new DMData();



    private MachineManager machineManager = null;
    private FindCandidateTask currentRunningTask;
    private boolean battleStarted = false;
    private WinnerController winnerController;
    private String currentBattleStatus = null;


    private BattleStatusRefresher battleStatusRefresher;
    private TimerTask battleInfoRefresher;
    private Timer timer = new Timer();

    public void setAgentData(AgentData agentData) {
        this.agentData = agentData;
        this.allyLabel.setText(agentData.getAgentAlly());
        dmData.setAmountOfAgents(agentData.getNumberOfThreads());
    }

    public void getBattleFile() {

        String agentAlly = this.agentData.getAgentAlly();
        HttpClientUtil.runAsync(UPLOAD_FILE + "?ally=" + agentAlly, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    machineManager = new MachineManager();
                    machineManager.createMachineFromXML(response.body().byteStream());
                    startBattleStatusRefresher();
                }
            }
        });
    }

    public void startBattleInfoRefresher() {

        battleInfoRefresher = new BattleInfoRefresher(this::updateBattleInfo, this.allyLabel.getText());
        startTimer(battleInfoRefresher);
    }

    private void updateBattleInfo(BattleFieldInfoDTO battleFieldInfoDTO) {
        if (battleFieldInfoDTO != null) {
            battleInfoRefresher.cancel();
            this.getBattleFile();
            Platform.runLater(() ->
            {
                battleStatus.setText("IDLE");
                this.battleFieldName.setText(battleFieldInfoDTO.getBattleName());
                this.difficulty.setText(battleFieldInfoDTO.getLevel().toString());
                dmData.setDifficulty(battleFieldInfoDTO.getLevel());
            });
        }
    }

    private void startBattleStatusRefresher() {

        battleStatusRefresher = new BattleStatusRefresher(this::updateByStatus, this.battleFieldName.getText());
        startTimer(battleStatusRefresher);
    }

    private void updateByStatus(BattleStatusDTO battleStatusDTO) {
        if (currentBattleStatus == null || !battleStatusDTO.getStatus().equals(currentBattleStatus)) {
            currentBattleStatus = battleStatusDTO.getStatus();
            if (battleStatusDTO.getStatus().equals("In Progress")) {
                if (!this.battleStarted) {
                    this.battleStarted = true;
                    Platform.runLater(() ->
                    {
                        this.encryptedMessage.setText(battleStatusDTO.getEncryptedMessage());
                        dmData.setEncryptedString(battleStatusDTO.getEncryptedMessage());
                        currentRunningTask = new FindCandidateTask(dmData, createUIAdapter(), this, this.machineManager, agentData);
                        new Thread(currentRunningTask).start();
                        battleStatus.setText("IN PROGRESS");
                    });
                }
            }
            if (battleStatusDTO.getStatus().equals("Finished")) {
                currentRunningTask.stop();
                battleStatusRefresher.cancel();
                Platform.runLater(() ->
                {

                    battleStatus.setText("FINISHED");
                    this.battleStarted = false;
                    this.machineManager = null;
                    battleStatusRefresher.Stop(true);
                    showWinner(battleStatusDTO.getWinningAlly());

                });
            }
            if (battleStatusDTO.getStatus().equals("Idle")) {
                battleStatus.setText("IDLE");

            }
            if (battleStatusDTO == null) {

                Platform.runLater(() ->
                {
                    this.reset();
                });
            }
        }
    }

    private void reset() {
        this.resetProgress();
        battleStatus.setText("");
        candidatesFlowPane.getChildren().clear();
        this.difficulty.setText("");
        this.battleFieldName.setText("");
        this.encryptedMessage.setText("");
        this.currentRunningTask = null;
        this.startBattleInfoRefresher();
    }

    private void resetProgress() {
        this.taskPulled.setText("");
        this.candidatesFound.setText("");
        this.taskDone.setText("");
        this.taskLeft.setText("");
    }


    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                decryptionCandidateData -> {
                    /*createWordCandidate(DecryptionCandidate);*/
                    createWordCandidate(decryptionCandidateData);
                },
                (queueData) -> {
                    this.updateProgressData(queueData);

                    /*this.totalFoundCandidate.set(delta);*/

                },
                () -> {
                    /*  this.totalFoundCandidate.set(totalFoundCandidate.get() + 1);*/
                },
                //on done
                () -> {
                    this.currentRunningTask.stop();
                },
                (string) -> {
                    /*  this.taskDone.setText(string);*/
                }
        );
    }
    private void updateProgressData(QueueData queueData)
    {

        this.taskDone.setText(EnigmaUtils.formatToIntWithCommas(queueData.getTaskDone()));
        this.taskLeft.setText(EnigmaUtils.formatToIntWithCommas(queueData.getTaskLeft()));
        this.taskPulled.setText(EnigmaUtils.formatToIntWithCommas(queueData.getTaskPulled()));
    }
    private void createWordCandidate(DecryptionCandidate decryptionCandidate) {

        try {
            Node wordCandidate = loadCandidate(decryptionCandidate);

            FlowPane.setMargin(wordCandidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(wordCandidate);
            this.candidatesFound.setText(String.valueOf(candidatesFlowPane.getChildren().size()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node loadCandidate(DecryptionCandidate decryptionCandidate) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(WORD_CANDIDATE_FXML));
        Node wordCandidate = loader.load();
        wordCandidate.setFocusTraversable(false);

        CandidateController wordCandidateController = loader.getController();
        wordCandidateController.setTextFont();
        wordCandidateController.setText(decryptionCandidate.getDecryptedString());
        wordCandidateController.setAgent(String.valueOf(decryptionCandidate.getAgentID()));
        wordCandidateController.setCode(decryptionCandidate.getCodeConfiguration());
        return wordCandidate;
    }

    private void showWinner(String allyName) {
        try {
            Stage settingStage = loadWinnerStage();

            this.winnerController.setWinnerLabel(allyName);
            settingStage.initModality(Modality.WINDOW_MODAL);
            settingStage.showAndWait();
            this.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stage loadWinnerStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/mini_apps/agent/winner/winner.fxml"));

        AnchorPane anchorPane = loader.load();
        this.winnerController = loader.getController();
        Scene scene = new Scene(anchorPane, 455, 100);
        Stage settingStage = new Stage();
        settingStage.setScene(scene);
        settingStage.setTitle("Agent : winner");
        return settingStage;
    }

    private void startTimer(TimerTask timerTask) {
        timer.schedule(timerTask, 200, REFRESH_RATE);
    }

    @Override
    public void close() throws IOException {
        this.timer.cancel();
    }
}
