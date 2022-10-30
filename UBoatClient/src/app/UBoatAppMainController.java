package app;


import app.components.logic.login.LoginController;
import app.mini_apps.uboat.UboatController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;


import static web.Constants.JHON_DOE;



public class UBoatAppMainController implements Closeable {
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/app/components/logic/login/login.fxml";
    private GridPane loginComponent;
    private LoginController logicController;

    private Parent uboatComponent;
    private UboatController uboatController;

    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;

    private final StringProperty currentUserName;

    public UBoatAppMainController() {
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
        loadLoginPage();
        loadUboatPage();
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
    }

    private void loadUboatPage() {
        URL url = getClass().getResource("/app/mini_apps/uboat/uboatApp.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            uboatComponent = fxmlLoader.load();
            uboatController = fxmlLoader.getController();
            uboatController.setMainAppController(this);
           /* chatRoomComponentController.setChatAppMainController(this);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void switchToChatRoom() {
        setMainPanelTo(uboatComponent);
    }

}
