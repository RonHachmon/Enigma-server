package app.mini_apps.uboat.settings;

import engine.enigma.machineutils.MachineInformation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.EnigmaUtils;


import java.io.IOException;
import java.net.URL;
import java.util.*;


public class SettingController implements Initializable {


    public static final String SELECTED_SWITCH_PLUG = "selected-switch-plug";
    public static final String SELECTED_REFLECTOR = "selected-reflector";
    public static final String SWITCH_PLUG_FXML = "/app/mini_apps/uboat/smallComponent/switchPlug.fxml";
    public static final String REFLECTOR_BUTTON_FXML = "/app/mini_apps/uboat/smallComponent/reflectorButton.fxml";
    private MachineInformation machineInformation;
    private List<Character> availableCharacters = new ArrayList<>();

    private Integer selectedReflector = null;
    private List<ChoiceBox<Integer>> rotorIndexes = new ArrayList<>();
    private List<ChoiceBox<Character>> rotorStartingIndexes = new ArrayList<>();


    private boolean isSettingValid = false;


    @FXML
    private HBox rotorsSetting;

    @FXML
    private Label rotorsErrorLabel;


    @FXML
    private HBox rotorsStartingIndexSetting;


    @FXML
    private VBox availableReflectors;


    @FXML
    private HBox allSwitchPlugs;

    @FXML
    private Label switchPlugError;

    @FXML
    private GridPane selectingRotorsGrid;
    @FXML
    private GridPane selectingStartingIndexGrid;
    @FXML
    private GridPane selectingReflectorsGrid;
    @FXML
    private GridPane switchPlugGrid;



    public void setSetting(MachineInformation machineInformation) {
        this.machineInformation = machineInformation;
        for (int i = 0; i < machineInformation.getAvailableChars().length(); i++) {
            this.availableCharacters.add(machineInformation.getAvailableChars().charAt(i));
        }
        setRotorsIndexesChoiceBox();

        setRotorStartingIndexChoiceBox();

        setReflectors();

    }

    private void setReflectors() {

        for (int i = 0; i < this.machineInformation.getAvailableReflectors(); i++) {

            Button reflector = loadFXML(REFLECTOR_BUTTON_FXML);

            reflector.setOnAction((e) ->
            {
                Button selectedReflector = ((Button) e.getSource());
                this.selectedReflector = EnigmaUtils.convertRomanToInt((selectedReflector.getText()));
                this.unselectAllReflectors();
                selectedReflector.setId(SELECTED_REFLECTOR);

            });
            reflector.setText(EnigmaUtils.convertIntToRoman(i + 1));
            availableReflectors.getChildren().add(reflector);
            VBox.setMargin(reflector, new Insets(12, 0, 0, 0));
        }
    }

    private void unselectAllReflectors() {
        this.availableReflectors.getChildren().forEach(node -> node.setId(""));
    }

    private void setRotorStartingIndexChoiceBox() {
        List<Character> rotors = new ArrayList<>();
        for (int j = 0; j < machineInformation.getAvailableChars().length(); j++) {
            rotors.add(machineInformation.getAvailableChars().charAt(j));

        }

        for (int i = 0; i < machineInformation.getAmountOfRotorsRequired(); i++) {
            ChoiceBox<Character> rotorStartingIndex = new ChoiceBox<>(FXCollections.observableArrayList(rotors));
            rotorsStartingIndexSetting.getChildren().add(rotorStartingIndex);
            rotorStartingIndexes.add(rotorStartingIndex);
            HBox.setMargin(rotorStartingIndex, new Insets(0, 12, 0, 12));
        }
    }

    private void setRotorsIndexesChoiceBox() {
        List<Integer> rotors = new ArrayList<>();
        for (int j = 0; j < machineInformation.getAmountOfRotors(); j++) {
            rotors.add(j + 1);

        }

        for (int i = 0; i < machineInformation.getAmountOfRotorsRequired(); i++) {

            ChoiceBox<Integer> rotorIndex = new ChoiceBox<>(FXCollections.observableArrayList(rotors));

            rotorIndex.valueProperty().addListener(
                    (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                        if (this.isAllElementAreUniqueInListOfChoiceBox(this.rotorIndexes)) {
                            this.rotorsErrorLabel.setText("");

                        } else {
                            this.rotorsErrorLabel.setText("Error, each id must only appear once");
                        }


                    });
            rotorsSetting.getChildren().add(rotorIndex);
            rotorIndexes.add(rotorIndex);
            HBox.setMargin(rotorIndex, new Insets(0, 12, 0, 12));
        }
    }

