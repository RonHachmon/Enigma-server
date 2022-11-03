package app.mini_apps.uboat.bodies;

import DTO.AllyDTO;
import DTO.DecryptionCandidate;
import DTO.MachineInformationDTO;
import app.mini_apps.uboat.bodies.absractScene.MainAppScene;
import app.mini_apps.uboat.bodies.interfaces.CodeHolder;
import app.mini_apps.uboat.bodies.refreshers.AlliesListRefresher;
import app.mini_apps.uboat.bodies.refreshers.CandidatesListRefresher;
import app.mini_apps.uboat.smallComponent.CandidateController;
import app.mini_apps.uboat.winner.WinnerController;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;


import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static app.mini_apps.uboat.bodies.ConfigurationController.SETTING_SCENE_FXML;
import static web.Constants.*;


public class ContestController extends MainAppScene implements Initializable, CodeHolder, Closeable {

    public static final String WORD_CANDIDATE_FXML = "/app/mini_apps/uboat/smallComponent/wordCandidate.fxml";

    public static final String WINNER_FXML = "/app/mini_apps/uboat/winner/winner.fxml";
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

    boolean clearTextClicked = false;
    private boolean isReady = false;
    private boolean winnerShown = false;
    private Dictionary dictionary;
    private boolean battleInProgress = false;

    private Timer timer;
    private TimerTask listRefresher;
    private Boolean autoUpdateAllies;
    private String battleName;
    private int lastUpdatedCandidateIndex = 0;
    private TimerTask candidatesListRefresher;
    private WinnerController winnerController;


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
        } catch (Exception e) {
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
        listRefresher = new AlliesListRefresher(autoUpdateAllies, this::updateAllies, battleName);
        timer = new Timer();
        timer.schedule(listRefresher, 200, REFRESH_RATE);
    }

    private void updateAllies(List<AllyDTO> alliesDetails) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            alliesTable.setItems(FXCollections.observableList(alliesDetails));
        });
        if (isReady) {
            if (this.isAllAlliesReady(alliesDetails)) {
                isReady = false;
                autoUpdateAllies = false;
                this.startBattleInServer();
                battleInProgress = true;
                this.candidatesListRefresher = new CandidatesListRefresher(this::updateCandidates, this.battleName);
                startTimer(candidatesListRefresher);
            }
        }
    }

    private void startTimer(TimerTask timerTask) {
        timer.schedule(timerTask, 200, REFRESH_RATE);
    }

    private void startBattleInServer() {
        MachineInformationDTO machineInformationDTO = new MachineInformationDTO(this.machineManager, outputArea.getText());
        String finalURL = HttpUrl.parse(BATTLE_STATUS).
                newBuilder().
                addQueryParameter("battleship", this.battleName)
                .addQueryParameter("status", "start")
                .build()
                .toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), GSON_INSTANCE.toJson(machineInformationDTO));

        HttpClientUtil.runAsyncWithBody(finalURL, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        }, body);

    }

    private void endBattleInServer(String winningAlly) {
        MachineInformationDTO machineInformationDTO = new MachineInformationDTO(this.machineManager, outputArea.getText());
        String finalURL = HttpUrl.parse(BATTLE_STATUS).
                newBuilder().
                addQueryParameter("battleship", this.battleName)
                .addQueryParameter("status", "end")
                .build()
                .toString();
        machineInformationDTO.setWinningTeam(winningAlly);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), GSON_INSTANCE.toJson(machineInformationDTO));

        HttpClientUtil.runAsyncWithBody(finalURL, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        }, body);
    }

    private boolean isAllAlliesReady(List<AllyDTO> alliesDetails) {
        if(alliesDetails.size()==0)
        {
            return false;
        }
        for (AllyDTO allyDTO : alliesDetails) {
            if (!allyDTO.isReady()) {
                return false;
            }
        }
        return true;
    }

    @FXML
    void pressedReady(ActionEvent event) {
        if (readyButton.getText().equals("Logout")) {
            this.logout();
            this.uboatController.close();
        } else {
            inputArea.setDisable(true);
            outputArea.setDisable(true);
            this.runButton.setDisable(true);
            isReady = true;
        }
    }

    @Override
    public void close() throws IOException {
        inputArea.clear();
        outputArea.clear();
        readyButton.setText("Ready");
        readyButton.setId("load-button");
        readyButton.setDisable(false);
        if (candidatesListRefresher != null && timer != null && listRefresher != null) {
            candidatesListRefresher.cancel();
            listRefresher.cancel();
            timer.cancel();
        }
    }


    private void logout() {
        String finalUrl = HttpUrl
                .parse(UNJOIN_BATTLE)
                .newBuilder()
                .addQueryParameter("battleship", battleName)
                .addQueryParameter("entity", "uboat")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*  httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure...:(");*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    /*httpStatusUpdate.updateHttpLine("Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());*/
                }
            }
        });
    }

    private void updateCandidates(DecryptionCandidate[] decryptionCandidates) {
        if (battleInProgress) {
            Platform.runLater(() -> {
                System.out.println("candi size " + decryptionCandidates.length);
                for (int i = this.lastUpdatedCandidateIndex; i < decryptionCandidates.length; i++) {

                    createWordCandidate(decryptionCandidates[i]);

                    if (decryptionCandidates[i].getDecryptedString().equals(inputArea.getText().toUpperCase())) {
                        battleInProgress = false;
                        endBattleInServer(decryptionCandidates[i].getAllyName());

                        this.showWinner(decryptionCandidates[i].getAllyName());
                        this.enableLogout();
                        winnerShown = true;

                    }
                }
                lastUpdatedCandidateIndex = decryptionCandidates.length;
            });
        }

    }

    private void enableLogout() {
        readyButton.setText("Logout");
        readyButton.setId("logout");
    }

    private void showWinner(String allyName) {
        if (!winnerShown) {
            try {
                System.out.println("uboat winner");
                Stage settingStage = loadWinnerStage();

                this.winnerController.setWinnerLabel(allyName);
                settingStage.initModality(Modality.WINDOW_MODAL);
                settingStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Stage loadWinnerStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WINNER_FXML));

        AnchorPane anchorPane = loader.load();
        this.winnerController = loader.getController();
        Scene scene = new Scene(anchorPane, 455, 100);
        Stage settingStage = new Stage();
        settingStage.setScene(scene);
        settingStage.setTitle("winner");
        return settingStage;
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
        this.clearTextClicked = true;
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

