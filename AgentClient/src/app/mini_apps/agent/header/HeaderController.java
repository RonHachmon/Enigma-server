package app.mini_apps.agent.header;


import app.mini_apps.agent.bodies.absractScene.MainAppScene;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.text.Font;


import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;



public class HeaderController extends MainAppScene implements Initializable   {
    public static final String COMMAND_PROMPT_TTF = "/resources/fonts/windows_command_prompt.ttf";


    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream inputStream=HeaderController.class.getResourceAsStream(COMMAND_PROMPT_TTF);
        Font font = Font.loadFont(inputStream, 26);
        titleLabel.setFont(font);
    }
}