package app.mini_apps.allies.bodies;

import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import app.mini_apps.allies.refreshers.BattleListRefresher;
import app.util.http.HttpClientUtil;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.bruteForce2.utils.DifficultyLevel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static app.util.Constants.*;
import static app.util.Constants.JOIN_BATTLE;

public class DashboardController extends MainAppScene implements Initializable {

    @FXML
    private TableView<?> agentsTable;

    @FXML
    private TableColumn<?, ?> agentColumn;

    @FXML
    private TableColumn<?, ?> threadsColumn;

    @FXML
    private TableColumn<?, ?> taskColumn;






    //-------------------------------------------------

    @FXML
    private TableView<BattleFieldInfo> contestTable;

    @FXML
    private TableColumn<BattleFieldInfo, String> battlefieldColumn;

    @FXML
    private TableColumn<BattleFieldInfo, String> uboatColumn;

    @FXML
    private TableColumn<BattleFieldInfo, String> statusColumn;

    @FXML
    private TableColumn<BattleFieldInfo, DifficultyLevel> difficultyColumn;

    @FXML
    private TableColumn<BattleFieldInfo, String> signedColumn;

    private Timer timer;
    private TimerTask listRefresher;
    private BattleFieldInfo joinedBattle=null;

    @FXML
    private TextField battleTextField;

    @FXML
    private Button readyButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContestTable();
        
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

        if (event.getClickCount() == 2) {
            if(!contestTable.getSelectionModel().getSelectedItem().isFull()) {

                if (this.joinedBattle != null) {
                    //if already joined chosen battle
                    if (this.joinedBattle.getBattleName().equals(contestTable.getSelectionModel().getSelectedItem().getBattleName())) {
                        return;
                    }
                    unjoinBattle(joinedBattle.getBattleName());
                }
                this.joinedBattle = contestTable.getSelectionModel().getSelectedItem();
                //validate not full
                this.joinBattle(joinedBattle.getBattleName());
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
            }
        });
    }

    private void joinBattle(String battleName) {
        battleName = battleName.replace(' ', '-');
        String url = JOIN_BATTLE +"?"+"battleship="+joinedBattle+"&"+"entity="+"ally";

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


    private void updateContestList(List<BattleFieldInfo> contestDetails) {
        Platform.runLater(() -> {
            contestTable.getItems().clear();
            contestTable.setItems(FXCollections.observableList(contestDetails));
        });
    }

    public void startListRefresher() {
        listRefresher = new BattleListRefresher(this::updateContestList);
        timer = new Timer();
        timer.schedule(listRefresher, 200, REFRESH_RATE);
    }


    public void readyPressed(javafx.event.ActionEvent actionEvent) {
        this.alliesController.showContest(this.joinedBattle);
    }
}
