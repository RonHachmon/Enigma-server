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





public class UBoatClientApp extends Application {
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/app/uboat-app-main.fxml";
    private UBoatAppMainController UBoatAppMainController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(350);
        primaryStage.setMinWidth(500);
        primaryStage.setTitle("Uboat client");

        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            UBoatAppMainController = fxmlLoader.getController();

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
