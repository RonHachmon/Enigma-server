package app.mini_apps.allies.header;


import app.mini_apps.allies.bodies.absractScene.MainAppScene;
import engine.enigma.machineutils.MachineInformation;
import engine.enigma.machineutils.MachineManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import okhttp3.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;



public class HeaderController extends MainAppScene implements Initializable   {
    public static final String COMMAND_PROMPT_TTF = "/resources/fonts/windows_command_prompt.ttf";

    @FXML
    private Button dashboardButton;

    @FXML
    private Button contestButton;





    @FXML
    private Label titleLabel;



    @FXML
    void contestClicked(ActionEvent event) {
        contestButton.setDisable(true);
        dashboardButton.setDisable(false);
        alliesController.displayEncrypt();

    }

    @FXML
    void dashboardClicked(ActionEvent event) {
        dashboardButton.setDisable(true);
        contestButton.setDisable(false);
        alliesController.displayMachineConfigScene();

    }



    public void setMachineManager(MachineManager machineManager) {
        this.machineManager = machineManager;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream=HeaderController.class.getResourceAsStream(COMMAND_PROMPT_TTF);
        Font font = Font.loadFont(inputStream, 26);
        titleLabel.setFont(font);
    }
}