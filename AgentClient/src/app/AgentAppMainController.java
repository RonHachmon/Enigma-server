package app;


import DTO.AgentData;
import app.components.logic.login.LoginController;
import app.mini_apps.agent.AgentController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Window;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import web.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static web.Constants.*;


public class AgentAppMainController implements Closeable {
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/app/components/logic/login/login.fxml";

    private GridPane loginComponent;
    private LoginController logicController;

    private Parent uboatComponent;
    private AgentController agentController;

    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;

    private final StringProperty currentUserName;

    public AgentAppMainController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));

        // prepare components
        loadLoginPage();
        loadUboatPage();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }
    
    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    @Override
    public void close() throws IOException {
        logicController.close();
        agentController.close();

    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setChatAppMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logicController.startListRefresher();
    }

    private void loadUboatPage() {
        URL url = getClass().getResource("/app/mini_apps/agent/agentApp.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            uboatComponent = fxmlLoader.load();
            agentController = fxmlLoader.getController();
           /* chatRoomComponentController.setChatAppMainController(this);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void switchToChatRoom() {
        setMainPanelTo(uboatComponent);
        mainPanel.getScene().getWindow().setHeight(650);
        mainPanel.getScene().getWindow().setWidth(850);
    }



    public void setAgentData(AgentData agentData) {
        this.agentController.setAgentData(agentData);
        this.addAgentHttpRequest( agentData);
    }
    private void addAgentHttpRequest(AgentData agentData)
    {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), GSON_INSTANCE.toJson(agentData));
        HttpClientUtil.runAsyncWithBody(AGENTS_DATA,new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        },body);


    }
}
