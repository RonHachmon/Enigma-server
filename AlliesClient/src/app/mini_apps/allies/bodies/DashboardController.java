package app.mini_apps.allies.bodies;




import DTO.AgentData;
import DTO.BattleFieldInfoDTO;
import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import app.mini_apps.allies.refreshers.AgentListRefresher;
import app.mini_apps.allies.refreshers.BattleListRefresher;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.DifficultyLevel;
import web.http.HttpClientUtil;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static web.Constants.*;

public class DashboardController extends MainAppScene implements Initializable {

    @FXML
    private TableView<AgentData> agentsTable;

    @FXML
    private TableColumn<AgentData, String> agentColumn;

    @FXML
    private TableColumn<AgentData, Integer> threadsColumn;

    @FXML
    private TableColumn<AgentData, Integer> taskColumn;






    //-------------------------------------------------

    @FXML
    private TableView<BattleFieldInfoDTO> contestTable;

    @FXML
    private TableColumn<BattleFieldInfoDTO, String> battlefieldColumn;

    @FXML
    private TableColumn<BattleFieldInfoDTO, String> uboatColumn;

    @FXML
    private TableColumn<BattleFieldInfoDTO, String> statusColumn;

    @FXML
    private TableColumn<BattleFieldInfoDTO, DifficultyLevel> difficultyColumn;

    @FXML
    private TableColumn<BattleFieldInfoDTO, String> signedColumn;
    @FXML
    private TextField battleTextField;

    @FXML
    private Button readyButton;

    private Timer timer;
    private TimerTask battleListRefresher;
    private TimerTask agentListRefresher;
    private BattleFieldInfoDTO joinedBattle=null;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContestTable();
        setAgentsTable();
        
    }



    private void setAgentsTable() {
        agentColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        threadsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfThreads"));
    }
    private void setContestTable() {
        battlefieldColumn.setCellValueFactory(new PropertyValueFactory<>("battleName"));
        uboatColumn.setCellValueFactory(new PropertyValueFactory<>("uboatName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        signedColumn.setCellValueFactory(new PropertyValueFactory<>("currentAndRequiredRatio"));
    }
    @FXML
    void selectedBattle(MouseEvent event) {
        BattleFieldInfoDTO selectedItem = contestTable.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2&&selectedItem!=null) {
            if(!selectedItem.isFull()) {
                if(!selectedItem.getStatus().equals("ended")) {

                    if (this.joinedBattle != null) {
                        //if already joined chosen battle
                        if (this.joinedBattle.getBattleName().equals(selectedItem.getBattleName())) {
                            return;
                        }
                        unjoinBattle(joinedBattle.getBattleName());
                    }
                    this.joinedBattle = selectedItem;
                    this.joinBattle(joinedBattle.getBattleName());
                }
                else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("war ended");
                    a.setTitle("War ended");
                    a.show();

                }
            }
            else
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("war is full");
                a.setTitle("War is full");
                a.show();
            }
        }

    }
    private void unjoinBattle(String battleName) {
        battleName = battleName.replace(' ', '-');

        String finalUrl = HttpUrl
                .parse(UNJOIN_BATTLE)
                .newBuilder()
                .addQueryParameter("battleship", battleName)
                .addQueryParameter("entity","ally")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
            }
        });
    }

    private void joinBattle(String battleName) {
        battleName = battleName.replace(' ', '-');

        String finalUrl = HttpUrl
                .parse(JOIN_BATTLE)
                .newBuilder()
                .addQueryParameter("battleship", battleName)
                .addQueryParameter("entity","ally")
                .build()
                .toString();

        String finalBattleName = battleName;
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
                else
                {
                    Platform.runLater(()->
                    {
                        battleTextField.setText(finalBattleName);
                        readyButton.setDisable(false);

                    });
                }
            }
        });
    }


    private void updateContestList(List<BattleFieldInfoDTO> contestDetails) {
        Platform.runLater(() -> {
            contestTable.getItems().clear();
            contestTable.setItems(FXCollections.observableList(contestDetails));
        });
    }
    private void updateAgentList(List<AgentData> agentData) {
        Platform.runLater(() -> {
            agentsTable.getItems().clear();
            agentsTable.setItems(FXCollections.observableList(agentData));
        });
    }

    public void startListRefresher() {
        battleListRefresher = new BattleListRefresher(this::updateContestList);
        agentListRefresher = new AgentListRefresher(this::updateAgentList);
        timer = new Timer();
        timer.schedule(battleListRefresher, 200, REFRESH_RATE);
        timer.schedule(agentListRefresher, 200, REFRESH_RATE);
    }


    public void readyPressed(ActionEvent actionEvent) {
        this.alliesController.showContest(this.joinedBattle);

    }


    public void reset() {
        this.battleTextField.setText("");
        this.joinedBattle=null;
    }

    public void close() {
        this.timer.cancel();
    }
}
