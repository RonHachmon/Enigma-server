package app.mini_apps.uboat;

import app.UBoatAppMainController;
import app.mini_apps.uboat.bodies.ConfigurationController;
import app.mini_apps.uboat.bodies.ContestController;
import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import app.mini_apps.uboat.header.HeaderController;
import engine.enigma.machineutils.MachineInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import engine.enigma.machineutils.MachineManager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UboatController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;
    @FXML private CheckMenuItem animationButton;
    @FXML private HeaderController headerComponentController;
    @FXML private GridPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;
    @FXML private GridPane contestComponent;
    @FXML private ContestController contestComponentController;


    public static final String ARMY_CSS = "/resources/css/army.css";
    public static final String NORMAL_CSS = "/resources/css/app.css";


    private List<CodeHolder> codeHolders=new ArrayList<>();
    private final List<MainAppScene> mainAppScenes = new ArrayList<>();
    private UBoatAppMainController mainApp;

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
        codeHolders.add(configurationComponentController);
        codeHolders.add(contestComponentController);

        mainAppScenes.add(headerComponentController);
        mainAppScenes.add(configurationComponentController);
        mainAppScenes.add(contestComponentController);
    }

    public void updateMachineInformation()
    {
        configurationComponentController.updateMachineInformation();
    }

    public void displayMachineConfigScene() {
        setAllPagesVisibilityToFalse();
        this.configurationComponent.setVisible(true);
    }
    public void displayContest() {
        setAllPagesVisibilityToFalse();
        this.contestComponent.setVisible(true);
    }


    private void setAllPagesVisibilityToFalse()
    {
        this.configurationComponent.setVisible(false);
        this.contestComponent.setVisible(false);

    }

    public void updateAllControllers() {
        mainAppScenes.forEach(mainAppScene -> {
            mainAppScene.setMachineManager(machineManager);
            mainAppScene.setMachineInformation(machineManager.getMachineInformation());
        });
        this.updateMachineInformation();
        this.contestComponentController.updateInitialDictionaryTable();

    }



    public void updateMachineCode(String currentCodeSetting) {
        codeHolders.forEach(codeHolder -> codeHolder.updateCode(currentCodeSetting));
    }
    public void setInitialCode(String code) {
        updateMachineCode(code);
    }

    public void enableContest()
    {
        headerComponentController.enableContest(true);
        this.contestComponentController.startListRefresher();
    }


    //called when new machine is loaded
    public void resetAll() {
        this.displayMachineConfigScene();
        this.clearEncryptText();

        headerComponentController.enableContest(false);

        configurationComponentController.resetCode();
        configurationComponentController.resetInformation();
        configurationComponentController.disableConfigButtons();

    }
    public void clearEncryptText()
    {
        this.contestComponentController.clearText();
    }


    public void close() {
        this.configurationComponentController.resetCode();
        this.configurationComponentController.resetInformation();
        this.headerComponentController.reset();
        this.machineManager=null;
        try {
            this.contestComponentController.close();
            mainApp.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setMainAppController(UBoatAppMainController uBoatAppMainController) {
        this.mainApp=uBoatAppMainController;
    }

    public void setTitle(String uboatName) {
        this.headerComponentController.setUboatName(uboatName);
    }

    public void shutdown() {
        this.contestComponentController.shutdown();
    }
}
