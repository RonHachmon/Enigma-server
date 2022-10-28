package app.mini_apps.uboat.smallComponent;




import app.mini_apps.uboat.header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import utils.EnigmaUtils;

import java.io.InputStream;

public class CandidateController {

    public static final String COMMAND_PROMPT_TTF = "/resources/fonts/windows_command_prompt.ttf";
    @FXML
    private Label textLabel;

    @FXML
    private Label codeLabel;

    @FXML
    private Label agentLabel;

    private Tooltip fullText;
    public void setTextFont()
    {
       /* fullText.class.getResource(COMMAND_PROMPT_TTF);*/
        InputStream inputStream= HeaderController.class.getResourceAsStream(COMMAND_PROMPT_TTF);
        Font font = Font.loadFont(inputStream, 15);
        textLabel.setFont(font);
    }
    public void setText(String text)
    {
        fullText=new Tooltip(text);
        EnigmaUtils.hackTooltipStartTiming(fullText,500);
        textLabel.setTooltip(fullText);
        textLabel.setText(text);
    }
    public void setAgent(String text)
    {
        agentLabel.setText("ally "+text);
    }
    public void setCode(String text)
    {
        codeLabel.setText(text);
    }


}