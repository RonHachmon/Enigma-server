package app.mini_apps.agent;

import DTO.AgentData;
import app.mini_apps.agent.bodies.absractScene.MainAppScene;

import app.mini_apps.agent.bodies.ContestController;
import app.mini_apps.agent.header.HeaderController;

import engine.machineutils.MachineManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.*;

public class AgentController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;

    @FXML private HeaderController headerComponentController;

    @FXML private GridPane contestComponent;
    @FXML private ContestController contestComponentController;


    public static final String ARMY_CSS = "/resources/css/army.css";
    public static final String NORMAL_CSS = "/resources/css/app.css";



    private final List<MainAppScene> mainAppScenes = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        addControllerToArray();
        headerComponentController.setMachineManager(machineManager);
        mainAppScenes.forEach(mainAppScene -> mainAppScene.setMainAppController(this));

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
        mainAppScenes.add(contestComponentController);
    }

    public void setAgentData(AgentData agentData) {
        this.contestComponentController.setAgentData(agentData);
        contestComponentController.startBattleInfoRefresher();
    }




}