    @FXML
    void addSwitchPlug(ActionEvent event) {
            HBox switchPlug = loadFXML(SWITCH_PLUG_FXML);
            switchPlug.setOnMouseClicked(hboxEvent ->
            {
                HBox selectedHbox = (HBox) hboxEvent.getSource();
                this.unSelectAllSwitchPlug();
                selectedHbox.setId(SELECTED_SWITCH_PLUG);
            });

            //setting selectable letters
            for (int i = 0; i < switchPlug.getChildren().size(); i++) {
                if (switchPlug.getChildren().get(i) instanceof ChoiceBox) {
                    ChoiceBox<Character> choiceBox = (ChoiceBox<Character>) switchPlug.getChildren().get(i);
                    choiceBox.valueProperty().addListener(
                            (ObservableValue<? extends Character> ov, Character old_val, Character new_val) -> {
                                if (isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes())) {
                                    this.switchPlugError.setText("");

                                } else {
                                    this.switchPlugError.setText("Error, each key can only appear once ");
                                }

                            });
                    choiceBox.setItems(FXCollections.observableArrayList(this.availableCharacters));
                }

            }
            HBox.setMargin(switchPlug, new Insets(0, 12, 0, 12));
            this.allSwitchPlugs.getChildren().add(switchPlug);
    }

    @FXML
    void removeSwitchPlug(ActionEvent event) {
        boolean foundSwitchPlug=false;
        for (int i = 0; i < this.allSwitchPlugs.getChildren().size(); i++) {
            if (allSwitchPlugs.getChildren().get(i).getId() == SELECTED_SWITCH_PLUG) {
                allSwitchPlugs.getChildren().remove(i);
                foundSwitchPlug=true;
                break;
            }
        }
        if(!foundSwitchPlug)
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please select the switch plug to delete");
            a.setTitle("missing input");
            a.show();
        }


        if (isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes())) {
            this.switchPlugError.setText("");
        } else {
            this.rotorsErrorLabel.setText("Error, each id must only appear once");

        }
    }

    private void unSelectAllSwitchPlug() {
        this.allSwitchPlugs.getChildren().forEach(node -> node.setId(""));
    }

    public List<ChoiceBox<Integer>> getRotorIndexes() {
        return rotorIndexes;
    }

    public List<ChoiceBox<Character>> getRotorStartingIndexes() {
        return rotorStartingIndexes;
    }

    public Integer getSelectedReflector() {
        return selectedReflector;
    }

    public List<ChoiceBox<Character>> getAllSwitchPlugChoiceBoxes() {
        List<ChoiceBox<Character>> switchPlugsChoiceBoxes = new ArrayList<>();
        for (int i = 0; i < allSwitchPlugs.getChildren().size(); i++) {
            HBox currentHbox = (HBox) allSwitchPlugs.getChildren().get(i);
            for (int j = 0; j < currentHbox.getChildren().size(); j++) {
                Node currentNode = currentHbox.getChildren().get(j);
                if (currentNode instanceof ChoiceBox) {
                    switchPlugsChoiceBoxes.add((ChoiceBox<Character>) currentNode);
                }
            }
        }

        return switchPlugsChoiceBoxes;
    }

    public boolean getIsCodeValid() {
        return isSettingValid;
    }

    @FXML
    void setRotorsClicked(ActionEvent event) {
        this.setAllToInvisible();
        selectingRotorsGrid.setVisible(true);

    }

    @FXML
    void setStartingIndexesClicked(ActionEvent event) {
        this.setAllToInvisible();
        selectingStartingIndexGrid.setVisible(true);
    }

    @FXML
    void setReflectorsClicked(ActionEvent event) {
        this.setAllToInvisible();
        selectingReflectorsGrid.setVisible(true);

    }

    @FXML
    void setSwitchPlugClicked(ActionEvent event) {
        this.setAllToInvisible();
        switchPlugGrid.setVisible(true);

    }


    private void setAllToInvisible() {
        selectingRotorsGrid.setVisible(false);
        selectingStartingIndexGrid.setVisible(false);
        selectingReflectorsGrid.setVisible(false);
        switchPlugGrid.setVisible(false);
    }

    @FXML
    void confirmAllSettingClicked(ActionEvent event) {
        StringBuilder errorMsg = new StringBuilder("");
        boolean isValid = isAllSettingValid(errorMsg);
        if (isValid) {
            this.isSettingValid = true;
            closeStage();

        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(errorMsg.toString());
            a.setTitle("Invalid setting");
            a.show();
        }
    }


    private boolean isAllSettingValid(StringBuilder errorMsg) {
        if (!this.isAllElementAreUniqueInListOfChoiceBox(this.getRotorIndexes())) {
            errorMsg.append("not all rotor index are distinct");
            return false;
        }
        if (!this.isAllElementAreUniqueInListOfChoiceBox(this.getAllSwitchPlugChoiceBoxes())) {
            errorMsg.append("not all switch plugs keys are distinct");
            return false;
        }
        if (!this.arrayDoesNotContainNull()) {
            errorMsg.append("not all field are set");
            return false;
        }
        if (selectedReflector == null) {
            errorMsg.append("please select a reflector");
            return false;
        }

        return true;
    }

    @FXML
    void cancelSettingClicked(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) rotorsSetting.getScene().getWindow();
        stage.close();
    }





    private <T> boolean isAllElementAreUniqueInListOfChoiceBox(List<ChoiceBox<T>> listOfChoiceBoxes) {

        Set<T> characters = new HashSet<>();
        for (int i = 0; i < listOfChoiceBoxes.size(); i++) {
            if (listOfChoiceBoxes.get(i).getValue() != null) {
                if (!(characters.add(listOfChoiceBoxes.get(i).getValue()))) {

                    return false;
                }
            }
        }

        return true;
    }

    private boolean arrayDoesNotContainNull() {
        for (int i = 0; i < rotorIndexes.size(); i++) {
            if (rotorIndexes.get(i).getValue() == null) {

                return false;

            }
        }
        for (int i = 0; i < this.rotorStartingIndexes.size(); i++) {
            if (rotorStartingIndexes.get(i).getValue() == null) {

                return false;

            }
        }

        return true;
    }

    private <T> T loadFXML(String path)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}