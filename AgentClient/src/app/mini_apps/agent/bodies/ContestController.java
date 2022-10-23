package app.mini_apps.agent.bodies;

import DTO.AgentData;
import app.mini_apps.agent.bodies.absractScene.MainAppScene;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.machineutils.MachineManager;
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


    private MachineManager machineManager=new MachineManager();

    public void updateBattleInfo(BattleFieldInfo joinedBattle) {
        difficulty.setText(joinedBattle.getLevel().toString());
        battleFieldName.setText(joinedBattle.getBattleName());
    }

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
}
