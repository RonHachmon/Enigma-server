package app.mini_apps.uboat.bodies;

import DTO.AllyDTO;
import DTO.DecryptionCandidate;
import DTO.MachineInformationDTO;
import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import app.mini_apps.uboat.bodies.refreshers.AlliesListRefresher;
import app.mini_apps.uboat.bodies.refreshers.CandidatesListRefresher;
import app.mini_apps.uboat.smallComponent.CandidateController;
import engine.enigma.bruteForce2.utils.Dictionary;

import javafx.application.Platform;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static web.Constants.*;


public class ContestController extends MainAppScene implements Initializable, CodeHolder {

    public static final String WORD_CANDIDATE_FXML = "/app/mini_apps/uboat/smallComponent/wordCandidate.fxml";
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
    private TableView<AllyDTO> alliesTable;

    @FXML
    private TableColumn<AllyDTO, String> alliesColumn;
    @FXML
    private TableColumn<AllyDTO, Boolean> readyColumn;
    @FXML
    private TableColumn<AllyDTO, Integer> agentColumn;

    @FXML
    private TableColumn<AllyDTO, Integer> taskColumn;

    @FXML
    private Button readyButton;


    private static String newline = System.getProperty("line.separator");

    boolean clearTextClicked=false;
    private boolean isReady=false;
    private Dictionary dictionary;

    private Timer timer;
    private TimerTask listRefresher;
    private Boolean autoUpdateAllies;
    private String battleName;
    private int lastUpdatedCandidateIndex=0;
    private TimerTask candidatesListRefresher;


    public ContestController() {

        autoUpdateAllies = new Boolean(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitialDictionaryTable();
        setAlliesTable();
        searchBar.textProperty().
                addListener((object, oldValue, newValue) -> filterDictionaryTable(newValue));

    }
    @FXML
    void runClicked(ActionEvent event) {
        try {
            String output = this.machineManager.encryptSentenceAndAddToStatistic(inputArea.getText().toUpperCase());
            outputArea.setText(output);
            uboatController.updateMachineCode(machineManager.getCurrentCodeSetting());
            this.readyButton.setDisable(false);
        }
        catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.setTitle("Invalid character");
            a.show();
        }

    }

    private void setAlliesTable() {
        alliesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAllyName()));
        readyColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isReady()));
        agentColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfAgents"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskSize"));

    }
    public void startListRefresher() {
        this.battleName = this.machineManager.getBattleField().getBattleName();
        listRefresher = new AlliesListRefresher(autoUpdateAllies,this::updateAllies, battleName);
        timer = new Timer();
        timer.schedule(listRefresher, 200, REFRESH_RATE);
    }
    private void updateAllies(List<AllyDTO> alliesDetails) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            alliesTable.setItems(FXCollections.observableList(alliesDetails));
        });
        if(isReady)
        {
            if(this.isAllAlliesReady(alliesDetails))
            {
                autoUpdateAllies=false;
                this.startBattleInServer();
                this.candidatesListRefresher = new CandidatesListRefresher(this::updateCandidates,this.battleName);
                startTimer(candidatesListRefresher);
            }
        }
    }
    private void startTimer(TimerTask timerTask) {
        timer.schedule(timerTask, 200, REFRESH_RATE);
    }

    private void startBattleInServer() {
        MachineInformationDTO machineInformationDTO=new MachineInformationDTO(this.machineManager,outputArea.getText());
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), GSON_INSTANCE.toJson(machineInformationDTO));
        HttpClientUtil.runAsyncWithBody(BATTLE_STATUS+"?battleship="+this.battleName,new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        },body);
        isReady=false;
    }

    private boolean isAllAlliesReady(List<AllyDTO> alliesDetails) {
        for (AllyDTO allyDTO:alliesDetails) {
            if (!allyDTO.isReady())
            {
                return false;
            }
        }
        return true;
    }

    @FXML
    void pressedReady(ActionEvent event) {
        inputArea.setDisable(true);
        outputArea.setDisable(true);
        this.runButton.setDisable(true);
        isReady=true;



    }
    private void updateCandidates(DecryptionCandidate[] decryptionCandidates) {
        Platform.runLater(() -> {
            System.out.println("candi size "+decryptionCandidates.length);
            for (int i = this.lastUpdatedCandidateIndex; i < decryptionCandidates.length; i++) {
                createWordCandidate(decryptionCandidates[i]);
            }
            lastUpdatedCandidateIndex=decryptionCandidates.length;
        });

    }
    private void createWordCandidate(DecryptionCandidate decryptionCandidate) {

        try {
            Node wordCandidate = loadCandidate(decryptionCandidate);

            FlowPane.setMargin(wordCandidate, new Insets(2, 10, 2, 10));
            this.candidatesFlowPane.getChildren().add(wordCandidate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Node loadCandidate(DecryptionCandidate decryptionCandidate) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(WORD_CANDIDATE_FXML));
        Node wordCandidate = loader.load();
        wordCandidate.setFocusTraversable(false);

        CandidateController wordCandidateController = loader.getController();
        wordCandidateController.setTextFont();
        wordCandidateController.setText(decryptionCandidate.getDecryptedString());
        wordCandidateController.setAgent(String.valueOf(decryptionCandidate.getAllyName()));
        wordCandidateController.setCode(decryptionCandidate.getCodeConfiguration());
        return wordCandidate;
    }

    //---------------------dictionary Related---------------------------------------
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

    //---------------------End of Dictionary Related---------------------------------------
    //---------------------encryptRelated---------------------------------------

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

    public void clearText() {
        inputArea.setText("");
        outputArea.setText("");

    }
    //---------------------End of encryptRelated---------------------------------------

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

