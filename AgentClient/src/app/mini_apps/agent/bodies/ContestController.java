package app.mini_apps.agent.bodies;

import DTO.*;
import app.mini_apps.agent.bodies.absractScene.MainAppScene;
import app.mini_apps.agent.bodies.refreshers.BattleInfoRefresher;
import app.mini_apps.agent.bodies.refreshers.BattleStatusRefresher;


import app.mini_apps.agent.utils.FindCandidateTask;
import app.mini_apps.agent.utils.UIAdapter;
import app.mini_apps.agent.utils.candidate.CandidateController;
import engine.bruteForce2.DecryptManager;
import engine.machineutils.MachineManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.DifficultyLevel;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static web.Constants.REFRESH_RATE;
import static web.Constants.UPLOAD_FILE;

public class ContestController extends MainAppScene {

    public static final String WORD_CANDIDATE_FXML = "/app/mini_apps/agent/smallComponent/wordCandidate.fxml";
    @FXML
    private Label battleFieldName;

    @FXML
    private Label difficulty;
    @FXML
    private Label allyLabel;

    @FXML
    private Label encryptedMessage;

    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private TableView<?> alliesTable;

    @FXML
    private TableColumn<?, ?> alliesColumn;

    @FXML
    private TableColumn<?, ?> totalAgentColumn;

    @FXML
    private TableColumn<?, ?> taskSizeColumn;

    private AgentData agentData;
    private DMData dmData=new DMData();

    @FXML
    private Label totalTaskDone;

    private Timer timer=new Timer();

    private TimerTask battleStatusRefresher;
    private TimerTask battleInfoRefresher;


    private MachineManager machineManager=new MachineManager();
    private DecryptManager decryptManager;
    private FindCandidateTask currentRunningTask;
    private boolean battleStarted=false;


    public void setAgentData(AgentData agentData) {
        this.agentData=agentData;
        this.allyLabel.setText(agentData.getAgentAlly());
    }

    public void getBattleFile() {
        String agentAlly = this.agentData.getAgentAlly();
        HttpClientUtil.runAsync(UPLOAD_FILE+"?ally="+agentAlly, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful())
                {
                    machineManager.createMachineFromXML(response.body().byteStream());
                    System.out.println("ContestController- recived file");
                }

            }
        });
    }
    public void startBattleInfoRefresher() {

        battleInfoRefresher =new BattleInfoRefresher(this::updateBattleInfo,this.allyLabel.getText());
        startTimer(battleInfoRefresher);
    }
    private void startBattleStatusRefresher() {

        battleStatusRefresher =new BattleStatusRefresher(this::updateByStatus,this.battleFieldName.getText());
        startTimer(battleStatusRefresher);
    }
    private void startTimer(TimerTask timerTask) {
        timer.schedule(timerTask, 200, REFRESH_RATE);
    }
    private void updateByStatus(BattleStatusDTO battleStatusDTO) {


        if(battleStatusDTO.getStatus().equals("In Progress"))
        {
            if(!this.battleStarted) {
                this.battleStarted=true;
                Platform.runLater(() ->
                {

                    System.out.println("Contest controller - updater by status, battle in progress");
                    this.encryptedMessage.setText(battleStatusDTO.getEncryptedMessage());
                    dmData.setEncryptedString(battleStatusDTO.getEncryptedMessage());
                    currentRunningTask = new FindCandidateTask(dmData, createUIAdapter(), this, this.machineManager, agentData);
                    new Thread(currentRunningTask).start();
                });
            }
        }
        if(battleStatusDTO.getStatus().equals("Finished"))
        {

        }
        if(battleStatusDTO.getStatus().equals("Idle"))
        {

        }

    }
    private void updateBattleInfo(BattleFieldInfoDTO battleFieldInfoDTO) {
        if(battleFieldInfoDTO!=null) {
            Platform.runLater(() ->
            {
                this.battleFieldName.setText(battleFieldInfoDTO.getBattleName());
                this.difficulty.setText(battleFieldInfoDTO.getLevel().toString());
                startBattleStatusRefresher();
                dmData.setDifficulty(battleFieldInfoDTO.getLevel());
                dmData.setAmountOfAgents(agentData.getNumberOfThreads());

            });
        }
    }
    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                decryptionCandidateData -> {
                    System.out.println("UI adapter in contest");
                    /*createWordCandidate(DecryptionCandidate);*/
                    createWordCandidate(decryptionCandidateData);
                },
                (delta) -> {
                    /*this.totalFoundCandidate.set(delta);*/

                },
                () -> {
                  /*  this.totalFoundCandidate.set(totalFoundCandidate.get() + 1);*/
                },
                //on done
                () -> {

                    this.currentRunningTask.stop();




                },
                (string) ->{
                  /*  this.taskDone.setText(string);*/
                }



        );
    }
    private void createWordCandidate(DecryptionCandidate decryptionCandidate) {

        try {
            System.out.println("contestCont new candidate");
            Node wordCandidate = loadCandidate(decryptionCandidate);

            FlowPane.setMargin(wordCandidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(wordCandidate);

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

}
