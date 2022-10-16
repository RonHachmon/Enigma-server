package app.mini_apps.uboat.bodies;


import app.mini_apps.uboat.bodies.absractScene.MainAppScene;

import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import app.mini_apps.uboat.settings.SettingController;
import engine.enigma.machineutils.MachineInformation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigurationController extends MainAppScene implements Initializable, CodeHolder {
    public static final String SETTING_SCENE_FXML = "/app/mini_apps/uboat/settings/settingScene.fxml";
    public static final String SETTING_CSS = "/app/mini_apps/uboat/settings/setting.css";
    public static final String EMPTY = "";

    private SettingController settingController;


    @FXML
    private Button setRandomCode;

    @FXML
    private Button setCode;

    @FXML
    private Label amountOfRotors;

    @FXML
    private Label amountOfRequiredRotors;

    @FXML
    private Label amountOfReflectors;

    @FXML
    private Label amountOfProcessedInput;

    @FXML
    private Label currentCodeConfig;

    @FXML
    private Label initialCodeConfig;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    void setCodeManually(ActionEvent event) {

        try {
            Stage settingStage = loadSettingStage();
            settingStage.initModality(Modality.WINDOW_MODAL);
            settingStage.showAndWait();
            if (settingController.getIsCodeValid()) {
                this.setMachineCode();
                this.initialCodeConfig.setText(machineManager.getInitialFullMachineCode());
                this.uboatController.setInitialCode(machineManager.getInitialFullMachineCode());
                this.uboatController.enableEncrypt();
                this.uboatController.clearEncryptText();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stage loadSettingStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SETTING_SCENE_FXML));

        AnchorPane anchorPane = loader.load();
        settingController = loader.getController();
        settingController.setSetting(machineInformation);
        Scene scene = new Scene(anchorPane, 800, 600);
        Stage settingStage = new Stage();
        settingStage.setScene(scene);
        scene.getStylesheets().add(String.valueOf(getClass().getResource(SETTING_CSS)));
        settingStage.setTitle("machine setting");
        return settingStage;
    }

    private void setMachineCode() {
        List<Integer> rotorsID = new ArrayList<>();
        StringBuilder staringIndexes = new StringBuilder("");
        StringBuilder switchPlugs = new StringBuilder("");

        this.settingController.getRotorIndexes().forEach(choiceBox -> rotorsID.add(choiceBox.getValue() - 1));
        this.settingController.getRotorStartingIndexes().forEach(characterChoiceBox -> staringIndexes.append(characterChoiceBox.getValue()));
        this.settingController.getAllSwitchPlugChoiceBoxes().forEach(switchPlugsBox -> switchPlugs.append(switchPlugsBox.getValue()));

        Collections.reverse(rotorsID);

        machineManager.setSelectedRotors(rotorsID);
        machineManager.setStartingIndex(staringIndexes.reverse().toString());
        machineManager.setSelectedReflector(this.settingController.getSelectedReflector() - 1);
        if (!switchPlugs.toString().isEmpty()) {
            machineManager.setSwitchPlug(switchPlugs.toString());
        }
    }

    @FXML
    void setRandomCode(ActionEvent event) {
        new Thread(() ->
        {
            this.machineManager.autoZeroMachine();
            Platform.runLater(() ->
            {

                this.initialCodeConfig.setText(machineManager.getInitialFullMachineCode());
                this.uboatController.setInitialCode(machineManager.getInitialFullMachineCode());
                this.uboatController.enableEncrypt();
                this.uboatController.clearEncryptText();
            });

        }).start();

    }

    public void updateMachineInformation() {
        amountOfRotors.setText(String.valueOf(machineInformation.getAmountOfRotors()));
        amountOfRequiredRotors.setText(String.valueOf(machineInformation.getAmountOfRotorsRequired()));
        amountOfReflectors.setText(String.valueOf(machineInformation.getAvailableReflectors()));
    }

    public void resetInformation() {
        amountOfRotors.setText(EMPTY);
        amountOfRequiredRotors.setText(EMPTY);
        amountOfReflectors.setText(EMPTY);
        amountOfProcessedInput.setText(EMPTY);
    }

    public void resetCode() {
        this.currentCodeConfig.setText(EMPTY);
        this.initialCodeConfig.setText(EMPTY);
    }

    public void disableConfigButtons() {
        this.setCode.setDisable(false);
        this.setRandomCode.setDisable(false);
    }


    @Override
    public void updateCode(String code) {
        this.currentCodeConfig.setText(code);
    }

    public void updateTotalEncryptedWord(int amount) {
        amountOfProcessedInput.setText(String.valueOf(amount));
    }


    public void updateMachineInformation(MachineInformation machineInformationFromServer) {
        this.machineInformation=machineInformationFromServer;
        this.updateMachineInformation();
    }
}
