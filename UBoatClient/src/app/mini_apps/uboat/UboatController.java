package app.mini_apps.uboat;

import app.mini_apps.uboat.bodies.ConfigurationController;
import app.mini_apps.uboat.bodies.EncryptController;
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

import java.net.URL;
import java.util.*;

public class UboatController implements Initializable {

    private MachineManager machineManager=new MachineManager();
    @FXML private VBox headerComponent;
    @FXML private CheckMenuItem animationButton;
    @FXML private HeaderController headerComponentController;
    @FXML private GridPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;


    public static final String ARMY_CSS = "/resources/css/army.css";
    public static final String NORMAL_CSS = "/resources/css/app.css";


    private List<CodeHolder> codeHolders=new ArrayList<>();
    private final List<MainAppScene> mainAppScenes = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        addControllerToArray();



        encryptComponent.setVisible(false);


        headerComponentController.setMachineManager(machineManager);
        mainAppScenes.forEach(mainAppScene -> mainAppScene.setMainAppController(this));

    }
    @FXML
    void enabledAnimation(ActionEvent event)
    {
            encryptComponentController.enableAnimation(animationButton.isSelected());
    }
    @FXML
    void armyCss(ActionEvent event)
    {
        encryptComponent.getScene().getStylesheets().clear();
        encryptComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(ARMY_CSS)));

    }

    @FXML
    void plainCss(ActionEvent event)
    {
        encryptComponent.getScene().getStylesheets().clear();
        encryptComponent.getScene().getStylesheets().add(String.valueOf(getClass().getResource(NORMAL_CSS)));
    }

    private void addControllerToArray() {
        codeHolders.add(configurationComponentController);
        codeHolders.add(encryptComponentController);

        mainAppScenes.add(headerComponentController);
        mainAppScenes.add(configurationComponentController);
        mainAppScenes.add(encryptComponentController);
    }

    public void updateMachineInformation()
    {
        configurationComponentController.updateMachineInformation();
    }

    public void displayMachineConfigScene() {
        setAllPagesVisibilityToFalse();
        this.configurationComponent.setVisible(true);
    }
    public void displayEncrypt() {
        setAllPagesVisibilityToFalse();
        this.encryptComponent.setVisible(true);
    }


    private void setAllPagesVisibilityToFalse()
    {
        this.configurationComponent.setVisible(false);
        this.encryptComponent.setVisible(false);

    }

    public void updateAllControllers() {
        mainAppScenes.forEach(mainAppScene -> {
            mainAppScene.setMachineManager(machineManager);
            mainAppScene.setMachineInformation(machineManager.getMachineInformation());
        });
        this.updateMachineInformation();
        this.encryptComponentController.updateInitialDictionaryTable();

    }



    public void updateMachineCode(String currentCodeSetting) {
        codeHolders.forEach(codeHolder -> codeHolder.updateCode(currentCodeSetting));
    }
    public void setInitialCode(String code) {
        encryptComponentController.addCodeToComboBox(code);
        updateMachineCode(code);
    }

    public void enableEncrypt()
    {
        headerComponentController.enableEncrypt(true);
        this.encryptComponentController.startListRefresher();
    }


    //called when new machine is loaded
    public void resetAll() {
        this.displayMachineConfigScene();
        this.clearEncryptText();

        headerComponentController.enableEncrypt(false);

        configurationComponentController.resetCode();
        configurationComponentController.resetInformation();
        configurationComponentController.disableConfigButtons();

    }
    public void clearEncryptText()
    {
        this.encryptComponentController.clearText();
    }



    public void updateTotalWordEncrypted(int processedInputCounter) {
        this.configurationComponentController.updateTotalEncryptedWord(processedInputCounter);
    }

    public void updateMachineInformation(MachineInformation machineInformationFromServer) {
        configurationComponentController.updateMachineInformation(machineInformationFromServer);
    }
    public void startRefresh()
    {

    }
}
