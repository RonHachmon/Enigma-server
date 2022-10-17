package app.mini_apps.uboat.bodies;

import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import engine.enigma.bruteForce2.utils.Dictionary;

import javafx.application.Platform;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static web.Constants.REFRESH_RATE;


public class ContestController extends MainAppScene implements Initializable, CodeHolder {




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

    boolean clearTextClicked=false;
    private boolean toAnimate=false;
    private Dictionary dictionary;

    private Timer timer;
    private TimerTask listRefresher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitialDictionaryTable();
        setAlliesTable();
        searchBar.textProperty().
                addListener((object, oldValue, newValue) -> filterDictionaryTable(newValue));

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
            this.currentCode.setText(code);
    }


    private <T> T loadFXML(String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }
}

