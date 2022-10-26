package app.mini_apps.agent.bodies;

import DTO.AgentData;
import DTO.BattleFieldInfoDTO;
import DTO.BattleStatusDTO;
import app.mini_apps.agent.bodies.absractScene.MainAppScene;
import app.mini_apps.agent.bodies.refreshers.BattleInfoRefresher;
import app.mini_apps.agent.bodies.refreshers.BattleStatusRefresher;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.machineutils.MachineManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static web.Constants.REFRESH_RATE;
import static web.Constants.UPLOAD_FILE;

public class ContestController extends MainAppScene {

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

    @FXML
    private Label totalTaskDone;

    private Timer timer=new Timer();

    private TimerTask battleStatusRefresher;
    private TimerTask battleInfoRefresher;


    private MachineManager machineManager=new MachineManager();



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
                    System.out.println("Yay");
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
            Platform.runLater(()->
            {
                System.out.println("ally app yay :D");
                this.encryptedMessage.setText(battleStatusDTO.getEncryptedMessage());
            });
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
            });
        }


    }
}
