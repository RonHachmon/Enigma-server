package app.mini_apps.allies.bodies;

import DTO.AllyDTO;
import DTO.BattleFieldInfoDTO;
import DTO.BattleStatusDTO;
import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import app.mini_apps.allies.refreshers.AlliesListRefresher;
import app.mini_apps.allies.refreshers.BattleStatusRefresher;
import engine.enigma.battlefield.BattleFieldInfo;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.EnigmaUtils;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static web.Constants.*;

public class ContestController extends MainAppScene implements Initializable {

    @FXML
    private Label battleFieldName;

    @FXML
    private Label difficulty;

    @FXML
    private Label encryptedMessage;

    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private TableView<AllyDTO> alliesTable;

    @FXML
    private TableColumn<AllyDTO, String> alliesColumn;

    @FXML
    private TableColumn<AllyDTO, Integer> agentColumn;

    @FXML
    private TableColumn<AllyDTO, Integer> taskColumn;

    @FXML
    private Label totalTaskDone;
    @FXML
    private TextField taskSizeField;

    @FXML
    private Button readyButton;

    private Timer timer=new Timer();
    private TimerTask listRefresher;
    private TimerTask battleStatusRefresher;
    private Tooltip toolTipError;
    private boolean validAssignment=false;

    public void updateBattleInfo(BattleFieldInfoDTO joinedBattle) {
        difficulty.setText(joinedBattle.getLevel().toString());
        battleFieldName.setText(joinedBattle.getBattleName());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setAlliesTable();
        setToolTip();
        taskSizeField.textProperty().
                addListener((object, oldValue, newValue) -> allDataValid());
    }

    public void startListRefresher() {
        listRefresher = new AlliesListRefresher(this::updateAllies,this.battleFieldName.getText());
        startTimer(listRefresher);
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

    private void updateAllies(List<AllyDTO> alliesDetails) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            alliesTable.setItems(FXCollections.observableList(alliesDetails));
        });

    }
    @FXML
    void readyPressed(ActionEvent event) {
        readyAllyInServer(HttpUrl
                .parse(READY)
                .newBuilder());
        sendTaskToServer();


        readyButton.setDisable(true);
        taskSizeField.setDisable(true);

    }

    private void sendTaskToServer() {

        String finalUrl = HttpUrl
                .parse(UPDATE_TASK)
                .newBuilder()
                .addQueryParameter("task",taskSizeField.getText())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            }
        });
    }

    private  void readyAllyInServer(HttpUrl.Builder READY) {
        String finalUrl = READY
                .addQueryParameter("entity","ally")
                .build()
                .toString();


        //currently does nothing except sending the request, maybe we should add something in the future.
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println(":(");
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
                else
                {
                    System.out.println(":D");
                }
            }
        });
    }
    private void allDataValid() {
        if (assignmentInputValid(taskSizeField.getText())) {
            readyButton.setDisable(false);
            return;
        }
        readyButton.setDisable(true);
    }
    private void setToolTip() {
        toolTipError = new Tooltip("input must be a positive number");
        toolTipError.setId("error-tool-tip");
        //changes duration until tool tip is shown
        EnigmaUtils.hackTooltipStartTiming(toolTipError, 100);
        this.taskSizeField.setOnMouseEntered(event -> showToolTip());
        this.taskSizeField.setOnMouseExited(event -> toolTipError.hide());
    }
    private void showToolTip() {
        String currentInput = this.taskSizeField.getText();

        if (!currentInput.isEmpty() && !validAssignment) {
            renderToolTip();
        } else {
            toolTipError.hide();
        }
    }
    private boolean assignmentInputValid(String newValue) {
        if (newValue.isEmpty()) {
            taskSizeField.setId(null);
            toolTipError.hide();
        }
        else {
            if (EnigmaUtils.isNumeric(newValue)) {
                int number = Integer.parseInt(newValue);
                if(number>0) {

                    validAssignment = true;
                    toolTipError.hide();
                    return true;
                }
                else {
                    validAssignment = false;
                    renderToolTip();
                    taskSizeField.setId("error-text-field");
                }
            } else {
                validAssignment = false;
                renderToolTip();
                taskSizeField.setId("error-text-field");
            }
        }
        return false;
    }
    private void renderToolTip() {
        Bounds boundsInScene = taskSizeField.localToScreen(taskSizeField.getBoundsInLocal());
        toolTipError.show(taskSizeField, boundsInScene.getMaxX(), boundsInScene.getMaxY() + 15);
    }
    private void setAlliesTable() {
        alliesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAllyName()));
        agentColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfAgents"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));

    }
}
