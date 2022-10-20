package app.components.logic.login;

import DTO.AgentData;
import app.AgentAppMainController;



import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.EnigmaUtils;
import web.Constants;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static web.Constants.ADD_AGENT;
import static web.Constants.REFRESH_RATE;

public class LoginController {

    @FXML
    public TextField userNameTextField;
    @FXML
    private Button loginButton;

    @FXML
    public Label errorMessageLabel;

    @FXML
    private TableView<String> alliesTable;

    @FXML
    private TableColumn<String, String> alliesColumn;

    @FXML
    private TextField chosenAlly;

    @FXML
    private ChoiceBox<Integer> amountOfThreadsChoiceBox;

    @FXML
    private TextField taskSizeField;

    private Timer timer;
    private TimerTask listRefresher;

    private AgentAppMainController agentAppMainController;
    private boolean validAssignment=false;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Tooltip toolTipError;

    @FXML
    public void initialize() {

        errorMessageLabel.textProperty().bind(errorMessageProperty);
        alliesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        List<Integer> list=new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        amountOfThreadsChoiceBox.setItems(FXCollections.observableArrayList(list));
        setToolTip();
        bindListenersToInputButtons();
    }

    private void bindListenersToInputButtons() {
        taskSizeField.textProperty().
                addListener((object, oldValue, newValue) -> allDataValid());
        amountOfThreadsChoiceBox.valueProperty().addListener((object, oldValue, newValue) -> allDataValid());
        chosenAlly.textProperty().addListener((object, oldValue, newValue) -> allDataValid());
        userNameTextField.textProperty().addListener((object, oldValue, newValue) -> allDataValid());
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        AgentData agentData=new AgentData(chosenAlly.getText(),amountOfThreadsChoiceBox.getValue(),Integer.parseInt(this.taskSizeField.getText()),this.userNameTextField.getText());


        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                        .parse(Constants.LOGIN_PAGE)
                        .newBuilder()
                        .addQueryParameter("username", userName)
                        .addQueryParameter("entity", "agent")
                        .build()
                        .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {

                    String json = "{\"id\":1,\"name\":\"John\"}";

                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json"), json);
                    HttpClientUtil.runAsyncWithBody(ADD_AGENT,new Callback(){

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }
                    },body);


                    Platform.runLater(() -> {
                            agentAppMainController.updateUserName(userName);
                            agentAppMainController.switchToChatRoom();

                        agentAppMainController.setAgentData(agentData);
                    });
                }
            }
        });
    }


    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    void selectedAlly(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedAlly = this.alliesTable.getSelectionModel().getSelectedItem();
            this.chosenAlly.setText(selectedAlly);
        }

    }

    private void setToolTip() {
        toolTipError = new Tooltip("input must be a positive number");
        toolTipError.setId("error-tool-tip");
        //changes duration until tool tip is shown
        EnigmaUtils.hackTooltipStartTiming(toolTipError, 100);
        this.taskSizeField.setOnMouseEntered(event -> showToolTip());
        this.taskSizeField.setOnMouseExited(event -> toolTipError.hide());
    }

    private void showToolTip() {
        String currentInput = this.taskSizeField.getText();

        if (!currentInput.isEmpty() && !validAssignment) {
            renderToolTip();
        } else {
            toolTipError.hide();
        }
    }

    private void allDataValid() {
        if (assignmentInputValid(taskSizeField.getText())) {
            if (amountOfThreadsChoiceBox.getValue() != null) {
                if (chosenAlly.getText()!=null && !chosenAlly.getText().isEmpty()) {
                    loginButton.setDisable(false);
                    toolTipError.hide();
                    return;
                }
            }
        }
        loginButton.setDisable(true);
    }

    private boolean assignmentInputValid(String newValue) {
        if (newValue.isEmpty()) {
            taskSizeField.setId(null);
            toolTipError.hide();
        }
        else {
            if (EnigmaUtils.isNumeric(newValue)) {
                int number = Integer.parseInt(newValue);
                if(number>0) {

                    validAssignment = true;
                    toolTipError.hide();
                    return true;
                }
                else {
                    validAssignment = false;
                    renderToolTip();
                    taskSizeField.setId("error-text-field");
                }
            } else {
                validAssignment = false;
                renderToolTip();
                taskSizeField.setId("error-text-field");
            }
        }
        return false;
    }

    private void renderToolTip() {
        Bounds boundsInScene = taskSizeField.localToScreen(taskSizeField.getBoundsInLocal());
        toolTipError.show(taskSizeField, boundsInScene.getMaxX(), boundsInScene.getMaxY() + 15);
    }

    public void startListRefresher() {
        listRefresher = new AlliesListRefresher(this::updateAllies);
        timer = new Timer();
        timer.schedule(listRefresher, 200, REFRESH_RATE);
    }
    private void updateAllies(List<String> alliesDetails) {
        Platform.runLater(() -> {
            alliesTable.getItems().clear();
            alliesTable.setItems(FXCollections.observableList(alliesDetails));
        });
    }



    public void setChatAppMainController(AgentAppMainController alliesAppMainController) {
        this.agentAppMainController = alliesAppMainController;
    }
}
