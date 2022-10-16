package app.mini_apps.allies;

import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import app.mini_apps.allies.bodies.interfaces.CodeHolder;
import app.mini_apps.allies.bodies.DashboardController;
import app.mini_apps.allies.bodies.ContestController;
import app.mini_apps.allies.header.HeaderController;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.machineutils.MachineInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import engine.enigma.machineutils.MachineManager;

import java.net.URL;
import java.util.*;

public class AlliesController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;
    @FXML private CheckMenuItem animationButton;
    @FXML private HeaderController headerComponentController;
    @FXML private GridPane dashboardComponent;
    @FXML private DashboardController dashboardComponentController;
    @FXML private GridPane contestComponent;
    @FXML private ContestController contestComponentController;


    public static final String ARMY_CSS = "/resources/css/army.css";
    public static final String NORMAL_CSS = "/resources/css/app.css";



    private final List<MainAppScene> mainAppScenes = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        addControllerToArray();



        contestComponent.setVisible(false);


        headerComponentController.setMachineManager(machineManager);
        mainAppScenes.forEach(mainAppScene -> mainAppScene.setMainAppController(this));

    }
    @FXML
    void enabledAnimation(ActionEvent event)
    {

    }
    @FXML
    void armyCss(ActionEvent event)
    {
        contestComponent.getScene().getStylesheets().clear();
        contestComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(ARMY_CSS)));

    }

    @FXML
    void plainCss(ActionEvent event)
    {
        contestComponent.getScene().getStylesheets().clear();
        contestComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(NORMAL_CSS)));
    }

    private void addControllerToArray() {


        mainAppScenes.add(headerComponentController);
        mainAppScenes.add(dashboardComponentController);
        mainAppScenes.add(contestComponentController);
    }



    public void displayMachineConfigScene() {
        setAllPagesVisibilityToFalse();
        this.dashboardComponent.setVisible(true);
    }
    public void displayEncrypt() {
        setAllPagesVisibilityToFalse();
        this.contestComponent.setVisible(true);
    }


    private void setAllPagesVisibilityToFalse()
    {
        this.contestComponent.setVisible(false);
        this.dashboardComponent.setVisible(false);

    }


    public void startRefresh() {
        this.dashboardComponentController.startListRefresher();
    }

    public void showContest(BattleFieldInfo joinedBattle) {
        this.displayEncrypt();
        this.contestComponentController.updateBattleInfo(joinedBattle);

    }
}
