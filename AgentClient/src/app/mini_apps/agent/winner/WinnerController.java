package app.mini_apps.agent.winner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WinnerController {

    @FXML
    private Label winnerLabel;

    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) winnerLabel.getScene().getWindow();
        stage.close();

    }

    public void setWinnerLabel(String winnerName) {
        this.winnerLabel.setText("Winner: "+winnerName);
    }
}
