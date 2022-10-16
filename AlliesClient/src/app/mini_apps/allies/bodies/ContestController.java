package app.mini_apps.allies.bodies;

import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import engine.enigma.battlefield.BattleFieldInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;

public class ContestController extends MainAppScene {

    @FXML
    private Label battleFieldName;

    @FXML
    private Label difficulty;

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

    @FXML
    private Label totalTaskDone;

    public void updateBattleInfo(BattleFieldInfo joinedBattle) {
        difficulty.setText(joinedBattle.getLevel().toString());
        battleFieldName.setText(joinedBattle.getBattleName());
    }
}
