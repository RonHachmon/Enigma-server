package app.mini_apps.uboat.bodies;

import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import engine.enigma.battlefield.BattleFieldInfo;
import engine.enigma.bruteForce2.utils.Dictionary;
import engine.enigma.machineutils.NewStatisticInput;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;


import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static app.util.Constants.REFRESH_RATE;

public class EncryptController extends MainAppScene implements Initializable, CodeHolder {




    @FXML
    private Label currentCode;


    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;


    @FXML
    private TextField searchBar;

    @FXML
    private TableView<String> dictionaryTable;

    @FXML
    private TableColumn<String, String> wordsColumn;

    @FXML
    private Button runButton;
    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private TableView<String> alliesTable;

    @FXML
    private TableColumn<String, String> alliesColumn;


    private static String newline = System.getProperty("line.separator");
    private Integer currentNanoDuration=0;
    SimpleBooleanProperty isManual = new SimpleBooleanProperty(false);
    boolean clearTextClicked=false;
    private boolean toAnimate=false;
    private Dictionary dictionary;

    private Timer timer;
    private TimerTask listRefresher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputArea.textProperty().addListener((object, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {

            }
            if (isManual.get()) {
                if(!clearTextClicked)
                {
                    encryptOneByOne(oldValue, newValue);
                }
                else
                {
                    clearTextClicked=false;
                }
            }
        });
        setInitialDictionaryTable();
        setAlliesTable();
        searchBar.textProperty().
                addListener((object, oldValue, newValue) -> filterDictionaryTable(newValue));

        setBindings();
    }

    private void setAlliesTable() {
        alliesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

    }
    private void updateAllies(List<String> alliesDetails) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            alliesTable.setItems(FXCollections.observableList(alliesDetails));
        });
    }
    public void startListRefresher() {
        listRefresher = new AlliesListRefresher(this::updateAllies,this.machineManager.getBattleField().getBattleName());
        timer = new Timer();
        timer.schedule(listRefresher, 200, REFRESH_RATE);
    }

    @FXML
    void selectedWord(MouseEvent event) {
        if (event.getClickCount() == 2) {
            this.addWordToInput(dictionaryTable.getSelectionModel().getSelectedItem());
        }
    }
    private void addWordToInput(String selectedWord) {
        String inputText = inputArea.getText();
        if (inputText.isEmpty() || inputText.charAt(inputText.length() - 1) == ' ') {
            inputArea.setText(inputText + selectedWord);
        } else {
            inputArea.setText(inputText + " " + selectedWord);
        }
    }
    public void updateInitialDictionaryTable() {
        dictionary = machineManager.getDictionary();
        dictionaryTable.getItems().clear();
        dictionary.getDictionary().forEach(s -> dictionaryTable.getItems().add(s));
    }
    private void filterDictionaryTable(String newValue) {
        dictionaryTable.getItems().clear();
        if (newValue.isEmpty()) {
            dictionary.getDictionary().forEach(s -> dictionaryTable.getItems().add(s));

        } else {
            dictionary.getDictionary().stream().filter(s -> s.startsWith(newValue.toUpperCase())).
                    forEach(s -> dictionaryTable.getItems().add(s));
        }
    }
    private void setInitialDictionaryTable() {
        wordsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    }

    private void setBindings() {


        runButton.visibleProperty().bind(isManual.not());

        runButton.managedProperty().bind(isManual.not());
    }




    @FXML
    void clearTextClicked(ActionEvent event) {
        this.clearTextClicked=true;
        this.uboatController.clearEncryptText();
    }

    @FXML
    void resetCodeClicked(ActionEvent event) {
        this.machineManager.resetMachineCode();
        this.uboatController.updateMachineCode(machineManager.getCurrentCodeSetting());
    }

    @FXML
    void resetAndClear(ActionEvent event) {
        this.uboatController.clearEncryptText();
        this.resetCodeClicked(event);

    }



    @FXML
    void runClicked(ActionEvent event) {
        try {
            String output = this.machineManager.encryptSentenceAndAddToStatistic(inputArea.getText().toUpperCase());
            outputArea.setText(output);
            updateStatistic();
            uboatController.updateMachineCode(machineManager.getCurrentCodeSetting());
        }
        catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }

    }

    private void updateStatistic() {
        this.uboatController.updateTotalWordEncrypted( this.machineManager.getProcessedInputCounter());

    }



    public void clearText() {
        inputArea.setText("");
        outputArea.setText("");

    }





    @Override
    public void updateCode(String code) {
        if (toAnimate) {
            scaleAnimation(code);
        }
        else {
            this.currentCode.setText(code);
        }
    }

    private void scaleAnimation(String code) {

            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(this.currentCode);
            translateTransition.setDuration(javafx.util.Duration.millis(400));
            translateTransition.setFromY(0);
            translateTransition.setToY(-20);

            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setNode(this.currentCode);
            fadeTransition.setDuration(javafx.util.Duration.millis(300));
            fadeTransition.setFromValue(0.2);
            fadeTransition.setToValue(0);
            translateTransition.play();
            fadeTransition.play();
            fadeTransition.setOnFinished((actionEvent) ->
            {
                this.currentCode.setText(code);
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(this.currentCode);
                transition.setDuration(javafx.util.Duration.millis(400));
                transition.setFromY(20);
                transition.setToY(0);
                FadeTransition fade = new FadeTransition();
                fade.setNode(this.currentCode);
                fade.setDuration(javafx.util.Duration.millis(300));
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.play();
                transition.play();
            });
    }


    public void addCodeToComboBox(String code) {
 /*       updateCurrentCode();*/

    }






    //--------------------------------------------Encrypt related--------------------------------
    //listen to inputTextArea and differs between new text to old text
    private void encryptOneByOne(String oldValue, String newValue) {
        try {
            /*if (!newValue.isEmpty()) {*/
                if (newValue.length() > oldValue.length()) {
                    encryptNewText(oldValue, newValue);
                } else {
                    this.machineManager.resetMachineCode();
                    this.uboatController.updateMachineCode(machineManager.getCurrentCodeSetting());
                    this.outputArea.setText("");
                    encryptString(newValue);
                }
            /*}*/
        } catch (Exception e) {
            inputArea.setText(oldValue);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }
    }

    //extract new characters
    private void encryptNewText(String oldValue, String newValue) {
        String addedCharacters = newValue.substring(oldValue.length());

        if (addedCharacters.contains(newline)) {
            outputArea.setText(outputArea.getText() + newline);
        }
        if (!addedCharacters.contains("\t")) {
            encryptString(addedCharacters);
        }
    }
    //send to encryption char by char
    private void encryptString(String addedChar) {
        for (int i = 0; i < addedChar.length(); i++) {
            sentToEncryptAndCalculateTime(addedChar.charAt(i));
        }
    }

    //for each char encrypt and sums time of encryption
    private void sentToEncryptAndCalculateTime(Character input) {
        Instant timeStart = Instant.now();
        String output = this.machineManager.encryptSentence(input.toString().toUpperCase());
        currentNanoDuration+=Duration.between(timeStart, Instant.now()).getNano();
        outputArea.setText(outputArea.getText() + output);
        uboatController.updateMachineCode(machineManager.getCurrentCodeSetting());
    }

    //--------------------------------------------End: Encrypt related--------------------------------

    private <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }

    public void enableAnimation(boolean selected) {
        this.toAnimate=selected;
    }
}

