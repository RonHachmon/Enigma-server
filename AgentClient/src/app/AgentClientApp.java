package app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import web.Constants;
import web.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;




public class AgentClientApp extends Application {

    private AgentAppMainController alliesAppMainController;
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/app/agent-app-main.fxml";


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(350);
        primaryStage.setMinWidth(500);
        primaryStage.setTitle("Agent client");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            alliesAppMainController = fxmlLoader.getController();

            Scene scene = new Scene(root, 500, 350);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.runBlocking(Constants.LOGOUT);
        HttpClientUtil.shutdown();
        alliesAppMainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
